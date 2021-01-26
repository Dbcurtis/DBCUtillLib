/*
 * Copyright (c) 2010-2015, Daniel B. Curtis <dbcurtis@dbcrd.net>
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.dbcrd.DBCUtilLib.status;

import com.vladium.utils.PropertyLoader;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.dbcrd.DBCUtilLib.BareBonesBrowserLaunch;
import net.dbcrd.DBCUtilLib.ClassPreamble;
import net.dbcrd.DBCUtilLib.FileUtils;

/**
 * <Strong>A Class that provides an API capture, display, and recover status notes.</strong>
 * It has its own ExecutorService so the class can be shutdown independently from other executorServices.
 */
@ClassPreamble(author = "Daniel B. Curtis",
        date = "Aug 2010 ",
        currentRevision = 3,
        lastModified = "03/30/2017",
        copyright = "(C) 2010-2017 by Daniel B. Curtis",
        lastModifiedBy = "Daniel B. Curtis")

public class MyStatus
        implements DBCStatus {

    /**
     *
     */
    private static final ExecutorService STATUS_WORKER_POOL = Executors.newCachedThreadPool();

    /**
     *
     */
    private final static Logger THE_LOGGER = Logger.getLogger(MyStatus.class.getName());

    /**
     * called to shutdown the status worker pool You should have already called dispose see:dispose Tries to shutdown nicely for
     * 5 seconds then does a shutdownNow().
     */
    public static void shutdown() {
        STATUS_WORKER_POOL.shutdown();
        try {
            STATUS_WORKER_POOL.awaitTermination(5, TimeUnit.SECONDS);
            if (!STATUS_WORKER_POOL.isTerminated()) {
                STATUS_WORKER_POOL.shutdownNow();

            }

        } catch (InterruptedException ignoreInterruptedException) {

        }
    }
    /**
     *
     */
    private final Object LOCK_STATUS;

    /**
     * Receives messages that are to be displayed in the message section of the status dialog. These messages are appended to
     * {@code TEXT}
     */
    private final BlockingQueue<String> blockingQ = new LinkedBlockingQueue<>();

    /**
     * Receives messages that are to be displayed in the message section of the status dialog. These messages are appended to
     * {@code TEXT} This blocking queue can be used by an instance of Status if Status needs multiple displays.
     */
    private final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(); //TODO how is this used?


    /**
     *
     */
    private boolean headful = false;

    /**
     * aborting
     */
    private volatile boolean iamAborting = false;


    /**
     * the text that is displayed in the status dialog
     */
    private final StringBuilder myText = new StringBuilder(4096);
    /**
     * The StatusDialog that is being controlled by this status
     */
    private Optional<StatusDialog> oDialog=Optional.empty();
    /**
     * Properties for notes.
     */
    private Optional<Properties> oLoggingTextProp = Optional.of(new Properties());

    /**
     * The status worker Future
     */
    private Optional<Future<?>> oWorkerFuture = Optional.empty();
    /**
     *
     */
    private String title = "";

    /**
     * @param headful          a boolean true to enable the display of the status window, false to not display the status window.
     * @param propertiesFileID a String that is supplied to a PropertyLoader to read the specified properties file. If no file is
     *                         found, a warning message is logged, but MyStatus continues to run.
     *                         If this happens, setNote will simply use the provided
     *                         text as default. Can be null or empty and if so, no error message.
     * @param title            a String that will be the first {@code <h1></h1>} value as well as used in a title tag
     */
    public MyStatus(final boolean headful, String propertiesFileID, String title) {
        this.LOCK_STATUS = new Object();
        this.headful = headful;
        this.title = title;
        if (propertiesFileID != null && !propertiesFileID.trim().isEmpty()) {
            oLoggingTextProp = Optional.ofNullable(PropertyLoader.loadProperties(propertiesFileID));
            if (!oLoggingTextProp.isPresent()) {
                final String msg =
                        MessageFormat.format("unable to load resource specified by {0}",
                                propertiesFileID);
                THE_LOGGER.log(Level.WARNING, msg);
                //loggingTextProp = new Properties();
            }
        }
//        else {
//            loggingTextProp = new Properties();
//        }

    }

    /**
     * invokes {@code MyStatus(false,"","default")}
     */
    public MyStatus() {
        this(false, "", "default");
    }
    /**
     * Gets a copy of the blocking queue and replaces the copy back on the blocking queue.
     *  <p>
     * Example invocation: {@code   List<String> errorList =  ((MyStatus)PrepApplication.myStatus).debuggingGetBlockingQCopy();}
     * </p>
     *
     * @return a list of String that is a copy of the blockingQ.
     */
    public List<String> debuggingGetBlockingQCopy() {
        synchronized (LOCK_STATUS) {
            final List<String> result = new ArrayList<>(blockingQ.size() + 20);
            blockingQ.drainTo(result);
            result.stream().forEach(blockingQ::add);
            return result;
        }
        
    }
    /**
     * Gets the contents of the blocking queue.
     *  <p>
     * Example invocation: {@code   List<String> errorList =  ((MyStatus)PrepApplication.myStatus).debuggingGetBlockingQReset();}
     * </p>
     *
     * @return a list of String that is a copy of the blockingQ.
     */
    public List<String> debuggingGetBlockingQDrain() {
        synchronized (LOCK_STATUS) {
            final List<String> result = new ArrayList<>(blockingQ.size() + 20);
            blockingQ.drainTo(result);
            return result;
        }
    }

    /**
     * Cancels the {@code StatusWorker} triggers the blocking Q, waits for the {@code StatusWorker} to complete, clears the
     * blocking queue, remembers last position of window on display and disposes the dialog. dispose should be called before
     * shutdown;
     */
    @Override
    public void dispose() {
        synchronized (LOCK_STATUS) {
            setIAmAborting(true);
            blockingQ.clear();
            if (oWorkerFuture.isPresent()) {
                oWorkerFuture.get().cancel(true);
            }
        }
        try {
            if (oWorkerFuture.isPresent()&& !oWorkerFuture.get().isCancelled()) {
                try {
                    oWorkerFuture.get().get(1, TimeUnit.SECONDS);
                } catch (ExecutionException ex) {
                    THE_LOGGER.log(Level.SEVERE, null, ex);
                } catch (TimeoutException | InterruptedException | CancellationException ignore) {

                }

                while (!oWorkerFuture.get().isDone()) { // if exception happend above
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        } finally {
            blockingQ.clear();
        }
        SwingUtilities.invokeLater(() -> {
            if (oDialog.isPresent()) {
                final StatusDialog dialog = oDialog.get();
                for (WindowListener wl : dialog.getWindowListeners()) {
                    dialog.removeWindowListener(wl);
                }
                dialog.jPBPhaseProgress.setIndeterminate(false);
                dialog.jPBTaskProgress.setIndeterminate(false);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

    }


    /**
     * Get the text from the status window.
     *
     * @return a String of the text that is being displayed in the note
     */
    @Override
    public String getText() {
        return myText.toString().trim();
    }

    /**
     * Initializes the progress bars. If you do not call this, phaseProgress and TaksProgress are set to 0-100
     *
     * @param phaseBarVal a Point with x=min val, y=max val for the phase bar
     * @param taskBarVal  a Point with x=min val, y=max val for the task bar
     */
    @Override
    public void initProgressBarVals(final Point phaseBarVal, final Point taskBarVal) {
        if (waitForDialog()) {
            SwingUtilities.invokeLater(() -> {

                if (oDialog.isPresent()) {
                    final StatusDialog dialog = oDialog.get();
                    dialog.jPBPhaseProgress.setMinimum(phaseBarVal.x);
                    dialog.jPBPhaseProgress.setMaximum(phaseBarVal.y);
                    dialog.jPBPhaseProgress.setValue(phaseBarVal.x);
                    dialog.jPBPhaseProgress.setStringPainted(false);
                    dialog.jPBTaskProgress.setMinimum(taskBarVal.x);
                    dialog.jPBTaskProgress.setMaximum(taskBarVal.y);
                    dialog.jPBTaskProgress.setValue(taskBarVal.x);
                    dialog.jPBTaskProgress.setStringPainted(false);
                    dialog.jPBTaskProgress.setString("??");
                    dialog.invalidate();
                }
            });
        }
    }

    /**
     *
     * @return a boolean, true if aborting.
     */
    public boolean isAborting() {
        return iamAborting;
    }


    /**
     * Resets the status box, makes the task progress bar indeterminate, sets the bar to its minimum value and resets any text in
     * the bar
     */
    @Override
    public void resetTask() {
        SwingUtilities.invokeLater(() -> {
            if (oDialog.isPresent()) {
                final StatusDialog dialog = oDialog.get();
                dialog.jPBTaskProgress.setIndeterminate(true);
                dialog.jPBTaskProgress.setValue(dialog.jPBTaskProgress.getMinimum());
                dialog.jPBTaskProgress.setStringPainted(false);
                dialog.invalidate();
            }
        });
    }
//    /**
//     * Sets a note with default color black
//     * @param notein a String that is the note to be added in black.
//     */
//   
//    public void setNote(final String notein) {
//        setNote(notein, BLACK);
//        
//    }

    /**
     * Prepare a note and add to the blocking queue. The string to be logged is prepended with the date info, wrapped in a
     * {@code <span style="color:#colorcode> date:logged string </span><br/>}
     *
     *
     * @param notein a String to be logged. This string can be html, but if so, should be in a span.
     * @param colorIn  a java.awt.Color Array for the color of the displayed text.  
     */
    @Override
    public void setNote(final String notein, final Color... colorIn) {

        synchronized (LOCK_STATUS) {
            if (iamAborting) {
                return;
            }
            Color color = Color.BLACK;
            if (colorIn.length>0){
                color = colorIn[0];
            }
            final String colorCode = "#" + Integer.toHexString(color.getRGB()).substring(2);
            final StringBuilder newString = new StringBuilder(255)
                    .append("<span style=\"color:").append(colorCode).append("\">")
                    .append(new Date(System.currentTimeMillis()).toString())
                    .append(": ")
                    .append(oLoggingTextProp.get().getProperty(notein, notein))
                    .append("</span><br/>");

            blockingQ.add(newString.toString());
        }
    }

    /**
     * Sets the phase bar
     *
     * @param val           an int that identifies which phase the program is in. see
     * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/BoundedRangeModel.html"><u>
     * Bounded Range Model</u> </a>
     * to understand how val interacts with the maximum and minimum of the phaseProgress bar.
     *
     * @param phaseStringIn a String that is painted in the bar.
     */
    @Override
    public void setPhase(final int val, final String phaseStringIn) {
        final String phaseString = oLoggingTextProp.get().getProperty(phaseStringIn, phaseStringIn);
        if (waitForDialog()) {

            SwingUtilities.invokeLater(() -> {
                final StatusDialog dialog = oDialog.get();
                dialog.jPBPhaseProgress.setIndeterminate(false);
                dialog.jPBPhaseProgress.setValue(val);
                dialog.jPBPhaseProgress.setString(phaseString);
                dialog.jPBPhaseProgress.setStringPainted(true);
                dialog.invalidate();

            });
        }
    }
    /**
     * Sets a value in the task bar, makes the bar determinate, and allows text on the bar
     *
     * @param val an int for the value see
     * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/BoundedRangeModel.html">
     * <u>Bounded Range Model</u> </a>
     * to understand how val interacts with the maximum and minimum of the phaseProgress bar.
     */
    @Override
    public void setTask(final int val) {
        
        if (waitForDialog()) {
            SwingUtilities.invokeLater(() -> {
                final StatusDialog dialog = oDialog.get();
                dialog.jPBTaskProgress.setIndeterminate(false);
                dialog.jPBTaskProgress.setValue(val);
                dialog.jPBTaskProgress.setStringPainted(true);
                dialog.invalidate();
                
            });
        }
    }
    /**
     * Set the task bar as indeterminate.
     *
     * @param message a String to be displayed in the task bar.
     */
    @Override
    public void setTaskIndeterminate(final String message) {
        if (waitForDialog()) {
            SwingUtilities.invokeLater(() -> {
                final StatusDialog dialog = oDialog.get();
                dialog.jPBTaskProgress.setIndeterminate(true);
                dialog.jPBTaskProgress.setStringPainted(true);
                dialog.jPBTaskProgress.setString(message);
                dialog.invalidate();
                
            });
        }
    }
    /**
     *
     * @param val an int that sets the maximum for the task bar see
     * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/BoundedRangeModel.html"><u>
     * Bounded Range Model </u></a>
     * to understand how val interacts with the maximum and minimum of the phaseProgress bar.
     */
    @Override
    public void setTaskMax(final int val) {
        if (waitForDialog()) {
            SwingUtilities.invokeLater(() -> {
                final StatusDialog dialog = oDialog.get();
                dialog.jPBTaskProgress.setIndeterminate(false);
                dialog.jPBTaskProgress.setValue(0);
                dialog.jPBTaskProgress.setMaximum(val);
                dialog.jPBTaskProgress.setStringPainted(true);
                dialog.invalidate();
                
            });
        }
    }
    /**
     * set aborting
     *
     * @param abort a boolean true to cause abort, false otherwise
     */
    private void setIAmAborting(final boolean abort) {
        iamAborting = abort;
    }

    /**
     * Wait for the {@code dialog} to be initialized (set to other than null)
     *
     * @return a boolean false if interrupted, true otherwise
     */
    private boolean waitForDialog() {
        boolean result = true;
        while (!oDialog.isPresent()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                result = false;
                break;
            }

        }
        return result;
    }

    /**
     *
     * @return
     */
    BlockingQueue<String> getBlockingQueue() {
        return blockingQueue;
    }

    /**
     * If not already aborting and if {@code aborting} is True, then set Aborting note and start the aborting process via
     * {@code setIAMAborting(true)}.
     *
     * @param aborting a boolean to set into aborting, True to abort,
     */
    void setAborting(final boolean aborting) {
        if (!isAborting() && aborting) {
            // setNote("Aborting", RED);
            setIAmAborting(true);
        }
    }

    /**
     * <strong> Displays the status information on a browser window</strong>
     */
    @SuppressWarnings("PublicInnerClass")
    public static class SaveStatusAndDisplay
            implements Runnable {

        /**
         *
         */
        final String status;
        /**
         * the path for the temorary file.
         */
        final Path tempPath;
        /**
         *
         */
        //final String urlString;
        final URL url;

        /**
         * Launch a browser window displaying the status
         *
         * @param status a String
         *
         * @throws MalformedURLException if the URL is malformed... should not happen
         * @throws IOException           if ...
         */
        public SaveStatusAndDisplay(final String status)
                throws MalformedURLException, IOException {
            
            final StringBuilder sb = new StringBuilder(10000)
                    .append(status).append("</body></html>");
            this.status = sb.toString();
            tempPath = Files.createTempFile("statusTemp", ".html");
            url = tempPath.toUri().toURL();
        }

        /**
         *
         */
        @Override
        public void run() {
            Thread.currentThread().setName("Status-2-browser");
            try {
                new FileUtils().bufferedWrite(status, tempPath);
            } catch (IOException ex) {
                Logger.getLogger(MyStatus.class.getName()).log(Level.SEVERE, null, ex);
            }
            BareBonesBrowserLaunch.send2Browser(url, "BrowserThread");
        }

    }

    /**
     * A Runnable that updates the information portion of the status window. When the StatusWorker is instantiated, it submits
     * itself to the WorkerPool and leaves the future in the global {@code workerFuture}
     *
     * @param <Object> Ignored
     */
    @SuppressWarnings("PublicInnerClass")
    public class StatusWorker<Object>
            implements Runnable {

       // StringBuilder content = new StringBuilder(10000);
        /**
         * used to indicate whether the instance is ready to accept data
         */
        transient boolean running = false;
 
        /**
         * TBD
         */
        @SuppressWarnings("LeakingThisInConstructor")
        public StatusWorker() {
            super();
            oWorkerFuture = Optional.ofNullable(STATUS_WORKER_POOL.submit(this));
        }

        /**
         * Sets {@code running} to true and loops so long as {@code running} stays true or the worker is not interrupted. Polls
         * the {@code BLOCKINGQ} every second and appends the contents of the queue into the {@code dialog.jTPLog} portion of the
         * window.
         */
        @Override
        public void run() {
            Thread.currentThread().setName("StatusWorker");
            initialize();

            final List<String> todo = new LinkedList<>();
//            final int updateSeconds = 2;  // 2 second delay between updates on the display
//            int updateCnt = 0;
            try {
                while (running && !isAborting()) {
                    final String str = blockingQ.poll(1, TimeUnit.SECONDS); //wait for a second to poll the queue
                    if (str != null && !str.isEmpty()) {
                        todo.add(str);              // save the first string
                        blockingQ.drainTo(todo);   //  save all the rest of the strings
                        todo.stream().forEach(myText::append);
                        todo.clear();
                      //  if (updateCnt-- < 0) {
                        //    updateCnt = updateSeconds;
                            
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                if (oDialog.isPresent()) {
                                    final StatusDialog dialog = oDialog.get();
                                    dialog.jTPLog.setText(myText.toString());
                                    dialog.invalidate();
                                }
                            });
                            //      }
                        } catch (InvocationTargetException ex) {
                           THE_LOGGER.log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (InterruptedException ie) {
                setAborting(true);
            }
        }

        /**
         * TBD
         */
        public void stopStatusWorker() {
            running = false;
            setAborting(true);
        }

        /**
         * Initialize the status window.
         */
        private void initialize() {
            myText.delete(0, myText.length());
            myText.append("<!DOCTYPE html><html><head><title>")
                    .append(title)
                    .append("</title>"
                            + "<meta charset=\"UTF-8\"><meta "
                            + "name=\"viewport\" content=\"width=device-width,"
                            + " initial-scale=1.0\"></head><body>")
                    .append("<h1>").append(title).append("</h1>");

            myText.append("<span style=\"color:#2E2EFE\">The program that generated this listing has (C) 2010-2015"
                    + " by Daniel B. Curtis</span><br/>");
            setNote("Status initalized");
            running = true;
            if (headful) {

                oDialog = Optional.ofNullable(new StatusDialog());
                if (oDialog.isPresent()) {
                    final StatusDialog dialog = oDialog.get();
                    // blockingQ = dialog.getBlockingQueue(); //use the dialog's blocking queue if headful.
                    dialog.jTPLog.setContentType("text/html");
                    dialog.setTitle(title);
                    SwingUtilities.invokeLater(() -> {
                        dialog.wpu.getRememberedPosition();
                        dialog.setVisible(true);
                        dialog.setVisible(true);
                        setNote("Status Window Running", Color.GREEN);
                    });
                }
            }
        }

    }

}
