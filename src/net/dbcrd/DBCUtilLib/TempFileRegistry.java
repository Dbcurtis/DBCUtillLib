/*
 * Copyright (c) 2016, Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
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
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import static java.util.stream.Collectors.toList;

/**
 * A class for keeping track of temporary files.  Allows cleanup of left over temporary files at the start of execution
 * of the program requesting the temporary files.
 * 
 * Calling sequence to be described later ****
 * 
 *
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Sept 2016 ",
        currentRevision = 1,
        lastModified = "9/01/2016",
        copyright = "(C) 2016 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
        )
public class TempFileRegistry {

    /** the initial REGEX if none is in the repository */
    private final static String INITIAL_REGEX = Pattern.compile("^(a|b).*(\\.?y|z)$").pattern();
    /** the pattern to decode the regex in the registory */
    private final static Pattern PARSEIT_PATTERN = Pattern.compile("^(\\(.*\\))\\.\\*(\\(.*\\)$)");
    /** the logger */
    private static final Logger THE_LOGGER = Logger.getLogger(TempFileRegistry.class.getName());
    /** registry prefs */
  //  private Preferences prefs = Preferences.userNodeForPackage(TempFileRegistry.class);
    /** name of the parameter to use with the system registry */
    private final String registryKey;
    /** the type of registration file or system registory. if you cannot reach the registory, a file will be generated automaitically */
    private final RegistryType rt;

    /**
     *
     * @param name a String that is the key for the 
     * @param rt a RegistryType used to specify the type of registory entyr, whether in the system registory or in a file
     */
    public TempFileRegistry(final String name, RegistryType rt) {
        this.registryKey = name;
        this.rt = rt;
    }

    /**
     * Delete the registered files and log an info counting the number of deleted files.
     * If an IOException happens, it will be logged and the method return an empty List
     * 
     * @return a List of Paths identifying the deleted files. or empty if no deleted files or if an IOException happended
     */
    public List<Path> cleanupTempFiles() {
        try {
            final DeleteTempFiles junk = new DeleteTempFiles(registryKey, Preferences.userNodeForPackage(TempFileRegistry.class));
            THE_LOGGER.log(Level.INFO, "deleted {0} old temp files", junk.deletedFiles.size());
            return junk.deletedFiles;
        } catch (IOException ex) {
            THE_LOGGER.log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>(1);
    }

    /**
     *
     * @param prefix a String
     * @param suffix a String
     * @param directory a File
     *
     * @return a File
     *
     * @throws IOException if as expected
     */
    public File createTempFile(final String prefix, final String suffix, final File directory)
            throws IOException {

        final File result = File.createTempFile(prefix, suffix, directory); //make a temp file
        rememberTempFileParams(prefix, suffix, result.toPath().getParent()); // remember it
        return result;
    }

    /**
     * Create a temporary file and remember its characteristics
     * 
     * @param prefix a String
     * @param suffix a String
     *
     * @return a File that was created 
     *
     * @throws IOException if as expected
     */
    public File createTempFile(final String prefix, final String suffix)
            throws IOException {
        final File result = File.createTempFile(prefix, suffix);
        rememberTempFileParams(prefix, suffix, result.toPath().getParent());
        return result;
    }

    /**
     *
     * @param prefix a String
     * @param suffix String
     *
     * @return a Path 
     *
     * @throws IOException if as expected
     */
    public Path createTempFilePath(final String prefix, final String suffix)
            throws IOException {
        return createTempFile(prefix, suffix).toPath();
    }

    /**
     *
     * @param prefix a String
     * @param suffix a String
     * @param directory a Path to a directory
     *
     * @return a Path to the temporary file
     *
     * @throws IOException if as expected
     */
    public Path createTempFilePath(final String prefix, final String suffix, final Path directory)
            throws IOException {
        return createTempFile(prefix, suffix, directory.toFile()).toPath();
    }

    /**
     *
     * @param gri a GetRegInfo
     * @param prefix a String
     * @param suffix a String 
     *
     * @return an Optinoal of Pattern
     */
    private Optional<Pattern> genREGEX(final GetRegInfo gri, final String prefix, final String suffix) {
        final String currentREGEXin = gri.getPat().pattern();
        final String currentREGEX = currentREGEXin.trim().isEmpty() ? "(().*(.?())" : currentREGEXin;
        final Matcher mx = PARSEIT_PATTERN.matcher(currentREGEX);
        boolean ok = mx.find();
        Optional<Pattern> defaultResult = Optional.empty();

     
        if (ok) {
            final Jjj j = new Jjj(mx.group(1), prefix);
            final Set<String> prefixSet = j.doit();
            final String g2 = mx.group(2);
            final String g2Fixed = g2.substring(3, g2.length() - 1);

            final Set<String> suffixSet = new Jjj(g2Fixed, "").doit();

            if (suffix != null && !suffix.isEmpty()) {
                String modSuffix = suffix.trim().startsWith(".") ? suffix.substring(1) : suffix; // remove leading dot
                suffixSet.add(modSuffix);
            }
            final StringBuilder updatedRegex = new StringBuilder(1000)
                    .append("((");
            prefixSet.stream().forEach((s) -> updatedRegex.append(s).append('|'));
            updatedRegex.deleteCharAt(updatedRegex.length() - 1)
                    .append(").*(\\.?(");
            suffixSet.stream().forEach((s) -> {
                String ss = s.startsWith(".") ? s.substring(1) : s;
                updatedRegex.append(ss).append('|');
            });
            updatedRegex.deleteCharAt(updatedRegex.length() - 1)
                    .append(")))");

            try {
                final Pattern testpat = Pattern.compile(updatedRegex.toString());
                return Optional.ofNullable(testpat);
            } catch (PatternSyntaxException pse) {
             
                return defaultResult;
            }

        }

        return defaultResult;
    }

    
    
    /**
     *
     * @param prefix    a String
     * @param suffix    a String
     * @param directory a Path
     */
    private void rememberTempFileParams(final String prefix, final String suffix, final Path directory) {//TODO this is wrong
        GetRegInfo gri = new GetRegInfo(registryKey, Preferences.userNodeForPackage(TempFileRegistry.class));
        Optional<Pattern> oNewREGEX = genREGEX(gri.doit(), prefix, suffix);
        if (oNewREGEX.isPresent()) {
            new SaveRegInfo(directory, oNewREGEX.get(), registryKey, Preferences.userNodeForPackage(TempFileRegistry.class)).doit();
        }
    }

    /**
     * Class to get the registory information
     */
    private static class GetRegInfo {

        /** TBD */
        private boolean ok = true;
        /** TBD */
        private Pattern pat;
        /** TBD */
        final String myRegistoryKey;
        /** tbd */
        Optional<Path> oDirectory = Optional.empty();
        /** TBD */

      
        final Preferences prefs;

        /**
         *
         * @param registryKey a String
         * @param prefs       a Preferences
         */
        GetRegInfo(final String registryKey, final Preferences prefs) {
            super();
            this.prefs = prefs;
            this.myRegistoryKey = registryKey;
        }

        /**
         *
         * @return a GetRegInfo which is this
         */
        GetRegInfo doit() {
            ok = false;
            final String reg = prefs.get(myRegistoryKey, "");
            if (reg.isEmpty() || !reg.contains("@")) {

                return this;
            } else {
                final String[] parts = reg.split("@");
                Path p;
                try {
                    p = Paths.get(new URI(parts[0]));
                } catch (URISyntaxException ex) {
                    THE_LOGGER.log(Level.SEVERE, null, ex);
                    p = null;
                }
                oDirectory = Optional.ofNullable(p);
                pat = Pattern.compile(parts[1]);
                ok = true;
                return this;
            }

        }

        /**
         *
         * @return a Pattern
         */
        Pattern getPat() {
            if (pat == null) {
                return Pattern.compile("");
            }
            return pat;
        }
        /**
         *
         * @return
         */
        Pattern getPattern(){
            return pat;
        }

        /**
         *
         * @return a boolean true if the conversion was ok, false otherwise
         */
        boolean isOK() {
            return ok;
        }

        /**
         * tbd
         */
        private static class RegistryInfo
                implements Comparable<RegistryInfo> {

            /** TBD */
            final String directory;

            /** TBD */
            final String prefix;

            /** TBD */
            final String suffix;

            /** TBD */
            final String tostring;

            /**
             *
             * @param prefix a String
             * @param suffix a String
             */
            RegistryInfo(final String prefix, final String suffix) {
               // this(prefix, suffix, null);
                super();
                this.prefix = prefix;
                this.suffix = suffix;
                this.directory = "";
                final StringBuilder sb = new StringBuilder(256)
                        .append(prefix).append(", ")
                        .append(suffix).append(", none");
                        //.append(directory);

                tostring = sb.toString();
            }

            /**
             *
             * @param prefix a String
             * @param suffix a String
             * @param directory  Patg
             */
            RegistryInfo(String prefix, String suffix, Path directory) {
                super();
                this.prefix = prefix;
                this.suffix = suffix;
                this.directory = directory.toString();
                final StringBuilder sb = new StringBuilder(256)
                        .append(prefix).append(", ")
                        .append(suffix).append(", ")
                        .append(directory);

                tostring = sb.toString();
            }

            @Override
            public int compareTo(RegistryInfo that) {
                return this.tostring.compareTo(that.tostring);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o instanceof RegistryInfo) {
                    RegistryInfo that = (RegistryInfo) o;
                    return this.tostring.equals(that.tostring);
                }
                return false;
            }

            @Override
            public int hashCode() {
                int hash = 3;
                hash = 47 * hash + Objects.hashCode(this.tostring);
                return hash;
            }

            @Override
            public String toString() {
                return tostring;
            }

            /**
             *
             * @return
             */
            String generateREGEX() {
                StringBuilder sb = new StringBuilder(1000);

                return sb.toString();

            }
        }
    }
    /**
     * TBD
     */
    private  static  class Jjj {
        /** TBD */
        final StringBuilder group;
        /** TBD */
        final String xfix;
        
        /**
         *
         * @param groupin a String
         * @param xfix a String
         */
        Jjj(final String groupin, final String xfix) {
            super();
            this.group = new StringBuilder(256).append(groupin);
            group.deleteCharAt(group.length() - 1).delete(0, 2);
            this.xfix = xfix.trim();
        }
        
        /**
         *
         * @return a Set of String
         */
        Set<String> doit() {
            final Set<String> fixSetRaw = new ConcurrentSkipListSet<>(Arrays.asList(group.toString().split("\\|")));
            final StringBuilder sb = new StringBuilder(156); // for suffix, the leading dot should not show up
            final Set<String> fixSet = new ConcurrentSkipListSet<>(
                    fixSetRaw.stream().map((s) -> {
                        sb.delete(0, sb.length()).append(s);
                        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == ')') {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        return sb.toString();
                    }).collect(toList()));
            fixSet.add(xfix);
            
            final Set<String> result = new ConcurrentSkipListSet<>();
            result.addAll(fixSet.stream().filter((s) -> !s.trim().isEmpty()).collect(toList()));
            return result;
            
        }
    }

    /**
     * a Class to reset the registry information
     */
    private static class ResetRegInfo {

        /** the specified registry key*/
        final String myRegistryKey;
        /** the specified Preferences */
        final Preferences prefs;

        /**
         *
         * @param registoryKey a String
         * @param prefs a String
         */
        private ResetRegInfo(final String registoryKey, final Preferences prefs) {

            this.prefs = prefs;
            this.myRegistryKey = registoryKey;
        }

        /**
         * save the key without a value
         */
        void doit() {

            prefs.put(myRegistryKey, "");
            try {
                prefs.sync();
            } catch (BackingStoreException ex) {
                THE_LOGGER.log(Level.SEVERE, "setPosition", ex);
            }
        }

    }

    /**
     * tbd
     */
    private static class SaveRegInfo {

        /** a Path to the directory */
        Path directory;

        /** the registry key */
        String myRegistryKey;

        /** the prefercencies*/
        Preferences prefs;

        /** the regex string */
        String regex;

        /**
         *
         * @param directory a Path
         * @param pattern a Pattern
         * @param registryKey a String
         * @param prefs a Preferences
         */
        private SaveRegInfo(final Path directory, final Pattern pattern, final String registryKey, final Preferences prefs) {
            this.regex = pattern.pattern();
            this.directory = directory;
            this.prefs = prefs;
            this.myRegistryKey = registryKey;
        }

        /**
         * saves a string with the directory@regex into the preferences
         */
        void doit() {
            final String dirStr = directory.toUri().toString();
            prefs.put(myRegistryKey, dirStr + "@" + regex);
            try {
                prefs.sync();
            } catch (BackingStoreException ex) {
                THE_LOGGER.log(Level.SEVERE, "setPosition", ex);
            }
        }

    }

    /**
     * class to delete the temporary files
     */
    static class DeleteTempFiles {

        /**
         * a List of Path that has the paths of the deleated files
         */
        List<Path> deletedFiles = new ArrayList<>(1);

        /**
         *
         * @param registryKey a String
         * @param prefs a Preferences
         *
         * @throws IOException if as expected
         */
        DeleteTempFiles(String registryKey, Preferences prefs)
                throws IOException {
            super();

            final GetRegInfo gri = new GetRegInfo(registryKey, prefs);
            gri.doit();
            if (gri.isOK()) {
                final Optional<Path> oPath = gri.oDirectory;
                if (oPath.isPresent()) {

                    final Path p = oPath.get();
                    final Pattern pat = gri.getPat();
                    final Matcher mx = pat.matcher("");
                    final File parent = p.toFile();
                    final File[] content = parent.listFiles(); //get the files in the directory
                    final List<File> files = Arrays.asList(content);
                    final List<Path> paths = files.stream().map((f) -> f.toPath()).collect(toList());
                    deletedFiles = paths.stream()
                            .filter((f) ->
                                    Files.exists(f, LinkOption.NOFOLLOW_LINKS)
                                    && !Files.isDirectory(f, LinkOption.NOFOLLOW_LINKS)
                                    && Files.isRegularFile(f, LinkOption.NOFOLLOW_LINKS)
                                    && Files.isReadable(f)
                                    && Files.isWritable(f)
                                    && mx.reset(f.getFileName().toString()).matches()
                            )
                            .collect(toList()); // collect the files that exist, not a directory, is regular file, is readable is writalbe, and matching the names
                    deletedFiles.parallelStream() // delete the files
                            .forEach((f) -> {
                                try {
                                    Files.delete(f);
                                } catch (IOException ioe) {
                                    THE_LOGGER.log(Level.WARNING, null, ioe);
                                }
                            });
                    new ResetRegInfo(registryKey, prefs).doit(); // reset the registory

                } else {
                    throw new AssertionError("should never get here");
                }
            }
        }
    }

    /**
     *  an enum to decide select which type of registry to use
     */
    @SuppressWarnings("PublicInnerClass")
    public enum RegistryType {

        /** select the file type*/
        FILE_TYPE,

        /** select the registory type */
        SYSTEM_TYPE;
    }

}
