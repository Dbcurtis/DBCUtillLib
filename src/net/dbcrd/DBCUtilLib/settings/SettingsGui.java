/*
 * Copyright (c) 2015, Daniel B. Curtis <dbcurtis@dbcrd.net>
 * All rights reserved.
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
package net.dbcrd.DBCUtilLib.settings;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import net.dbcrd.DBCUtilLib.ClassPreamble;
import net.dbcrd.DBCUtilLib.FileUtils;
import net.dbcrd.DBCUtilLib.WindowPlacementUtil;
import org.json.JSONArray;

/**
 *
 * @author Daniel B. Curtis
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "June 2015",
        currentRevision = 1,
        lastModified = "06/09/15",
        copyright = "(C) 2015 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public class SettingsGui extends javax.swing.JFrame {

    /**
     *
     */
    static SettingsTableModel settingsTableModel;
    /**
     * the Logger
     */
    private static final Logger THE_LOGGER = Logger.getLogger(SettingsGui.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 3053533186810271588L;

    /**
     *
     */
    private final transient Thread callingThread;

    /**
     *
     */
    boolean canceled;

    /**
     *
     */
    transient ExecutorService MY_WORKER_POOL;

    /**
     *
     */
    private final static String ORG = "SETTINGSP632";

    /**
     *
     */
    private final transient WindowPlacementUtil<SettingsGui> wpu;

    /**
     *
     */
   // private static Preferences PREFS = Preferences.userNodeForPackage(SettingsGui.class);

    /**
     *
     */
    private File target = null;

    /**
     *
     */
    private static SettingsGui displaySettings;

    /**
     *
     */
    boolean saveSettings;

    /**
     * Creates new window SettingsGui
     *
     * @param mwp propagate the system ExecutorService
     * @param classInPackage
     */
    public SettingsGui(ExecutorService mwp,Class<?> classInPackage) {
     //   PREFS = Preferences.userNodeForPackage(classInPackage);
        initComponents();
        saveSettings = false;
        MY_WORKER_POOL = mwp;
        wpu = new WindowPlacementUtil<>(this, ORG);
        callingThread = Thread.currentThread();
        canceled = false;

       // if (null != PREFS) 
       {
            final String lastDir = Preferences.userNodeForPackage(classInPackage).get(ORG + "lastdir", "");
            target = new File(lastDir);

        }

    }

    /**
     * This method is called from within the constructor to initialize the form. 
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPData = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTSettings = new javax.swing.JTable();
        jPControls = new javax.swing.JPanel();
        jBOK = new javax.swing.JButton();
        jBCancel = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMEdit = new javax.swing.JMenu();
        jMIAddSetting = new javax.swing.JMenuItem();
        jMIDelete = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SETTINGS");

        jPData.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), new java.awt.Color(153, 153, 153)));

        jTSettings.setModel(settingsTableModel);
        jScrollPane2.setViewportView(jTSettings);

        javax.swing.GroupLayout jPDataLayout = new javax.swing.GroupLayout(jPData);
        jPData.setLayout(jPDataLayout);
        jPDataLayout.setHorizontalGroup(
            jPDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 593, Short.MAX_VALUE)
            .addGroup(jPDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPDataLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPDataLayout.setVerticalGroup(
            jPDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 235, Short.MAX_VALUE)
            .addGroup(jPDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPDataLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPControls.setBorder(new javax.swing.border.MatteBorder(null));

        jBOK.setText("OK");
        jBOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBOKActionPerformed(evt);
            }
        });

        jBCancel.setText("Cancel");
        jBCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPControlsLayout = new javax.swing.GroupLayout(jPControls);
        jPControls.setLayout(jPControlsLayout);
        jPControlsLayout.setHorizontalGroup(
            jPControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPControlsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBCancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPControlsLayout.setVerticalGroup(
            jPControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPControlsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBOK)
                    .addComponent(jBCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMEdit.setText("Edit");

        jMIAddSetting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMIAddSetting.setText("Add Setting");
        jMIAddSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIAddSettingActionPerformed(evt);
            }
        });
        jMEdit.add(jMIAddSetting);

        jMIDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMIDelete.setText("Delete");
        jMIDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIDeleteActionPerformed(evt);
            }
        });
        jMEdit.add(jMIDelete);

        jMenuBar1.add(jMEdit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPControls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPControls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMIDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIDeleteActionPerformed

        final TableModel m = displaySettings.jTSettings.getModel();
        if (m instanceof SettingsTableModel) {
            final SettingsTableModel stm = (SettingsTableModel) m;
            THE_LOGGER.warning("The Delete Action is not implemented <<<<<<<<<<<<<<<<<<<");

        }


    }//GEN-LAST:event_jMIDeleteActionPerformed

    private void jBCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelActionPerformed

        displaySettings.canceled = true;
        displaySettings.saveSettings = false;
        displaySettings.setVisible(false);
        displaySettings.callingThread.interrupt();


    }//GEN-LAST:event_jBCancelActionPerformed

    private void jBOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBOKActionPerformed

        displaySettings.canceled = false;
        displaySettings.setVisible(false);
        displaySettings.saveSettings = true;
        displaySettings.callingThread.interrupt();


    }//GEN-LAST:event_jBOKActionPerformed

    private void jMIAddSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIAddSettingActionPerformed

        final TableModel tm = jTSettings.getModel();
        if (tm instanceof SettingsTableModel) {
            final SettingsTableModel stm = (SettingsTableModel) tm;
            final List<Setting> data = stm.getData();
            Setting.factory();//automatically added to the list
            stm.fireTableDataChanged();
            // the following two lines are to move the scroll so that you can see the newly added line
            jTSettings.getSelectionModel().setSelectionInterval(data.size(), data.size());
            jTSettings.scrollRectToVisible(new Rectangle(jTSettings.getCellRect(data.size(), 0, true)));
            this.invalidate();

        }


    }//GEN-LAST:event_jMIAddSettingActionPerformed

    @Override
    public void dispose() {
        wpu.rememberPosition();
        super.dispose();
    }

    /**
     * @param args the command line arguments
     * @param mwp propagete the system ExecutorSErvice
     * @param tmod a SettingsTableModel for the settings table
     * @param classinPackage
     * @return a SettingsGUI
     */
    public static SettingsGui main(final String args[], 
            final ExecutorService mwp, 
            final SettingsTableModel tmod,
            final Class<?> classinPackage) {

        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            THE_LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
        }

        settingsTableModel = tmod;
        displaySettings = new SettingsGui(mwp,classinPackage);
        displaySettings.wpu.getRememberedPosition();
        final TableColumn column = displaySettings.jTSettings.getColumnModel().getColumn(1);
        column.setCellEditor(new Setting.TypeCellEditor());

        /* Create and display the form */
        SwingUtilities.invokeLater(() -> {
            displaySettings.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    displaySettings.canceled = true;
                    displaySettings.setVisible(false);
                    displaySettings.callingThread.interrupt();
                }
            });
            displaySettings.setVisible(true);
            displaySettings.setVisible(true);
        });
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ignore) {
        }
        if (displaySettings.saveSettings) {

            final Setting[] sa = Setting.getSETTINGS().toArray(new Setting[Setting.getSETTINGS().size()]);
            final JSONArray ja = new JSONArray(sa);
            final Path dirPath = Setting.makeAbsolute( Setting.getSetting("DownLoadedData").getValue());
            final Path svPath = Paths.get(dirPath.toString(), timeStamp("settings.json"));
            try {
                new FileUtils().bufferedWrite(ja, svPath);
            } catch (IOException ex) {
                Logger.getLogger(SettingsGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return displaySettings;
    }
 /**
     * Provide a timeStamp.
     *
     * @param filename a String. I empty or without a ".", returns a timeStamp of the form yyyyMMdd_HHmm. If a String that contains a "." like
     * "foo.bar" returns "foo_yyyyMMdd_HHmm.bar"
     * @return a String that has been timeStamped per above.
     */
    static String timeStamp(String filename) {

        final StringBuilder sb = new StringBuilder(128).append(filename.trim());
        final SimpleDateFormat format1 = new SimpleDateFormat("_yyyyMMdd_HHmm");
        final Date date = Calendar.getInstance().getTime();
        final String dateSt = format1.format(date);
        int loc = sb.lastIndexOf(".");
        if (filename.trim().isEmpty() || loc == -1) {
            sb.append(dateSt).deleteCharAt(0);
            return sb.toString();
        } else {
            sb.insert(sb.lastIndexOf("."), dateSt);
            return sb.toString();
        }
    }

    /**
     * Return a Calendar from YYYYMMDD_HHmm where the HHmm are set to 0.
     *
     * @param timeStamp a String of the form YYYYMMDD_HHmm
     * @return a Calendar or null if the
     * @throws IllegalArgumentException if timestamp is of the wrong form
     */
    static Calendar getTimeStampCalendar(String timeStamp) {
        Calendar result = Calendar.getInstance();

        final Pattern fnp_pat = Pattern.compile("(\\d{4,4})(\\d\\d)(\\d\\d)_(\\d\\d)(\\d\\d)");
        final Matcher mx = fnp_pat.matcher(timeStamp);
        if (mx.find()) {
            final String year = mx.group(1);
            final String month = mx.group(2);
            final String day = mx.group(3);
            final int y = Integer.parseInt(year);
            final int m = Integer.parseInt(month) - 1;
            final int d = Integer.parseInt(day);

            result.clear();
            result.set(y, m, d, 0, 0, 0);
            result.get(Calendar.DAY_OF_YEAR);//force recompute
            return result;
        }
        throw new IllegalArgumentException("input must be in the form YYYYMMDD_HHmm");

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancel;
    private javax.swing.JButton jBOK;
    private javax.swing.JMenu jMEdit;
    private javax.swing.JMenuItem jMIAddSetting;
    private javax.swing.JMenuItem jMIDelete;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPControls;
    private javax.swing.JPanel jPData;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTSettings;
    // End of variables declaration//GEN-END:variables
}
