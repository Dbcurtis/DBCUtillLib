
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * see {@code www.microsoft.com/resources/documentation/windows/xp/all/proddocs/en-us/cmd.mspx?mfr=true}
 * <p>
 */
@Deprecated
public class DosCmd // the call is returning a string with new lines, the old version did not have the new Lines
        implements Callable<String> {

    /** The Logger*/
    private static final Logger THE_LOGGER = Logger.getLogger(DosCmd.class.getName());

    /** File to be executed by a cmd /c command  */
    private final File file;
    /** allows caller to adjust the timeout for the opsys process */
    private int timeout = 5;

    /**
     *
     * @param file
     */
    public DosCmd(final File file) {
        super();
        this.file = file;
    }


    @Override
    public String call()
            throws IOException, InterruptedException {
        
        final StringBuilder sb = new StringBuilder(5000);
        THE_LOGGER.log(Level.INFO, "executing {0}", file.getAbsolutePath());
        List<String> cmdl = new ArrayList<>(10);
        cmdl.addAll(Arrays.asList("cmd", "/c"));
        cmdl.add("\"" + file.getAbsolutePath() + "\"");
        OSProcess osp = new OSProcess(cmdl, timeout);
        osp.call();
      
        osp.getOutputList().stream().forEach((s)->sb.append(s).append('\n'));
        return sb.toString();
    }
    /**
     * 
     * @param to an int that is the timeout value, if set to zero (I THINK) to is infinite
     */
    public void setTimeOut(final int to) {
        timeout = to;
    }
}
