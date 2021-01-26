/*
 * Copyright (c) 2015-2016, Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <b>FileUtility Class.</b>
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Jan 2015",
        currentRevision = 2,
        lastModified = "11/20/16",
        copyright = "(C) 2015-2016 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
/**
 * A class that provides useful file operations
 */
public class FileUtils
{

    /** TBD */
    private static final Pattern LINE_PAT = Pattern.compile("^.+$", Pattern.MULTILINE);
    /** Pattern to match multiple spaces */
    static final private Pattern TAB_PAT = Pattern.compile("\\s{2,}");

    /** THE LOGGER */
    private static final Logger THE_LOGGER = Logger.getLogger(FileUtils.class.getName());

    /**
     *
     */
    final static Path USER_DIR_P = Paths.get(System.getProperty("user.dir"));

    /**
     *
     */
    final static Path USER_HOME_P = Paths.get(System.getProperty("user.home"), "Documents");

    /**
     * <b>Delete the contents of a Directory, delete the Directory, remake an empty Directory</b>
     *
     * @param path a Path pointing to a folder
     *
     * @return a boolean false if unable to recursive delete the contents of the folder, true otherwise.
     *
     * @throws FileNotFoundException if the folder is not found
     * @throws IOException           if as expected.
     */
    public static boolean deleteContentsOf(final Path path)
            throws FileNotFoundException, IOException
    {
        // delete the contents of the Directory and the Directory itself
        boolean endFlag = deleteRecursive(path);

        if (!endFlag)
        {
            return false;
        }
        try
        {
            // create the Directory
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException faee)
        {
            return false;
        }
        return true;

    }

    /**
     * By default File#delete fails for non-empty directories, it works like "rm". We need something a little more brutual - this
     * does the equivalent of "rm -r"
     *
     * @param path Root File Path
     *
     * @return true iff the file and all sub files/directories have been removed
     *
     * @throws FileNotFoundException see: http://stackoverflow.com/questions/779519/delete-files-recursively-in-java
     */
    public static boolean deleteRecursive(final Path path)
            throws FileNotFoundException, IOException
    {
        if (!Files.exists(path))
        {
            throw new FileNotFoundException(path.toFile().getAbsolutePath());
        }
        boolean ret = true;
        if (Files.isDirectory(path))
        {
            for (File f : path.toFile().listFiles())
            {
                ret = ret && FileUtils.deleteRecursive(f.toPath());
            }
        }
        return ret && Files.deleteIfExists(path);
    }

    /**
     * Instantiate the class
     */
    public FileUtils()
    {
        super();
    }

    /**
     * <b>Writes a JSONArray to Path</b>
     *
     * @param ja a JSONArray
     * @param p  a Path
     *
     * @throws IOException if IO operation fails.
     */
    public void bufferedWrite(final JSONArray ja, final Path p)
            throws IOException
    {
        try (final Writer w1 = new FileWriter(p.toFile()))
        {
            ja.write(w1);
        }
    }

    /**
     * <b>Writes a JSONObject to Path</b>
     *
     * @param jo a JSONObject
     * @param p  a Path
     *
     * @throws IOException if IO operation fails.
     */
    public void bufferedWrite(final JSONObject jo, final Path p)
            throws IOException
    {
        try (final Writer w1 = new FileWriter(p.toFile()))
        {
            jo.write(w1);
        }

    }

    /**
     * <b>Writes to a path</b>
     *
     * @param content a String to be written
     * @param fileP   a Path to save the content to
     *
     * @throws java.io.IOException if as expected
     */
    public void bufferedWrite(final String content, final Path fileP)
            throws IOException
    {
        final List<String> lines = new ArrayList<>(content.length() / 20 > 10000 ? 10000 : content.length() / 20);
        final StringBuilder sb = new StringBuilder(content);
        final Matcher mx = LINE_PAT.matcher(sb);
        while (mx.find())
        {
            lines.add(mx.group());
        }
        bufferedWrite(lines, fileP);
    }

