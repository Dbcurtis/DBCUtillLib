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

import java.awt.Color;
import static java.awt.Color.RED;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import net.dbcrd.DBCUtilLib.ClassPreamble;
import net.dbcrd.DBCUtilLib.status.DBCStatus;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * Provides a Setting capability (sort of a smart Properties).
 *
 * @author Daniel B. Curtis
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "June 2015",
        currentRevision = 1,
        lastModified = "06/14/15",
        copyright = "(C) 2015 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public class Setting implements JSONString {

    /**
     * a String used to identify items in the PREFS
     */
    private static String ORG = "Setting1183";
    /**
     * a Preferences database replaced by ***TBD
     */
    //static Preferences PREFS = Preferences.userNodeForPackage(Setting.class);
    /**
     * a List of the current Settings
     */
    private static final List<Setting> SETTINGS = new LinkedList<>();
    /**
     * a map of the Setting names to the Setting
     */
    private final static Map<String, Setting> SETTING_CATALOG = new ConcurrentHashMap<>(100);
    /**
     * used to sync some accesses
     */
    private final static Object SYNC = new Object();
    /**
     * used to generate unique UND names
     */
    private final static AtomicInteger UNDEFINED_POST_FIX = new AtomicInteger();
    /**
     * the most recient THOMEDIR value
     */
    private static Path lastHomePath = Paths.get("");
    
    /**
     *
     */
    private static ExecutorService mwp=null;

    /**
     * the Logger
     */
    private static final Logger THE_LOGGER = Logger.getLogger(Setting.class.getName());
    
    /**
     * a local representation of the status set by initialize
     */
    static MyLocalStatus mySt = new MyLocalStatus();
    
    /**
     *
     * @param mwp My Worker Pool
     * @param status
     * @param package4Prefs
     */
    public static void initialize(ExecutorService mwp, DBCStatus status, Class<?> package4Prefs) {
        synchronized (SYNC) {
            Setting.mwp = mwp;
            setStatus(status);
            Preferences.userNodeForPackage(package4Prefs);
        }
    }

    /**
     *
     * @return an int that is the postfix for undefined names
     */
    private static int getPostFix() {
        return UNDEFINED_POST_FIX.getAndIncrement();
    }

    /**
     * Returns a Setting that matches the key. If no such setting exists, instantiate an UND setting, store it, and return it.
     *
     * @param key a String that is the key for the requested Setting
     * @return a Setting from the catalog that is mapped by the key an UND Setting if the key was unknown
     */
    public static Setting getSetting(final String key) {
        final Setting s = SETTING_CATALOG.get(key);
        Setting ss;
        if (null == s) {
            ss = Setting.factory();
            ss.setName(key);
            return ss;
        }
        return s;
    }

    /**
     * Generate an Absolute path using the lastHomePath and the relativeP path
     *
     * @param relativeP a Path that is a relative path
     * @return an absolute Path
     */
    public static Path makeAbsolute(final Object relativeP) {
        if (relativeP instanceof File) {
            final Path pth = ((File) relativeP).toPath();
            return lastHomePath.resolve(pth).normalize();

        }
        if (relativeP instanceof Path) {
            return lastHomePath.resolve((Path) relativeP).normalize();
        }
        if (relativeP instanceof String) {
            final Path pth = Paths.get((String) relativeP);
            return lastHomePath.resolve(pth).normalize();
        }
        throw new IllegalArgumentException("Argument must be a Path, File, or String");

    }

    /**
     * Checks if a key exists in the catalog, Synchronized with Factory
     *
     * @param key a String
     * @return a boolean true if the key does not exist in the catalog
     *
     */
    public static boolean isNotUsed(final String key) { //TODO not thread safe
        synchronized (SYNC) {
            return !SETTING_CATALOG.containsKey(key);
        }
    }

    /**
     * Remove the provided Setting from the catalog and mark the setting as deleted and valid as false. Synchronized with Factory.
     *
     * @param s a Setting to be removed
     * @return a boolean true if the Setting exists and the setting is removed from the catalog, false if the setting is not in
     * the catalog
     *
     * The provided Setting is renamed to "deleated" and its valid property set to false. It no longer exists in the catalog.
     *
     */
    static boolean removeSetting(final Setting s) {
        synchronized (SYNC) {
            if (SETTING_CATALOG.containsKey(s.name)) {
                SETTING_CATALOG.remove(s.name);
                getSETTINGS().remove(s);
                s.name = "deleated";
                s.valid = false;
                return true;
            }
            return false;
        }
    }

    /**
     * Clear the catalog;
     */
    static void clear() {
        SETTING_CATALOG.clear();
        getSETTINGS().clear();
    }

    /**
     * A Factory that generates Setting objects.
     *
     * @param args can be up to 0, 1 or 3 Objects long. If 0, the factory returns an undefined Setting with a unique name. If only
     * one Object that Object must be a JSONObject of a Setting, if 3 Objects, the first is a String that is the key, the second
     * is a SETTING_TYPE and the third is an Object;
     * @return a Setting;
     * @throws IllegalArgumentException if the number of arguments is not 0, 1 or 3, of if the argument type is incorrect..
     */
    public static Setting factory(final Object... args) {
        Setting s;
        synchronized (SYNC) {
            switch (args.length) {
                case 0:
                    while (true) {
                        s = new Setting();
                        if (!SETTING_CATALOG.containsKey(s.name)) {
                            break;
                        }
                    }
                    SETTING_CATALOG.put(s.name, s);
                    getSETTINGS().add(s);
                    return s;

                case 1:
                    if (args[0] instanceof JSONObject) {
                        final JSONObject jo = (JSONObject) args[0];
                        final String name = jo.getString("name");
                        if (!Setting.isNotUsed(name)) {
                            Setting ss = Setting.getSetting(name);
                            Setting.removeSetting(ss);
                        }

                        s = new Setting(jo);
                        SETTING_CATALOG.put(s.name, s);
                        getSETTINGS().add(s);
                        return s;
                    }
                    throw new IllegalArgumentException("single argument must be  JSONObject");
                case 3:
                    if (args[0] instanceof String && args[1] instanceof SETTING_TYPE && args[2] instanceof Object) {
                        s = new Setting((String) args[0], (SETTING_TYPE) args[1], args[2]);
                        SETTING_CATALOG.put(s.name, s);
                        getSETTINGS().add(s);
                        return s;
                    }
                    throw new IllegalArgumentException("three argument must be String, SETTING_TYPE, Object");
                default:
                    throw new IllegalArgumentException("0 arguments, 1 JSONObject or String, SETTING_TYPE, Object");
            }
        }
    }

 
    /**
     * validates the file system with the settings paths. It does not overwrite any directory or file, but if a THOMEDIR exists,
     * all TDIRs will be checked for existance. TPATH and TFILE will be checked for existance.
     *
     * Approrpate messages and status is generated
     *
     */
    public static void setupFileSystem() {
        Map<String, Setting> homeDirMap = new ConcurrentHashMap<>(50);
        Map<String, Setting> tDirMap = new ConcurrentHashMap<>(50);
        Map<String, Setting> pathMap = new ConcurrentHashMap<>(50);
        
      
        
        SETTING_CATALOG.forEach((k, v) -> {

            switch (v.stype) {
                case THOMEDIR:
                    homeDirMap.put(k, v);
                    break;
                case TDIR:
                    tDirMap.put(k, v);
                    break;
                case TFILE:
                case TPATH:
                    pathMap.put(k, v);
                    break;
                case TSTRING:
                case UND:
                default:
                    break;
            }
        });

//        for (Map.Entry<String, Setting> e : SETTING_CATALOG.entrySet()) {
//            Setting v = e.getValue();
//            //SETTING_TYPE stype = v.stype;
//            String k = e.getKey();
//            switch (v.stype) {
//                case THOMEDIR:
//                    homeDirMap.put(k, v);
//                    break;
//                case TDIR:
//                    tDirMap.put(k, v);
//                    break;
//                case TFILE:
//                case TPATH:
//                    pathMap.put(k, v);
//                    break;
//                case TSTRING:
//                case UND:
//                default:
//                    break;
//            }
//        }
        if (homeDirMap.isEmpty()) {
            mySt.setNote("Home Directory is not specified, unable to auto configure Settings Disk Structure", RED);
            return;

        } else if (homeDirMap.size() > 1) {
            mySt.setNote("Home Directory is multiply defined, unable to auto configure Settings Disk Structure", RED);
            return;
        }

        final Map.Entry<String, Setting> e = homeDirMap.entrySet().iterator().next();
        final Path p = (Path) e.getValue().getValue();
        if (Files.exists(p)) {
            mySt.setNote(".HomeDir Exists");
            if (Files.isDirectory(p)) {
                if (Files.isReadable(p) && Files.isWritable(p)) {
                    createDirectories(tDirMap);
                    validateFiles(pathMap);
                } else {
                    mySt.setNote("Home Directory access problems, unable to auto configure Settings Disk Structure", RED);
                }
            } else {
                mySt.setNote(MessageFormat.format("Setting {0} does not specify a directory.", e.getValue().name), RED);
            }

        } else {
            mySt.setNote(".HomeDir does not exist, creating directories", RED);
            createDirectories(homeDirMap);
            createDirectories(tDirMap);
        }
    }

    /**
     *
     * @param pathMap
     */
    private static void validateFiles(Map<String, Setting> pathMap) {
        pathMap.entrySet().stream().map((e) -> e.getValue()).forEach((s) -> {
            //final File f = (Setting.makeAbsolute((Path) s.getValue())).toFile();
            final Path p = Setting.makeAbsolute(s.getValue());
            if (Files.exists(p)) {
                if (!Files.isDirectory(p) && Files.isReadable(p)) {//f.isFile() && f.canRead()) {
                    mySt.setNote(MessageFormat.format("...Setting {0} verified", s.getName()));
                } else {
                    mySt.setNote(
                            MessageFormat.format(
                                    "...Setting {0} does not specify a file or specifies a file that cannot be read",
                                    s.getName()),
                            RED);
                }
            } else {
                mySt.setNote(MessageFormat.format("...File {0} for Setting {1} does not exist {1}", p, s.getName()),
                        RED);
            }
        });
    }

    /**
     *
     * @param dirMap
     */
    private static void createDirectories(Map<String, Setting> dirMap) {
        
        for (Map.Entry<String, Setting> e : dirMap.entrySet()) {
            final Path dirPath = (Path) e.getValue().getValue();

            try {
                Files.createDirectories(Setting.makeAbsolute(dirPath));
            } catch (IOException ex) {
                Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
                mySt.setNote(MessageFormat.format("Unable to create Directory {0}", dirPath), RED);
                continue;
            }
            mySt.setNote(MessageFormat.format("...Directory {0} verified/created.", dirPath));
        }
    }

    /**
     *
     * @param status
     */
    public static void setStatus(DBCStatus status) {
        mySt = new MyLocalStatus(status);
    }

    /**
     * @return the SETTINGS
     */
    static List<Setting> getSETTINGS() {
        return SETTINGS;
    }

    /**
     * @param aLastHomePath the lastHomePath to set
     */
    static void setLastHomePath(Path aLastHomePath) {
        lastHomePath = aLastHomePath;
    }
    /**
     * name of the Setting
     */
    private String name;
    /**
     * a SETTING_TYPE for the Setting
     */
    private SETTING_TYPE stype;
    /**
     * an Object corrosponding to the SETTING_TYPE
     */
    private Object value;
    /**
     * a boolean true if the Setting is valid
     */
    private boolean valid = true;

    /**
     * Instantiates a Setting Generates an undefined Setting with a unique name starting with "undefined"
     */
    private Setting() {
        name = new StringBuilder(255).append("undefined").append(getPostFix()).toString().trim();
        stype = SETTING_TYPE.UND;
        value = "undefined";
    }

    /**
     * Instantiates a Setting
     *
     * @param jo a JSONObject of a Setting;
     */
    private Setting(final JSONObject jo) {

        name = jo.getString("name");
        stype = SETTING_TYPE.valueOf(jo.getString("stype").trim());
        value = genVal(stype, jo.getString("value"));
        if (stype == SETTING_TYPE.THOMEDIR) {
            Setting.setLastHomePath((Path) genVal(stype, value.toString()));
        }

    }

    /**
     * Instantiates a Setting
     *
     * @param name a String with the name of the Setting
     * @param stype a SETTING_TYPE
     * @param value an Object
     *
     * if the Setting_TYPE is THOMEDIR, the lastHomePath is updated by the value
     */
    private Setting(final String name, final SETTING_TYPE stype, final Object value) {
        this.name = name.trim();
        this.stype = stype;
        this.value = value;
        if (stype == SETTING_TYPE.THOMEDIR) {
            Setting.setLastHomePath((Path) value);
        }
    }

    /**
     * Add this Setting to the catalog.
     */
    void catalog() {
        synchronized (SYNC) {
            SETTING_CATALOG.put(name, this);
        }
    }

    /**
     * @return the name of the Setting
     */
    public String getName() {
        return name;
    }

    /**
     * Removes this from the catalog, updates this.name and adds the updated setting to the catalog.
     *
     * @param name a String that is the name to set
     * @throws IllegalArgumentException if the desired name is already in the catalog
     */
    protected void setName(final String name) {
        if (!this.name.equals(name)) {
            synchronized (SYNC) {
                if (!SETTING_CATALOG.containsKey(name.trim())) {
                    SETTING_CATALOG.remove(this.name);
                    this.name = name;
                    SETTING_CATALOG.put(name, this);
                } else {
                    throw new IllegalArgumentException("duplicate setting name");
                }
            }
        }
    }

    /**
     * @return a SETTING_TYPE of the setting
     */
    public SETTING_TYPE getStype() {
        return stype;
    }

    
    /**
     *
     * @return an Object that is the value and is dependent on the SETTING_TYPE.
     */
    public Object getValue() {
        switch (stype) {
            case TSTRING:
                return value;
            case TPATH:

            case TFILE:

            case TDIR:
                final Object o = value;
                Path p;
                if (!(o instanceof Path)) {
                    p = Paths.get(o.toString());

                } else {
                    p = (Path) o;
                }
                return p;

            case THOMEDIR:
                return value;
            case UND:

            default:
                return "undefined";
        }
    }

    /**
     * @param value an Object that is the value to set
     */
    @Deprecated
    void setValue(final Object value) {
       ///old code this.value = value;
        setValue(this.stype, value);
    }
    /**
     * @param stype a SETTING_TYPE used to set the stype
     */
    @Deprecated
    void setStype(final SETTING_TYPE stype) {
       //old code this.stype = stype;
        setValue(stype, this.value);
    }
