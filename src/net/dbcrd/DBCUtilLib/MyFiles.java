package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <strong> Recursive copy of file directory.</strong>
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */

@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 1,
        lastModified = "8/18/2013",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public enum MyFiles
{

    /** TBD */
    INSTANCE;

    /**
     * 
     * @param file 
     */
    public void deleteDir(final File file) {
    File[] contents = file.listFiles();
    if (contents != null) {
        for (File f : contents) {
            deleteDir(f);
        }
    }
    file.delete();
}
    /** TBD */
    private static final Logger THE_LOGGER = Logger.getLogger(MyFiles.class.getName());

    /**
     *
     * @param source  a Path for the file to be copied
     * @param target  a Path for the target file to receive the copy
     * @param prompt  a boolean *** what does this do?
     * @param options a CopyOption vararg
     *
     * @return a FileVisitResult
     *
     * @throws IOException if as expected
     * <hr>
     * <h3> Example invocations:</h3>
     * <h4> Recursive directory copy</h4>
     * <pre>{@code    
     * long bytes = com.yourcompany.nio.Files.copyRecursive(
     *      new java.io.File("<filepath1>").toPath(),
     *      new java.io.File("<filepath2>").toPath(),
     *      java.nio.file.StandardCopyOption.REPLACE_EXISTING,
     *      java.nio.file.StandardCopyOption.COPY_ATTRIBUTES
     *      java.nio.file.LinkOption.NOFOLLOW_LINKS );
     *}</pre>
     * <h4>Copy file</h4>
     * <pre>{@code   
     * long bytes = java.nio.Files.copy(
     *      new java.io.File("<filepath1>").toPath(),
     *      new java.io.File("<filepath2>").toPath(),
     *      java.nio.file.StandardCopyOption.REPLACE_EXISTING,
     *      java.nio.file.StandardCopyOption.COPY_ATTRIBUTES
     *      java.nio.file.LinkOption.NOFOLLOW_LINKS );
     *}</pre>
     * <h4> Move file/dir</h4> 
     * <pre>{@code   
     * long bytes = java.nio.Files.move(
     *      new java.io.File("<filepath1>").toPath(),
     *      new java.io.File("<filepath2>").toPath(),
     *      java.nio.file.StandardCopyOption.ATOMIC_MOVE,
     *      java.nio.file.StandardCopyOption.REPLACE_EXISTING );
     *}</pre>
     *
     * <h4> Recursive directory copy</h4> 
     * <pre>{@code   
     * long bytes = com.yourcompany.nio.Files.copyRecursive(
     *      new java.io.File("<filepath1>").toPath(),
     *      new java.io.File("<filepath2>").toPath(),
     *      java.nio.file.StandardCopyOption.REPLACE_EXISTING,
     *      java.nio.file.StandardCopyOption.COPY_ATTRIBUTES
     *      java.nio.file.LinkOption.NOFOLLOW_LINKS );
     *}</pre>
     */
    public FileVisitResult copyRecursive(Path source, Path target, boolean prompt, CopyOption... options)
            throws IOException
    {

        CopyVisitor copyVisitor = new CopyVisitor(source, target, options);//.copy();

        EnumSet<FileVisitOption> fileVisitOpts;
        if (Arrays.asList(options).contains(LinkOption.NOFOLLOW_LINKS))
        {
            fileVisitOpts = EnumSet.noneOf(FileVisitOption.class);
        } else
        {
            fileVisitOpts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        }
        Files.walkFileTree(source, fileVisitOpts, Integer.MAX_VALUE, copyVisitor);
        return FileVisitResult.CONTINUE;
    }

    /**
     * TBD
     */
    private class CopyVisitor
            implements FileVisitor<Path>
    {

        /** TBD */
        final Path source;

        /** TBD */
        final Path target;

        /** TBD */
        final CopyOption[] options;

        /**
         *
         * @param source  a Path for the souirce
         * @param target  a Path to the target
         * @param options a vararg of CopyOption
         */
        CopyVisitor(Path source, Path target, CopyOption... options)
        {
            this.source = source;
            this.target = target;
            this.options = options;
        }

        /**
         *
         * @param dir a Path
         * @param attrs a BasicFileAttributes
         *
         * @return a FileVisitResult
         */
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        {
            // before visiting entries in a directory we copy the directory
            // (okay if directory already exists).
            final Path newdir = target.resolve(source.relativize(dir));
            try
            {
                Files.copy(dir, newdir, options);
            } catch (FileAlreadyExistsException ignore)
            {
                // ignore
            } catch (IOException x)
            {
                THE_LOGGER.log(Level.WARNING, "Unable to create: " + newdir.getFileName(), x);
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }

        /**
         *
         * @param path a path
         * @param attrs a BasicFileAttributes
         *
         * @return
         */
        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
        {
            Path newfile = target.resolve(source.relativize(path));
            try
            {
                Files.copy(path, newfile, options);
            } catch (IOException x)
            {
                THE_LOGGER.log(Level.WARNING, "Unable to copy: " + source.toString(), x);
            }
            return FileVisitResult.CONTINUE;
        }

        /**
         *
         * @param dir a Path
         * @param exc an IOException
         *
         * @return a FileVisitResult
         */
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc)
        {
            // fix up modification time of directory when done
            if (exc == null && Arrays.asList(options).contains(StandardCopyOption.COPY_ATTRIBUTES))
            {
                Path newdir = target.resolve(source.relativize(dir));
                try
                {
                    FileTime time = Files.getLastModifiedTime(dir);
                    Files.setLastModifiedTime(newdir, time);
                } catch (IOException x)
                {
                    THE_LOGGER.log(Level.WARNING, "Unable to copy all attributes to: " + newdir.toString(), x);
                }
            }
            return FileVisitResult.CONTINUE;
        }

        /**
         *
         * @param path a Path
         * @param exc an IOException
         *
         * @return
         */
        @Override
        public FileVisitResult visitFileFailed(Path path, IOException exc)
        {
            if (exc instanceof FileSystemLoopException)
            {
                THE_LOGGER.log(Level.WARNING, "cycle detected: {0}", path.toString());
            } else
            {
                THE_LOGGER.log(Level.WARNING, "Unable to copy: " + path.toString(), exc);
            }
            return FileVisitResult.CONTINUE;
        }
    }

    /**
     * Filenamefilter per the pattern
     */
    public static class PatternFileFilter
            implements FilenameFilter
    {

        /** TBD */
        final Matcher mx;

        /**
         *
         * @param pat a Pattern to match a filename
         */
        public PatternFileFilter(final Pattern pat)
        {
            super();
            mx = pat.matcher("test");
        }

        @Override
        public boolean accept(File dir, String name)
        {
            return mx.reset(name).matches();
        }

    }
}
