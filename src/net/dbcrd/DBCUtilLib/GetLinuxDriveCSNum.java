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
 */
package net.dbcrd.DBCUtilLib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;

/**
 * A singleton that looks for a disk ID on a windows system using the system command "
 * <pre>{@code cmd /c dir C:\\totaljunk}</pre>"
 * <p>
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 1,
        lastModified = "9/18/2016",
        copyright = "(C) 2013-2016 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public enum GetLinuxDriveCSNum
        implements GetSystemInfo {

    /** Singleton Instance */
    INSTANCE;

    /**
     *
     */
    private static final Logger THE_LOGGER = Logger.getLogger(GetLinuxDriveCSNum.class.getName());
    /** cmd to get windows op system information */
    static final private List<String> GET_OPSYS_CMD3 = Arrays.asList(
            "ls", "-1", "-g", "-o", "-Q", "/etc/");

    /**
     *
     */
    static final private List<String> GET_OPSYS_CMD2 = Arrays.asList(
            "cat", "/etc/issue");

    /**
     *
     */
    static final private List<String> GET_OPSYS_CMD1 = Arrays.asList(
            "lsb_release", "-a");

    /** pattern for finding the windows operating system */
    private final static Pattern OPSYS_PAT = Pattern.compile("^.*REG_SZ\\s*([a-z0-9_ ]*)",
            Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
    /** cmd to get the system disk info */
    static final private List<String> GET_SYSDSK_CMD = Arrays.asList("ls", "/dev/disk/by-uuid");
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
            "[0-9a-f]{4,4}-[0-9a-f]{4,4}", Pattern.CASE_INSENSITIVE);

    /** TBD */
    static int returnStatus = -100;

    /**
     *
     */
    static final private Map<String, String> OPSYS_MAP = new ConcurrentSkipListMap<>();

    /**
     *
     * @param osp
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private static Collection<String> processCmd3(OSProcess osp)
            throws IOException, InterruptedException {
        List<String> result = new ArrayList<>(1000);
        List<String> goodList = osp.getOutputList().stream()
                .filter((s) -> !s.startsWith("d"))
                .filter((s) -> s.contains("-release"))
                .map((s) -> {
                    int idxlast = s.lastIndexOf('\"');
                    int idxfirst = s.indexOf('\"');
                    if (s.contains("->")) {
                        idxfirst = s.indexOf('\"', s.indexOf("->") + 1);
                    }

                    final String fn = "/etc/" + s.substring(idxfirst + 1, idxlast);
                    return fn;
                })
                .collect(toList());

        final List<String> cmda = new ArrayList<>(100);
        cmda.add("cat");
        cmda.addAll(goodList);
        osp.command(cmda);
        osp.clear();
        osp.call();
        result.addAll(osp.getOutputList());
        return result;
    }

    @Override
    public int getReturnStatus() {
        return returnStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(512);
        sb.append(sysId).append(", ").append(sysDiskId);
        return sb.toString();
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

        try {
            sysDiskId = "Unknown Unix Disk";
            OSProcess osp = new OSProcess(GET_SYSDSK_CMD, 5);
            osp.call();

            sysDiskIdFound = osp.getReturnStatus() == 0;

            final StringBuilder cmdResults = new StringBuilder(1000);
            if (sysDiskIdFound) {
                osp.getOutputList().forEach((s) -> cmdResults.append(s).append('\n'));
                final Matcher mx = SN_PAT.matcher(cmdResults);
                sysDiskIdFound = mx.find();
                if (sysDiskIdFound) {
                    sysDiskId = mx.group(0);
                }
            }

            List<String> tempOpSysInfo = new ArrayList<>(200);
            cmdResults.delete(0, cmdResults.length());
            osp.clear();
            osp.command(GET_OPSYS_CMD1);
            osp.call();

            tempOpSysInfo.addAll(osp.getOutputList());
            osp.command(GET_OPSYS_CMD2);
            osp.call();
            tempOpSysInfo.addAll(osp.getOutputList());

            osp.clear();
            osp.command(GET_OPSYS_CMD3);
            osp.call();
            tempOpSysInfo.addAll(processCmd3(osp));

            final Set<String> remdups = new ConcurrentSkipListSet<>();
            remdups.addAll(tempOpSysInfo);
            OPSYS_MAP.clear();
            remdups.stream().forEach((s) -> {
                List<String> splits = Arrays.asList(s.split("\\s*=\\s*"));
                if (splits.size() == 2) {
                    OPSYS_MAP.put(splits.get(0), splits.get(1));
                }

            });

            sysIdFound = true;
            if (sysIdFound) {
                if (OPSYS_MAP.containsKey("PRETTY_NAME")) {
                    sysId = OPSYS_MAP.get("PRETTY_NAME");
                } else if (OPSYS_MAP.containsKey("NAME")) {
                    sysId = OPSYS_MAP.get("NAME");
                } else {
                    sysId = "UNKNOWN";
                }
            }
            initialized = true;
            returnStatus = 0;

        } catch (InterruptedException ex) {
            sysIdFound = false;
            sysDiskIdFound = false;
            THE_LOGGER.log(Level.SEVERE, null, ex);
            returnStatus = -1;
        }

    }
}