  /**
     * <b>Writes Stream of String to a path</b>
     *
     * Each String is terminanted.
     *
     * @param content a Stream of String to be written
     * @param fileP   a Path to save the content to
     * @throws java.io.IOException if... as expected
     */
       public void bufferedWrite(final Stream<String> content, final Path fileP) throws IOException
    {
        try (final BufferedWriter bWriter = Files.newBufferedWriter(fileP, Charset.forName("utf-8"));
               final PrintWriter writer = new PrintWriter(bWriter))
        {
            content.forEachOrdered(writer::println);
        } catch (IOException ex)
        {
            throw ex;
        }
    }

    /**
     * <b>Writes a List of String to a path</b>
     *
     * @param content a List of String to be written
     * @param fileP   a Path to save the content to
     * @throws java.io.IOException if... as expected
     */
       public void bufferedWrite(final List<String> content, final Path fileP) throws IOException
    {
        bufferedWrite(content.stream(),fileP);
    }

    /**
     * Copies the contents of a url to a path.
     *
     * @param in        a URL
     * @param out       a Path
     * @param overwrite a boolean if true will allow overwrite of an existing file at the path.
     *
     * @throws IOException but will close the Stream first
     */
    public void copyURL2Path(final URL in, Path out, boolean overwrite)
            throws IOException
    {
        InputStream s = in.openStream();
        if (overwrite)
        {
            Files.copy(s, out, StandardCopyOption.REPLACE_EXISTING);
        } else
        {
            Files.copy(s, out);
        }
    }


    /**
     * If the provided path is an absolute path, the path is tested to verify the path point to a readable regular file. If the
     * provide path is not an absolute path, the user.dir will be searched for a matching file and if not found depth level of
     * subdirectories in user.dir will be searched. If not found, user.home /doocuments will be searched, and if not found depth
     * level of subdirecties will be found
     * <p>
     * P can contain constructs such as "./name" and "../../name" not yet implemented
     * the file depth is adjusted such that if ../../name is applied, the deapth will be 2 deeper then specified
     * but no adjustment if ./name, not yet implemented
     *
     *
     * @param p a Path to match against
     * @param depthin a vararg with an int[0] value indicating how deep the directory tree should be followed
     *
     * @return a Stream of Path of the found file(s) if any readable regular file is found
     *
     * @throws java.io.FileNotFoundException if no file is found, is found but is a directory, is not a directory but unreadable
     */
    public Stream<Path> findFile(final Path p, int... depthin)
            throws FileNotFoundException
    {
        Objects.requireNonNull(p, "path requied");
        final Path fileNameP = p.getFileName();
        final int depth = depthin != null && depthin.length > 0 ? depthin[0] : Integer.MAX_VALUE;
        final List<Path> result = new ArrayList<>(10);

        //  Path pRHp = USER_HOME_P.relativize(p);
        //  Path pRUp = USER_DIR_P.relativize(p);
        if (p.isAbsolute())
        {
            return processAbsolutePath(p, result);
        } else
        {
            try
            {
                return findFile(p.toAbsolutePath().normalize());
            } catch (FileNotFoundException ignoreFileNotFoundException)
            {
            }
            final Path parentPath = p.getParent();
            processRelativePath(fileNameP, result,parentPath, p, depth);
        }
        if (result.isEmpty())
        {
            throw new FileNotFoundException(fileNameP.toString());
        }
        final Set<Path> tempset = new HashSet<>(100);
        tempset.addAll(result);
        return tempset.stream();

    }
    /**
     * <b>Find a file by URI</b>
     *
     * @param uriIn URI
     * @param depth a vararg of int
     *
     * @return a Stream of Path
     *
     * @throws FileNotFoundException
     */
    public Stream<Path> findFile(final URI uriIn, int... depth)
            throws FileNotFoundException
    {
        Objects.requireNonNull(uriIn, "uriIn requied");
        return findFile(Paths.get(uriIn).getFileName(), depth);
    }
    /**
     * <b>Find a file by URI</b>
     *
     * @param fileNameIn a String with the filename
     * @param depth a vararg that if present is an int that specifies the depth of the directory search
     * based from the TBD***
     *
     * @return a Stream of Path
     *
     * @throws FileNotFoundException
     */
    public Stream<Path> findFile(final String fileNameIn, int... depth)
            throws FileNotFoundException
    {
        Objects.requireNonNull(fileNameIn, "fileNameIn requied");
        final String fileName = Objects.requireNonNull(fileNameIn, "Null argument not allowed").trim();
        if (fileName.isEmpty())
        {
            Objects.requireNonNull(null,"Empty argument not allowed");
           // throw new IllegalArgumentException("Empty argument not allowed");
        }
       // final Path p = Paths.get(fileName);//TODO can perhaps make this better
        return findFile(Paths.get(fileName), depth);
    }

