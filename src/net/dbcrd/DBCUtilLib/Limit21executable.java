package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;

/**
 *
 * <strong>A singleton that detects if the program is already running</strong>
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 1,
        lastModified = "8/18/2013",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public enum Limit21executable
{

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    final static Object MULTIINVOKSYNC = new Object();

    /**
     * <strong> Allows only one copy of the application to run.</strong>
     *
     * @param appName a <code>String</code> that specifies the application name
     * <p>
     *
     * If the application is already executing will display an error dialog and exit.
     * @return 
     *
     * @throws net.dbcrd.DBCUtilLib.MultiInvocationException if there is another instance of the program executing.
     */
    public static FileChannel limitToOneExecutible(final String appName)
            throws MultiInvocationException
    {
        FileChannel channel=null;
        synchronized (MULTIINVOKSYNC)
        {
            final String filename = new StringBuilder(128).append(appName.replaceAll(" ","_")).append("Lock.tmp").toString();
            final File filea = new File(System.getProperty("user.home"), filename);
            try
            {
                try  // do not do the try() construct here.  The stream must stay open until the program ends
                {
                    channel = new RandomAccessFile(filea, "rws").getChannel();
                    if (channel.tryLock() == null)
                    {
                        throw new MultiInvocationException("Program already executing A");
                    }
                                       
                } catch (OverlappingFileLockException | IOException e)
                {
                    throw new MultiInvocationException(e.getMessage());
                }

            } finally
            {
                filea.deleteOnExit();
            }
        }
        return channel;
    }

}