/**
 * 
 * @param stype a SETTING_TYPE used to set the stype
 * @param value an Object that is the value to set
 */
    void setValue(final SETTING_TYPE stype, final Object value) {
        this.stype = stype;
        switch (stype) {
            case TSTRING:
                this.value = value.toString();
                break;

            case TFILE:
                this.value = (value instanceof File) ? value : new File(value.toString());
                break;
            case TPATH:
//                this.value = (value instanceof Path) ? value : Paths.get(value.toString());
//                break;
            case TDIR:
//                this.value = (value instanceof Path) ? value : Paths.get(value.toString());
//                break;
            case THOMEDIR:
                this.value = (value instanceof Path) ? value : Paths.get(value.toString());
                break;
            case UND:
            default:
                this.value = "undefined";
        }
    }

    /**
     * Returns an Object (either String or Path) of the value of the Setting
     *
     * @param stype a SETTING_TYPE
     * @param value a String
     * @return a String or Path responsive to the SETTING_TYPE
     */
    private Object genVal(final SETTING_TYPE stype, final String value) {
        switch (stype) {
            case TSTRING:
                return value;
            case TPATH:

              //  return Paths.get(value);
            case TFILE:
              //  return Paths.get(value);
            case TDIR:
              //  return Paths.get(value);
            case THOMEDIR:
                return Paths.get(value);
            case UND:
                return "undefined";
        }
        return new Object();
    }

    /**
     *
     * @return
     */
    @Override
    public String toJSONString() {
        JSONObject jo = new JSONObject()
                .put("name", getName())
                .put("stype", getStype())
                .put("value", getValue().toString());
        return jo.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder(1024).append(getName()).append(':')
                .append(getStype()).append('-')
                .append(value.toString()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Setting) {
            Setting that = (Setting) o;
            return this.getName().equals(that.getName())
                    && this.getStype().equals(that.getStype())
                    && this.getValue().equals(that.getValue());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.getName());
        hash = 17 * hash + Objects.hashCode(this.getStype());
        hash = 17 * hash + Objects.hashCode(this.getValue());
        return hash;
    }

    /**
     * TBD
     */
    static class MyLocalStatus {

        /** TBD  */
        private static DBCStatus myStatus = null;

        /**
         *
         * @param mystatusa
         */
        private static void setMyStatus(DBCStatus mystatusa){
            myStatus=mystatusa;
        }

        /**
         *
         * @param gblStatus
         */
        MyLocalStatus(final DBCStatus gblStatus) {
           setMyStatus(gblStatus);
        }

        /**
         * TBD
         */
        MyLocalStatus() {
           setMyStatus(null);
        }

        /**
         *
         * @param notein a String
         */
        void setNote(final String notein) {
            if (null == myStatus) {
                THE_LOGGER.info(notein);
            } else {
                myStatus.setNote(notein);
            }

        }

        /**
         *
         * @param notein a String
         * @param colorcode a Color
         */
        void setNote(final String notein, final Color colorcode) {
            if (null == myStatus) {
                THE_LOGGER.info(notein);
            } else {
                myStatus.setNote(notein, colorcode);
            }

        }
    }

    /**
     * The typeCellEditor for the second column in the settings table
     */
    public static class TypeCellEditor extends AbstractCellEditor implements TableCellEditor {

        /* TBD */

        /**
         *
         */

        private static final long serialVersionUID = -1443356275326412445L;

        /* TBD */

        /**
         *
         */

        private final JComboBox<SETTING_TYPE> editor;

        /**
         * the constructor
         */
        public TypeCellEditor() {
            // Create a new Combobox with the array of values.
            editor = new JComboBox<>(SETTING_TYPE.values());
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getSelectedItem();
        }
/**
 * 
 * @param table
 * @param value
 * @param isSelected
 * @param row
 * @param column
 * @return 
 */
        @Override
        public Component getTableCellEditorComponent(final JTable table,
                final Object value, final boolean isSelected, final int row, final int column) {
            // Set the model data of the table
            if (isSelected) {
                editor.setSelectedItem(value);
                final TableModel model = table.getModel();
                model.setValueAt(value, row, column);
                if (null != mwp) {
                  //  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value="")
                  Future<?> f=  mwp.submit(new SetValueTask(model, value, row, column));
                  f.isDone();
                } else {
                    SwingUtilities.invokeLater(new SetValueTask(model, value, row, column));
                }

            }

            return editor;
        }

        /**
         * A SwingWorker to TBD****
         */
        private static class SetValueTask extends SwingWorker<Object, Void> {

            /* TBD */

            /**
             *
             */

            private final Object value;

           /* TBD */

            /**
             *
             */

            private final int row;

            /* TBD */

            /**
             *
             */

            private final int column;

             /* TBD */

            /**
             *
             */

            final private TableModel model;

            /**
             *
             * @param model a TableModel
             * @param value an Object
             * @param row an int that is the row number
             * @param column an int that is the column number
             */
            SetValueTask(final TableModel model, final Object value, final int row, final int column) {
                this.value = value;
                this.row = row;
                this.column = column;
                this.model = model;
            }
/**
 * 
 * @return
 * @throws Exception 
 */
            @Override
            protected Object doInBackground() throws Exception {
                return getObject(value, false);

            }

            /**
             * gets the found value Object and saves it in the specified row and the next column
             */
            @Override
            protected void done() {
                try {
                    final Object col3obj = get();
                    model.setValueAt(col3obj, row, column + 1);
                } catch (InterruptedException ignore) {

                } catch (ExecutionException ex) {
                    THE_LOGGER.log(Level.SEVERE, null, ex);
                }

            }

            /**
             * Asks for user input. Displays either a JOptionPand or a JFileCHooser
             *
             * @param value an Object
             * @param absolute a boolean if true stores an absolute path. If false stores a relative path. Is not used in the UND,
             * TSTRING or THOMEDIR cases.
             * @return an Object; a String for TSTRING and UND, a PATH for TPATH, TFILE, THOMEDIR, TDIR. For the Paths, if user
             * does not Approve, returns a path of an empty file.
             */
            private Object getObject(final Object value, final boolean absolute) {
                final SETTING_TYPE t = SETTING_TYPE.valueOf((String) value);

                final JFileChooser fc = new JFileChooser();
                File target;
              //  if (null != PREFS) 
              
             {
                    final String lastDir = Preferences.userNodeForPackage(Setting.class).get(ORG + "lastdir", "");
                    target = new File(lastDir);
                    fc.setCurrentDirectory(target);
              //  
             }
                switch (t) {
                    /* tbd */
                    case TSTRING:
                        return JOptionPane.showInputDialog("Provide Text").trim();

                    case TPATH:
                        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        int returnval = fc.showOpenDialog(null);
                        if (returnval == JFileChooser.APPROVE_OPTION) {
                            target = fc.getSelectedFile();
                         //   if (null != PREFS) {
                                Preferences.userNodeForPackage(Setting.class).put(ORG + "lastdir", target.toString());
                                try {
                                    Preferences.userNodeForPackage(Setting.class).sync();
                                } catch (BackingStoreException ex) {
                                    THE_LOGGER.log(Level.SEVERE, null, ex);
                                }
                         //   }
                            Path p = fc.getSelectedFile().toPath();
                            Path dd = lastHomePath.relativize(p);
                            Path ss = lastHomePath.resolve(dd).normalize();

                            return absolute ? ss : dd;

                        }
                        return (new File("")).toPath();

                    case TFILE:
                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        returnval = fc.showOpenDialog(null);
                        if (returnval == JFileChooser.APPROVE_OPTION) {
                            target = fc.getSelectedFile();
                           // if (null != PREFS) {
                                Preferences.userNodeForPackage(Setting.class).put(ORG + "lastdir", target.toString());
                                try {
                                    Preferences.userNodeForPackage(Setting.class).sync();
                                } catch (BackingStoreException ex) {
                                    THE_LOGGER.log(Level.SEVERE, null, ex);
                                }
                           // }
                            Path p = fc.getSelectedFile().toPath();
                            Path dd = lastHomePath.relativize(p);
                            Path ss = lastHomePath.resolve(dd).normalize();

                            return absolute ? ss : dd;

                        }
                        return (new File("")).toPath();

                    case THOMEDIR:
                       // if (null != PREFS) 
                    {
                            final String lastDir = Preferences.userNodeForPackage(Setting.class).get(ORG + "homedirdir", "");
                            target = new File(lastDir);
                            fc.setCurrentDirectory(target);
                        }
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        returnval = fc.showOpenDialog(null);
                        if (returnval == JFileChooser.APPROVE_OPTION) {
                            target = fc.getSelectedFile();
                          //  if (null != PREFS)
                          {
                                Preferences.userNodeForPackage(Setting.class).put(ORG + "homedirdir", target.toString());
                                try {
                                    Preferences.userNodeForPackage(Setting.class).sync();
                                } catch (BackingStoreException ex) {
                                    THE_LOGGER.log(Level.SEVERE, null, ex);
                                }
                            }
                            //lastHomePath = fc.getSelectedFile().toPath();

                            return lastHomePath = fc.getSelectedFile().toPath();

                        }
                        return (new File("")).toPath();

                    case TDIR:
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        returnval = fc.showOpenDialog(null);
                        if (returnval == JFileChooser.APPROVE_OPTION) {
                            target = fc.getSelectedFile();
                            //if (null != PREFS) 
                            {
                                Preferences.userNodeForPackage(Setting.class).put(ORG + "lastdir", target.toString());
                                try {
                                    Preferences.userNodeForPackage(Setting.class).sync();
                                } catch (BackingStoreException ex) {
                                    THE_LOGGER.log(Level.SEVERE, null, ex);
                                }
                            }
                            Path p = fc.getSelectedFile().toPath();
                            Path dd = lastHomePath.relativize(p);
                            Path ss = lastHomePath.resolve(dd).normalize();

                            return absolute ? ss : dd;

                        }
                        return (new File("")).toPath();

                    case UND:
                    default:
                        return "undefined";
                }

            }
        }
    }

    /**
     * Used to define the type of the Setting.
     */
    @SuppressWarnings("PublicInnerClass")
    public enum SETTING_TYPE {

        /** The Home Directory */
        THOMEDIR,
        /** a string value */
        TSTRING,
        /**  a Path value */
        TPATH, //TODO whats the difference between TPATH and TFILE?
        /** a Path to a file */
        TFILE, //TODO whats the difference between TPATH and TFILE?
        /** a Path to a directory */
        TDIR,
        /** a String of "undefined" */
        UND
    }
}
