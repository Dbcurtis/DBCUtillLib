package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * /////////////////////////////////////////////////////////
 * // Bare Bones Browser Launch //
 * // Version 1.5 //
 * // December 10, 2005 //
 * // Supports: Mac OS X, GNU/Linux, Unix, Windows XP //
 * // Example Usage: //
 * // String url = "http://www.centerkey.com/"; //
 * // BareBonesBrowserLaunch.openURL(url); //
 * // Public Domain Software -- Free to Use as You Like //
 * // Modified to by Daniel B. Curtis to use ProcessBuilder via OSProcess  *** not yet complete
 * /////////////////////////////////////////////////////////
 */
public final class BareBonesBrowserLaunch {

    /**
     *
     */
    private static final String ERR_MSG = "Error attempting to launch web browser";

    /**
     *
     */
    private static final List<String>UNIX_BROWSERS = Arrays.asList(
        "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape");

    /**
     *
     * @param url a String that will be converted to a URL
     */
    public static void openURL(final String url) {
        final String osName = System.getProperty("os.name");
        Optional<Process> oProc = Optional.empty();
        try {
            if (osName.startsWith("Mac OS")) {
                final Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                final Method openURL = fileMgr.getDeclaredMethod("openURL",
                        new Class<?>[]{String.class});
                openURL.invoke(null, new Object[]{url});
            }
            else if (osName.startsWith("Windows")) {
                oProc=Optional.ofNullable(Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url));
            }
            else { //assume Unix or Linux
                String browser = null;
                for (int count = 0; count < UNIX_BROWSERS.size() && browser == null; count++) {
                    if (Runtime.getRuntime().exec(
                            new String[]{"which", UNIX_BROWSERS.get(count)}).waitFor() == 0) {
                        browser = UNIX_BROWSERS.get(count);
                    }
                }
                if (browser == null) {
                    JOptionPane.showMessageDialog(null, ERR_MSG + ": no browser found" );
                }
                else {
                    oProc=Optional.ofNullable(Runtime.getRuntime().exec(new String[]{browser, url}));
                }
            }
            if (oProc.isPresent()){ //TODO why am I doing this here instead of making the calling program handle it?
               
                Process p = oProc.get();
                
                if (!p.waitFor(5, TimeUnit.MINUTES))
                {
                    System.out.println("browser or text display did not start within 5 minutes");
                }
            }
          
        }
        catch (InterruptedException iex){
            
        }
        catch (ClassNotFoundException|NoSuchMethodException |IllegalAccessException |InvocationTargetException |IOException ex) {
            JOptionPane.showMessageDialog(null, ERR_MSG + ":\n" + ex.getLocalizedMessage());
        }
    }

    /**
     *
     * @param url a String that will be converted to a URL
     */
    public static void openWindowsNotepad(final String url) {
        openURL(url);
    }

    /**
     *
     * @param url a YRL
     * @param threadId a String used to identify the browserThread
     */
    public static void send2Browser(final URL url, final String threadId) {
        final Thread browserThread = new Thread(() ->
        {
            BareBonesBrowserLaunch.openURL(url.toString());
        });
        browserThread.setName("DA-" + threadId.trim());
        browserThread.start();
    }

    /**
     *
     * @param tempfile a File that is the temporary file.
     * @param threadId a String that is the threadId
     *
     * @throws MalformedURLException if...
     */
    public static void send2Browser(final File tempfile, final String threadId) throws MalformedURLException {
        send2Browser(tempfile.toURI().toURL(), threadId);
    }

    /**
     * TBD
     */
    private BareBonesBrowserLaunch() {
        super();
    }
    private static final Logger LOG = Logger.getLogger(BareBonesBrowserLaunch.class.getName());
}
