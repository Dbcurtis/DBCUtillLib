/*
 * Copyright (C) 2017 dbcurtis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

/**
 * <strong> Class to setup the loggers.</strong>
 * Probably has some multi thread problems
 *
 * @author dbcurtis
 */
@ClassPreamble(
    author = "Daniel B. Curtis",
    date = "Aug 2010 ",
    currentRevision = 3,
    lastModified = "07/10/2017",
    lastModifiedBy = "Daniel B. Curtis"
)
public enum LoggerSetup
{

    /** TBD */
    INSTANCE;

    /** TBD */
    private static final int NET = 0;

    /** TBD */
    private static final int DBCRD = 1;

    /** TBD */
    private static final String JAVA = "Java";

    /** TBD */
    private static final String LOGS = "Logs";

    /** TBD */
    private static final String DEF_LOG_DIR = JAVA + "/" + LOGS;

    /** a List of Loggers NET and DBCRD (which is net.dbcrd) */
    static final List<Logger> ALOGGER_LIST; //warning here because of the public static for SET_LOGGER_LEVEL
    /** Object used for SYNC */
    private static final Object SYNC = new Object();
    /** True if unable to be initialized, false if able to be initialized */
    private static boolean initialized = false;
    /** show initialized */
    private static Runnable Set_Initialized = ()->initialized = true;
    /** mapping of Integer to logger Level */
    private static final Map<Integer, Level> INT2LEVEL = new LinkedHashMap<>(20);

    /** TBD */
    private static final Map<Integer, List<Path>> HANDLER_PATHS = new LinkedHashMap<>(10);
    /** true if the HandlerPaths are to be cleared */
    private static boolean clearHandlerPaths = true;
    /**
     * Lambda Supplier to get the handler paths for net.dbcrd
     * <p>
     * locks, makes a copy of the HANDLER_PATHS for DBCRD, unlocks and streams the copy
     */
    private static final Supplier<Stream<Path>> GET_HANDLER_PATHS = () ->
    {
        final List<Path> tempL;
        synchronized (SYNC)
        {
            tempL = new ArrayList<>(HANDLER_PATHS.get(DBCRD)
                .size());
            tempL.addAll(HANDLER_PATHS.get(DBCRD));
        }
        return tempL.stream();
    };
    /**
     * Lambda BiFunction to get the Data from the logger files
     * <p>
     * count is the number of files to get - newest first.
     * pred is a Predicate to select which files to read
     * <p>
     * returns a List of Stream of String for each returned file. Some of the Streams in the List may be empty
     */
    public static final BiFunction<Integer, Predicate<Path>, List<DataRecord>> GET_LOGGING_DATA = (count, pred) ->
    {
        final int cnt = (count.equals(0)) ? Integer.MAX_VALUE : count;
        final List<Path> dirP = GET_HANDLER_PATHS.get()
            .filter(pred)
            .collect(toList());

        if (dirP.isEmpty())
        {
            return new ArrayList<>(2);
        }
        final List<File> dirFiles = (Arrays.asList(dirP.get(0)
                               .toFile()
                               .listFiles())).stream()
            .filter(f -> !f.toString()
            .endsWith(".lck"))//remove lock file
            .limit(cnt)
            .collect(toList());

        if (dirFiles.isEmpty())
        {
            return new ArrayList<>(2);
        }

        if (dirFiles.size() > 1)
        {
            dirFiles.sort((File a, File b) -> // sort by modification date

            {
                long am = a.lastModified();
                long bm = b.lastModified();
                return am > bm ? -1 : am < bm ? +1 : 0;

                });
        }

        final List<DataRecord> resultL = new ArrayList<>(dirFiles.size());
        dirFiles.stream()
            .map((f) -> f.toPath())
            .forEachOrdered((p) ->
            {
                Stream<String> result = Stream.empty();  //notice can store empty streams
                try
                {
                    result = new FileUtils().readPathToLineStream(p);
                } catch (FileNotFoundException ignore)
                {

                }

                resultL.add(new DataRecord(p, result));
            });
        return resultL;
    };