    /**
     * <b>Read path and generate a stream of lines of the content of the path</b>
     *
     * @param inpathIn a Path
     * @param options a vararg of Object
     *
     * @return a Stream of String (lines)
     *
     * @throws java.io.FileNotFoundException if the file does not exist
     */
    public Stream<String> readPathToLineStream(final Path inpathIn, Object... options)
            throws FileNotFoundException
    {
        return readPathToLines(inpathIn, options).stream();

    }
    /**
     * Read path and generate a list of lines of the content of the path
     *
     * @param inpathIn a Path
     * @param options
     *
     * @return a List of String (lines)
     *
     * @throws java.io.FileNotFoundException if the file does not exist
     */
    @Deprecated
    public List<String> readPathToLines(final Path inpathIn, Object... options)
            throws FileNotFoundException
    {
        Charset cs = Charset.forName("ISO-8859-1");

        if (options != null && options.length > 0 && options[0] instanceof Charset)
        {
            cs = (Charset) options[0];
        }
        List<String> lines = new ArrayList<>(10);
        final Path inpath = Objects.requireNonNull(inpathIn, "Null Path is illegal");

        if (!Files.exists(inpath))
        {
            throw new FileNotFoundException(inpath.toString());
        }

        try
        {
            lines = Files.readAllLines(inpath, cs);

        } catch (IOException ex)
        {
            THE_LOGGER.log(Level.SEVERE, "Unable to read file", ex);
        }
        return lines;
    }

    /**
     * Read a Path and separate lines with a new line.
     *
     * @param inpathIn a Path
     * @param options  a Charset[]
     *
     * @return a StringBuilder
     *
     * @throws java.io.FileNotFoundException if the specified file not found
     */
    public StringBuilder readPathToSBLines(final Path inpathIn, Object... options)
            throws FileNotFoundException
    {
        final Path inpath = Objects.requireNonNull(inpathIn, "Null Path is illegal");

        if (!Files.exists(inpath))
        {
            throw new FileNotFoundException(inpath.toString());
        }

        Charset cs = Charset.forName("ISO-8859-1");

        if (options != null && options.length > 0 && options[0] instanceof Charset)
        {
            cs = (Charset) options[0];
        }
        Object[] opts =
        {
            cs
        };
        // final List<String> lines = readPathToLines(inpath, opts);
        final StringBuilder sb = new StringBuilder(20000);
        readPathToLineStream(inpath, opts).forEach((s) ->
                //lines.stream().forEach((s) ->
                {
                    sb.append(s).append('\n');
                });
        final Matcher tabmx = TAB_PAT.matcher(sb);
        int idx = 0;
        while (tabmx.find(idx))
        {
            sb.replace(tabmx.start(), tabmx.end(), " ");
            idx = tabmx.start();
        }
        sb.trimToSize();
        return sb;
    }

