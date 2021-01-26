/*
 * Copyright (c) 2015, Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
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
 */package net.dbcrd.DBCUtilLib;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A singleton that looks for a disk ID on a windows system using the system command "
 * <pre>{@code cmd /c dir C:\\totaljunk}</pre>"
 * <p> </p>
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 2,
        lastModified = "9/27/2016",
        copyright = "(C) 2013-2016 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public enum GetWindowsDriveCSNum
        implements GetSystemInfo {

    /** Singleton Instance */
    INSTANCE;

    /**
     *
     */
    private static final Logger THE_LOGGER = Logger.getLogger(GetWindowsDriveCSNum.class.getName());
    /** cmd to get windows op system information */
    static final private String GET_OPSYS_CMD =
            "cmd /c reg query \"HKLM\\Software\\Microsoft\\Windows NT\\CurrentVersion\" /v \"ProductName\"";
    /** pattern for finding the windows operating system */
    private final static Pattern OPSYS_PAT = Pattern.compile("^.*REG_SZ\\s*([a-z0-9_ ]*)",
            Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
    /** cmd to get the system disk info */
    static final private String GET_SYSDSK_CMD = "cmd /c dir C:\\totaljunk";
    /** true if system disk found */
    static private boolean sysDiskIdFound = false;

    /** true if system information found */
    static private boolean sysIdFound = false;
    /** ID for system disk */
    static private String sysDiskId = "UNKNOWN";
    /** id for system name */
    static private String sysId = "UNKNOWN";

    /**
     *
     */
    static private transient boolean initialized = false;
    /** Pattern to parse the system disk ID */
    static final private Pattern SN_PAT = Pattern.compile(
            "(?<=Volume\\s{1,3}Serial\\s{1,3}Number\\s{1,3}is\\s{1,3})[a-zA-Z0-9_-]+");

    /**
     *
     */
    static  int returnStatus = -100;

     @Override
     public String toString(){
         StringBuilder sb = new StringBuilder(512);
         sb.append(sysId).append(", ").append(sysDiskId);
         return sb.toString();
     }

     /**
      *
      * @return
      */
    @Override
    public int getReturnStatus() {
       return returnStatus;
    }

    /**
     *
     * @return a boolean true if the task found the disk
     */
    @Deprecated
    public boolean getSysDiskIdFound() {
        return sysDiskIdFound;
    }

    /**
     *
     * @return a boolean true if the task found the disk
     */
    @Override
    public boolean isSysDiskIdFound() {
        if (!initialized) {
            try {
                main(new String[1]);
            } catch (IOException ex) {
                THE_LOGGER.log(Level.SEVERE, "Lazy initialization filed", ex);
                return false;
            }
        }
        return sysDiskIdFound;
    }

    /**
     *
     * @return a boolean true if the task found the system info
     */
    @Override
    public boolean isSysIdFound() {
        if (!initialized) {
            try {
                main(new String[1]);
            } catch (IOException ex) {
                THE_LOGGER.log(Level.SEVERE, "Lazy initialization filed", ex);
                return false;
            }
        }
        return sysIdFound;
    }

    /**
     *
     * @return a String that is the disk id or UNKNOWN if no disk was found
     */
    @Override
    public String getSysDiskId() {
        if (!initialized) {
            try {
                main(new String[1]);
            } catch (IOException ex) {
                THE_LOGGER.log(Level.SEVERE, "Lazy initialization filed", ex);
                return "NOT INITIALIZED";
            }
        }
        return sysDiskId;
    }
/**
 *
 * @return
 */
    @Override
    public String getSysId() {
        if (!initialized) {
            try {
                main(new String[1]);
            } catch (IOException ex) {
                THE_LOGGER.log(Level.SEVERE, "Lazy initialization filed", ex);
                return "NOT INITIALIZED";
            }
        }
        return sysId;
    }

    /**
     *
     * @param args a String[] which is totally ignored
     *
     * @throws IOException if ...
     */
    public static void main(final String[] args)
            throws IOException {


        returnStatus = -100;
        sysDiskIdFound = false;
        try {
            OSProcess osp = new OSProcess(Arrays.asList("cmd", "/c", "dir", "C:\\totaljuink"), 5);
            osp.call();

            sysDiskIdFound = true;

            final StringBuilder cmdResults = new StringBuilder(1000);

            if (sysDiskIdFound) {
                osp.getOutputList().forEach((s) -> cmdResults.append(s).append('\n'));
                final int idx = cmdResults.indexOf("Volume in drive C ");
                sysDiskIdFound = (idx != -1);
                final Matcher mx = SN_PAT.matcher(cmdResults);
                sysDiskIdFound = sysDiskIdFound && mx.find();
                sysDiskId = mx.group(0);
            }

            osp.command(Arrays.asList("cmd", "/c", "reg", "query",
                    "\"HKLM\\Software\\Microsoft\\Windows NT\\CurrentVersion\"", "/v", "\"ProductName\""));
            osp.call();
            sysIdFound = true;

            cmdResults.delete(0, cmdResults.length());
            osp.getOutputList().forEach((s) -> cmdResults.append(s).append('\n'));
            if (sysIdFound) {
                final Matcher mx = OPSYS_PAT.matcher(cmdResults);
                if (mx.find()) {
                    sysId = mx.group(1);
                }
            }
            initialized = true;
            returnStatus = 0;
         //   System.out.println(GetWindowsDriveCSNum.INSTANCE.toString());
        } catch (InterruptedException ex) {
            sysIdFound = false;
            sysDiskIdFound = false;
            THE_LOGGER.log(Level.SEVERE, null, ex);
            returnStatus = -1;
        }
    }
}