    /* initialization */
    static
    {
        HANDLER_PATHS.putIfAbsent(NET, new ArrayList<>(10));
        HANDLER_PATHS.putIfAbsent(DBCRD, new ArrayList<>(10));

        Arrays.asList(Level.OFF, Level.SEVERE,
                      Level.WARNING, Level.INFO,
                      Level.CONFIG, Level.FINE,
                      Level.FINER, Level.FINEST,
                      Level.ALL)
            .stream()  // initialize INT2LEVEL
            .forEach(lev -> INT2LEVEL.put(lev.intValue(), lev));

        /* initialize the logger list */
        ALOGGER_LIST = new ArrayList<>(10);
        ALOGGER_LIST.add(Logger.getLogger("net"));
        ALOGGER_LIST.add(Logger.getLogger("net.dbcrd"));

        /*
         * add a ConsoleHandler to DBCRD at level Warning, and disable passing messages up to NET
         * Set the level for the DBCRD logger to All --- it will be changed to that of the handler of the lowest level
         *
         */
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.WARNING);
        ALOGGER_LIST.get(DBCRD)
            .addHandler(ch);
        ALOGGER_LIST.get(DBCRD)
            .setUseParentHandlers(false);
        ALOGGER_LIST.get(DBCRD)
            .setLevel(Level.ALL);
    }

    /** THE LOGGER for this class */
    private static final Logger THE_LOGGER = Logger.getLogger(LoggerSetup.class.getName());

    /**
     * When done logging, run this lambda to close all FileHandlers on NET and DBCRD and remove them from those loggers
     * sets clearHandlerPaths to true for future reset
     * sets initialized to false to allow a net setup.
     */
    public static final Runnable CLOSE_FILE_HANDLERS = () ->
    {
        synchronized (SYNC)
        {
            ALOGGER_LIST.stream() //TODO should this be limited to DBCRD?
                .forEach(logger ->
                {
                    Arrays.asList(logger.getHandlers())
                        .stream()
                        .filter(h -> h instanceof FileHandler) // closing and removing FileHandlers only
                        .forEach(h ->
                        {
                            h.close();
                            logger.removeHandler(h);
                        });
                });
            ALOGGER_LIST.get(DBCRD)
                .setUseParentHandlers(true);
            ALOGGER_LIST.get(DBCRD)
                .setLevel(Level.ALL);
            clearHandlerPaths = true;
            initialized = false;
        }

    };

    /**
     * returns true if the handlers contains a file handler
     */
    private static final Predicate<Logger> HAS_FILEHANDLER = lg ->
    {
        final int num = Arrays.asList(lg.getHandlers())
            .stream()
            .filter(h -> h instanceof FileHandler) // closing and removing FileHandlers only
            .map(h -> 1)
            .mapToInt(Integer::intValue)
            .sum();
        return num > 0;
    };

    /**
     *
     */
    private final static Runnable SET_MIN_LEVEL = () ->
    {
        List<Handler> handlers;

        //find the minimum logger level of DBCRD's attached handlers
        synchronized (SYNC)
        {
            handlers = Arrays.asList(ALOGGER_LIST.get(DBCRD)
                .getHandlers());
        }
        int minHandlerLevel = handlers.stream()
            .map(h -> h.getLevel()
            .intValue())
            .collect(Collectors.reducing(Integer::min))
            .get();

        ALOGGER_LIST.get(DBCRD)
            .setLevel(INT2LEVEL.get(minHandlerLevel));
    };

     /**
     * A Consumer that accepts a string specifying the classificatoin of the tyhpes of FCC alerts to display
     *
     *
     * <tt>net.dbcrd</tt> Logger.
     */
    public static final Consumer<Level> SET_FCC_ALERT_PRAM = ( pram->
    {


    });



    /**
     * A Consumer that accepts a Logger.Level and applies it to the ConsoleHandler of <tt>net.dbcrd</tt> Logger.
     * It also makes sure that the logger will accept error levels that are the minimum of level of all the handlers on the
     * <tt>net.dbcrd</tt> Logger.
     */
    public static final Consumer<Level> SET_LOGGER_LEVEL = (lev ->
    {
        ALOGGER_LIST.get(DBCRD)//TODO why are you doing this?
            .setLevel(lev);

        final List<Handler> handlers;
        synchronized (SYNC)
        {
            handlers = Arrays.asList(ALOGGER_LIST.get(DBCRD)
                .getHandlers());
        }

        handlers.stream()
            .filter(h -> h instanceof ConsoleHandler)
            .forEach(h ->
            {
                h.setLevel(lev);
            });

        SET_MIN_LEVEL.run();

//        //find the minimum logger level of DBCRD's attached handlers
//        synchronized (SYNC)
//        {
//            handlers = Arrays.asList(ALOGGER_LIST.get(DBCRD)
//                .getHandlers());
//        }
//        int minHandlerLevel = handlers.stream()
//            .map(h -> h.getLevel()
//            .intValue())
//            .collect(Collectors.reducing(Integer::min))
//            .get();
//
//        ALOGGER_LIST.get(DBCRD)
//            .setLevel(INT2LEVEL.get(minHandlerLevel));
    });

    /**
     * A Consumer that accepts a Logger.Level and applies it to the FileHandlers of <tt>net.dbcrd</tt> Logger.
     * It also makes sure that the logger will accept error levels that are the minimum of level of all the handlers on the
     * <tt>net.dbcrd</tt> Logger.
     */
    public static final Consumer<Level> SET_RLOGGER_LEVEL = (lev ->
    {

//        ALOGGER_LIST.get(DBCRD)//TODO why are you doing this?
//            .setLevel(lev);
        final List<Handler> handlers;
        synchronized (SYNC)
        {
            handlers = Arrays.asList(ALOGGER_LIST.get(DBCRD)
                .getHandlers());
        }

        handlers.stream()
            .filter(h -> h instanceof FileHandler)
            .forEach(h ->
            {
                h.setLevel(lev);
            });

        SET_MIN_LEVEL.run();
//        //find the minimum logger level of DBCRD's attached handlers
//        synchronized (SYNC)
//        {
//            handlers = Arrays.asList(ALOGGER_LIST.get(DBCRD)
//                .getHandlers());
//        }
//        int minHandlerLevel = handlers.stream()
//            .map(h -> h.getLevel()
//            .intValue())
//            .collect(Collectors.reducing(Integer::min))
//            .get();
//
//        ALOGGER_LIST.get(DBCRD)
//            .setLevel(INT2LEVEL.get(minHandlerLevel));
    });

    /**
     * <strong>Sets up the logger system.</strong>
     * <p>
     * Only effective the first time it is called without error, subsequent calls are ignored until after CLOSE_FILE_HANDLERS
     * is invoked.
     * <p>
     * @param xmlLevIn a Level to set the logger level for the xml handler
     * @param fhin     an vararg array of FileHandler, if exits and length greater thzn 0, applies the file handlers to net.dbcrd.
     *                 If not present, uses FileHandler("Log%u.%g.xml", 1000000, 3, false).
     *                 If fhin is specified, the FileHandler level is replaced by xmlLev.
     *
     * @return a boolean true if the logger became initialized or was initialized, false if the logger was unable to be
     *         initialized
     */
    public boolean setup(final Level xmlLevIn, final FileHandler... fhin)
    {
        final Level xmlLev = Objects.requireNonNull(xmlLevIn);
        synchronized (SYNC)
        {
            if (initialized || HAS_FILEHANDLER.test(ALOGGER_LIST.get(DBCRD)))
            {
                return false;
            }
            try
            {
                final String uHome = System.getProperty("user.home");
                final List<FileHandler> fhl = new ArrayList<>(10);
                if (fhin.length > 0) // user provided FileHandler(s)
                {
                    fhl.addAll(Arrays.asList(fhin));
                }
                else
                { // default logger is .../Java/Logs
                    final String fileHandlerPattern = getFileHandlerString("%h/Log%u.%g.xml");
                    Files.createDirectories(Paths.get(uHome, JAVA, LOGS));
                    fhl.add(new FileHandler(fileHandlerPattern, 100000, 3, false));//%h/java%u.log

                }
                fhl.stream()
                    .forEach(h ->
                    {
                        h.setLevel(xmlLev);
                        ALOGGER_LIST.get(DBCRD)
                            .addHandler(h);

                    });

                SET_LOGGER_LEVEL.accept(Level.WARNING); // sets the consule handlers to WARNING
               // setInitialized();
                Set_Initialized.run();
            } catch (IOException | SecurityException ex)
            {
                THE_LOGGER.log(Level.SEVERE, null, ex);
                return false;
            }

        }
        return initialized;
    }

    /**
     *
     * if clearHandlerPaths is true it does so.
     * if the Handler pattern starts with %h will generate the logger directory,
     * and reformats the handler pattern as follows:
     *
     * @param fhDir a String that is the file handler pattern
     *
     * @return a String that TBD
     *
     * @throws java.io.IOException if as expected
     */
    public static String getFileHandlerString(final String fhDir)
        throws IOException
    {
        synchronized (SYNC)
        {
            if (clearHandlerPaths)
            {
                HANDLER_PATHS.entrySet()
                    .stream()
                    .forEach(e -> e.getValue()
                    .clear()); //reset left over after close
                clearHandlerPaths = false;
            }
        }
        final List<String> resultl = new ArrayList<>(5);
        resultl.add(fhDir);
        if (fhDir.trim() // only look at %h patterns
            .startsWith("%h"))
        {
            processPercentHPattern(fhDir, resultl);
        }
        return resultl.get(0);
    }

    /**
     *
     * @param fhDir
     * @param resultl
     */
    static void processPercentHPattern(final String fhDir, final List<String> resultl)
    {
        final String uHome = System.getProperty("user.home");
        final List<String> slashList = new ArrayList<>(10); // a list of slash seperated data
        slashList.addAll(Arrays.asList(fhDir.trim()
            .split("/")));

        Optional<Path> oDir = Optional.empty();
        while (true)
        {
            try
            {
                if (slashList.size() == 2) // if %h/name store as Java/Logs/name
                {

                    slashList.add(1, LOGS);//generates Java/Logs
                    slashList.add(1, JAVA);
                    oDir = Optional.of(Files.createDirectories(Paths.get(uHome, DEF_LOG_DIR)));
                    resultl.set(0, String.join("/", slashList));
                    break;

                }
                if (!JAVA.equals(slashList.get(1)))
                {
                    slashList.add(1, JAVA);
                }
                if (slashList.size() > 3)
                {
                    if (LOGS.equals(slashList.get(2)))
                    {
                        oDir = Optional.of(Files.createDirectories(Paths.get(uHome, DEF_LOG_DIR)));
                        resultl.set(0, String.join("/", slashList));
                        break;

                    }
                    else if (LOGS.equals(slashList.get(3)))
                    {
                        oDir = Optional.of(
                            Files.createDirectories(
                                Paths.get(
                                    uHome,slashList.get(1), slashList.get(2), slashList.get(3))
                            )
                        );

                        resultl.set(0, String.join("/", slashList));
                        break;
                    }
                    else
                    {
                        slashList.add(3, LOGS);
                        final List<String> kdkd = slashList.subList(1, slashList.size() - 1);
                        //String[] args = kdkd.toArray(new String[kdkd.size()]);
                        oDir = Optional.of(Files.createDirectories(
                            Paths.get(
                                uHome,
                                kdkd.toArray(new String[kdkd.size()]))
                        ));
                        resultl.set(0, String.join("/", slashList));
                        break;
                    }
                }
            } catch (IOException ex)
            {
                THE_LOGGER.log(Level.SEVERE, null, ex);
                resultl.set(0, fhDir);
                oDir = Optional.empty();
            }
        }
        oDir.map(dir ->
        {
            assert Files.exists(dir)
                && Files.isDirectory(dir)
                && Files.isReadable(dir)
                && Files.isWritable(dir);
            HANDLER_PATHS.get(DBCRD)
                .add(dir);
            return true;
        });

    }

    /**
     *
     * @param args
     */
    public static void main(final String[] args)
    {
        final String uHome = System.getProperty("user.home");
        try
        {
            CLOSE_FILE_HANDLERS.run();
            String result = LoggerSetup.getFileHandlerString("%h/unitTestjava%g.log");
            if (!"%h/Java/Logs/unitTestjava%g.log".equals(result))
            {
                THE_LOGGER.log(Level.SEVERE, "incorrect: {0}", result);
            }

            result = LoggerSetup.getFileHandlerString("%t/unitTestjava%g.log");
            if (!"%t/unitTestjava%g.log".equals(result))
            {
                THE_LOGGER.log(Level.SEVERE, "incorrect: {0}", result);
            }

            result = LoggerSetup.getFileHandlerString("%h/Java/Logs/unitTestjava%g.log");
            if (!"%h/Java/Logs/unitTestjava%g.log".equals(result))
            {
                THE_LOGGER.log(Level.SEVERE, "incorrect: {0}", result);
            }

            result = LoggerSetup.getFileHandlerString("%h/Java/deleteme0/Logs/unitTestjava%g.log");
            if (!"%h/Java/deleteme0/Logs/unitTestjava%g.log".equals(result))
            {
                THE_LOGGER.log(Level.SEVERE, "incorrect: {0}", result);
            }
            MyFiles.INSTANCE.deleteDir(Paths.get(uHome, "Java/deleteme0")
                .toFile());
            //-------

            result = LoggerSetup.getFileHandlerString("%h/Java/deleteme0/unitTestjava%g.log");
            if (!"%h/Java/deleteme0/Logs/unitTestjava%g.log".equals(result))
            {
                THE_LOGGER.log(Level.SEVERE, "incorrect: {0}", result);
            }
            MyFiles.INSTANCE.deleteDir(Paths.get(uHome, "Java/deleteme0")
                .toFile());
            //-------

            result = LoggerSetup.getFileHandlerString("%h/deleteme0/unitTestjava%g.log");
            if (!"%h/Java/deleteme0/Logs/unitTestjava%g.log".equals(result))
            {
                THE_LOGGER.log(Level.SEVERE, "incorrect: {0}", result);
            }
            MyFiles.INSTANCE.deleteDir(Paths.get(uHome, "Java/deleteme0")
                .toFile());
            //-------

            result = LoggerSetup.getFileHandlerString("%h/deleteme0/deleteme1/a/b/c/unitTestjava%g.log");
            if (!"%h/Java/deleteme0/Logs/deleteme1/a/b/c/unitTestjava%g.log".equals(result))
            {
                THE_LOGGER.log(Level.SEVERE, "incorrect: {0}", result);
            }

            MyFiles.INSTANCE.deleteDir(Paths.get(uHome, "Java/deleteme0")
                .toFile());
            //-------
        } catch (IOException ex)
        {
            THE_LOGGER.log(Level.SEVERE, null, ex);
        }
        CLOSE_FILE_HANDLERS.run();
    }

    /**
     * TBD
     */
    @SuppressWarnings("PublicInnerClass")
    public static class DataRecord
    {

        /** TBD */
        public final Stream<String> data;

        /** TBD */
        public final Path p;

        /**
         *
         * @param pIn
         * @param dataIn
         */
        DataRecord(final Path pIn, final Stream<String> dataIn)
        {
            p = Objects.requireNonNull(pIn);
            data = Objects.requireNonNull(dataIn);

        }

        /**
         * @return the data
         */
        public Stream<String> getData()
        {
            return data;
        }

        /**
         * @return the p
         */
        public Path getP()
        {
            return p;
        }
    }
}