    /**
     * Read a Path and separates lines with a space. Multiple whitespace is converted to a single space.
     *
     * @param inpathIn a Path
     * @param options  an array of Object. options[0] can be a Charset. The default is ISO-8859-1
     *
     * @return a StringBuilder
     *
     * @throws java.io.FileNotFoundException if the path does not lead to an existing file.
     */
    public StringBuilder readPathToSBSpace(final Path inpathIn, Object... options)
            throws FileNotFoundException
    {
        final Path inpath = Objects.requireNonNull(inpathIn, "Null Path is illegal");
        if (!Files.exists(inpath))
        {
            throw new FileNotFoundException(inpath.toString());
        }

        Charset cs = Charset.forName("ISO-8859-1");

        if (options != null && options.length > 0 && options[0] instanceof Charset)
        {
            cs = (Charset) options[0];
        }
        Object[] opts =
        {
            cs
        };
        // final List<String> lines = readPathToLines(inpath, opts);
        final StringBuilder sb = new StringBuilder(20000);
        readPathToLineStream(inpath, opts).forEach((s) ->
                {
                    sb.append(s).append(' ');
                });
        final Matcher tabmx = TAB_PAT.matcher(sb);
        int idx = 0;
        while (tabmx.find(idx))
        {
            sb.replace(tabmx.start(), tabmx.end(), " ");
            idx = tabmx.start();
        }
        sb.trimToSize();
        return sb;
    }

    /**
     *
     * @param p      an Absolute Path
     * @param result a List of Path that includes any results found so far
     *
     * @return a Stream of Path
     *
     * @throws FileNotFoundException
     */
    private Stream<Path> processAbsolutePath(final Path p, final List<Path> result)
            throws FileNotFoundException
    {
        if (Files.exists(p))
        {
            if (Files.isRegularFile(p))
            {
                if (Files.isReadable(p))
                {
                    result.add(p);
                    return result.stream();
                } else
                {
                    throw new FileNotFoundException("file unreadable");
                }
            } else
            {
                throw new FileNotFoundException("file is a direcotry");
            }
        } else
        {
            throw new FileNotFoundException("does not exist");
        }
    }
/**
 *
 * @param fileNameP
 * @param result
 * @param parentPath
 * @param p
 * @param depth
 */
    private void processRelativePath(final Path fileNameP,
                                     final List<Path> result,
                                     final Path parentPath,
                                     final Path p,
                                     final int depth)
    {
        final MySimpleFileVisitor msfv = new MySimpleFileVisitor(fileNameP, result);
        final Path parentP = p.toAbsolutePath().normalize().getParent();
        //TODO need to implement ../../file.sql
        try
        {
            Files.walkFileTree(parentP, EnumSet.noneOf(FileVisitOption.class), depth, msfv);

        } catch (IOException ignoreIOException)
        {
        }

        //  final Path userhomeP = Paths.get(System.getProperty("user.home"), "Documents");
        final Path uudf = USER_HOME_P.resolve(fileNameP);
        final Path parentP1 = uudf.toAbsolutePath().normalize().getParent();
        try
        {
            Path junkp = Files.walkFileTree(parentP1, EnumSet.noneOf(FileVisitOption.class), depth, msfv);
        } catch (IOException ignoreIOException)
        {
        }
    }


    /**
     * my file visitor for searching for a file
     */
    private static class MySimpleFileVisitor
            extends SimpleFileVisitor<Path>
    {

        /**
         *
         */
        final Path fn;

        /**
         *
         */
        final List<Path> myresult;

        /**
         *
         * @param fn     a path of the file name
         * @param result a list of previously found paths
         */
        MySimpleFileVisitor(final Path fn, List<Path> result)
        {
            super();
            this.fn = fn;
            this.myresult = result;
        }


        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
                throws IOException
        {
            return super.postVisitDirectory(dir, e);
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException
        {
            Objects.requireNonNull(dir);
            Objects.requireNonNull(attrs);
            if (dir.getFileName().startsWith("."))
            {
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;

        }

        @Override
        public FileVisitResult visitFile(final Path dir, final BasicFileAttributes attrs)
                throws IOException
        {
            Objects.requireNonNull(dir);
            Objects.requireNonNull(attrs);
            if (!attrs.isDirectory() && dir.getFileName().equals(fn))
            {
                myresult.add(dir);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(final Path dir, final IOException e)
                throws IOException
        {
            Objects.requireNonNull(dir);
            if (e instanceof AccessDeniedException)
            {
                return FileVisitResult.SKIP_SUBTREE;
            }

            return super.visitFileFailed(dir, e);
        }

    }
}
