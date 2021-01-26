/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dbcurtis
 */
public class LoggerSetupTest
{
    
    private static final Logger LOG = Logger.getLogger(LoggerSetupTest.class.getName());

    /**
     * delete the log files
     */
    private static  void aresetlog(){
      Path p = Paths.get(System.getProperty("user.home"),"Java","UNITTEST");
           MyFiles.INSTANCE.deleteDir(p.toFile());
}
    @BeforeClass
    public static void setUpClass()
    {
          aresetlog();
    }
    
    @AfterClass
    public static void tearDownClass()
    {
             aresetlog();
    }
    public LoggerSetupTest()
    {
    }
    
    @Before
    public void setUp()
    {
          LoggerSetup.CLOSE_FILE_HANDLERS.run();
    }
    
    @After
    public void tearDown()
    {
         LoggerSetup.CLOSE_FILE_HANDLERS.run();
    }
    /**
     * Test of setup method, of class LoggerSetup.
     */
    @Test
    public void testSetup()
    {
        try
        {
            System.out.println("* LoggerSetup: setup");
            Level lev = Level.FINE;           
     
            LoggerSetup instance = LoggerSetup.INSTANCE;
           // boolean expResult = false;
                    
            assertTrue(instance.setup(lev));
            assertFalse(instance.setup(lev));           
            String ets = Long.toString(System.currentTimeMillis());
           
            LOG.fine(F1 +" lt="+ets);
            LoggerSetup.CLOSE_FILE_HANDLERS.run();
            List<LoggerSetup.DataRecord> streamsL = LoggerSetup.GET_LOGGING_DATA.apply(1,(Path p)->p.toString().contains("Log"));
            assertTrue(streamsL.size()==1);
            streamsL = LoggerSetup.GET_LOGGING_DATA.apply(0,(Path p)->p.toString().contains("Log"));
            assertTrue(streamsL.size()<=3&&!streamsL.isEmpty());
            StringBuilder sb = new StringBuilder(20000);
            streamsL.get(0).data.forEachOrdered(l-> sb.append(l).append('\n'));
            assertTrue(sb.indexOf(F1)>0);
            assertTrue(sb.indexOf("</log>")>0);
            assertTrue(sb.indexOf(ets)>0);
            assertTrue(instance.setup(lev));
            LoggerSetup.CLOSE_FILE_HANDLERS.run();
            
            File df = new File(System.getProperty("user.home")+"/Java/UNITTEST");
          //  boolean dhd = df.exists()&&df.canRead()&&df.isDirectory();
            MyFiles.INSTANCE.deleteDir(df);
            assertFalse(df.exists()&&df.canRead()&&df.isDirectory());
            
            String fileHandlerName =  LoggerSetup.getFileHandlerString("%h/UNITTEST/LoggerSetupLog%u.%g.xml");
            assertTrue(df.exists()&&df.canRead()&&df.isDirectory());
            
            assertTrue (instance.setup(lev, new FileHandler(fileHandlerName, 10000, 2)));
            ets = Long.toString(System.currentTimeMillis());
            LOG.fine(F1 +" lt="+ets);
            streamsL = LoggerSetup.GET_LOGGING_DATA.apply(0,(Path p)->p.toString().contains("UNITTEST"));
            sb.delete(0,sb.length());
            streamsL.get(0).data.forEachOrdered(l-> sb.append(l).append('\n'));
            
            assertTrue(sb.indexOf(F1)>0);
            assertTrue(sb.indexOf("</log>")<0);
            assertTrue(sb.indexOf(ets)>0);
            assertFalse( instance.setup(lev, new FileHandler(fileHandlerName, 10000, 2)));
            LoggerSetup.CLOSE_FILE_HANDLERS.run();
            streamsL = LoggerSetup.GET_LOGGING_DATA.apply(0,(Path p)->p.toString().contains("UNITTEST"));
            sb.delete(0,sb.length());
            streamsL.get(0).data.forEachOrdered(l-> sb.append(l).append('\n'));            
            assertTrue(sb.indexOf(F1)>0);
            assertTrue(sb.indexOf("</log>")>0);
            assertTrue(sb.indexOf(ets)>0);
            
            LoggerSetup.CLOSE_FILE_HANDLERS.run();
            
        } catch (IOException ex)
        {
            Logger.getLogger(LoggerSetupTest.class.getName())
                .log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
    }

  
    /**
     * Test of valueOf method, of class LoggerSetup.
     */
    @Test
    public void testValueOf()
    {
        System.out.println("* LoggerSetup: valueOf ignored");

    }
    /**
     * Test of values method, of class LoggerSetup.
     */
    @Test
    public void testValues()
    {
        System.out.println("* LoggerSetup: values ignored");
    
    }
   /**
     * Test of getFileHandlerString method, of class LoggerSetup.
     */
    final static String S1 = "severe 1";
    final static String W1 = "warning 1";
    final static String I1 = "info 1";
    final static String I2 = "info 2";
    final static String C1 = "config 1";
    final static String F1 = "fine 1";
            
    @Test
    public void testCloseFileHandlers(){
        try
        {
            System.out.println("* LoggerSetup: testCloseFileHandlers");
              LoggerSetup.CLOSE_FILE_HANDLERS.run();
                LoggerSetup.CLOSE_FILE_HANDLERS.run();
            String fileHandlerName =  LoggerSetup.getFileHandlerString("%h/UNITTEST/LoggerSetupLog%u.%g.xml");
            // Files.createDirectories(Paths.get(System.getProperty("user.home"),"Java","UpdateFCCDB","Logs"));
            LoggerSetup.INSTANCE.setup(Level.INFO, new FileHandler(fileHandlerName, 10000, 2));
            LOG.severe(S1);
            LOG.warning(W1);
            LOG.info(I1);
            LOG.config(C1);
            LOG.fine(F1);

            StringBuilder sb = new StringBuilder(10000);
            List<LoggerSetup.DataRecord> streamsL = LoggerSetup.GET_LOGGING_DATA.apply(0, (Path p) -> p.toString().contains("UNITTEST"));
            LOG.info(I2);

            assertTrue(streamsL.size() < 4);
            List<String> data = streamsL.get(0).data.collect(toList());
            streamsL.stream().map(dr->dr.data).forEach(Stream::close);
            data.stream().forEachOrdered(l -> sb.append(l).append('\n'));
            assertTrue(sb.indexOf(S1) > 0);
            assertTrue(sb.indexOf(W1) > 0);
            assertTrue(sb.indexOf(I1) > 0);
            assertTrue(sb.indexOf(I2) < 0);
            assertTrue(sb.indexOf(C1) < 0);
            assertTrue(sb.indexOf(F1) < 0);
            assertTrue(sb.indexOf("</log>") < 0);
            sb.delete(0, sb.length());
            LoggerSetup.CLOSE_FILE_HANDLERS.run();
            streamsL= LoggerSetup
                   .GET_LOGGING_DATA.apply(0,(Path p)->p.toString().contains("UNITTEST"));
            assertTrue(streamsL.size() < 4);
            streamsL.get(0).data.forEachOrdered(l->sb.append(l).append('\n'));
            assertTrue(sb.indexOf(S1) > 0);
            assertTrue(sb.indexOf(W1) > 0);
            assertTrue(sb.indexOf(I1) > 0);
            assertTrue(sb.indexOf(I2) > 0);
            assertTrue(sb.indexOf(C1) < 0);
            assertTrue(sb.indexOf(F1) < 0);
            assertTrue(sb.indexOf("</log>") > 0);
             
  
        } catch (IOException ex)
        {
           fail("Unexpected exception");
        }
        
    }
    /**
     * Test of getFileHandlerString method, of class LoggerSetup.
     */
    @Test
    public void testGetFileHandlerString()
           
    {
        System.out.println("* LoggerSetup: getFileHandlerString");
           try
        {
            String result = LoggerSetup.getFileHandlerString("%h/unitTestjava%g.log");
            assertEquals ("%h/Java/Logs/unitTestjava%g.log",result);
         
           
            
            result = LoggerSetup.getFileHandlerString("%t/unitTestjava%g.log");
            assertEquals ("%t/unitTestjava%g.log",result);
           
            
            result = LoggerSetup.getFileHandlerString("%h/Java/Logs/unitTestjava%g.log");
            assertEquals("%h/Java/Logs/unitTestjava%g.log",(result));
          
                     
            result = LoggerSetup.getFileHandlerString("%h/Java/deleteme0/Logs/unitTestjava%g.log");
            assertEquals("%h/Java/deleteme0/Logs/unitTestjava%g.log",result);         
            MyFiles.INSTANCE.deleteDir(Paths.get(System.getProperty("user.home"),"Java/deleteme0").toFile());
            //-------
            
            result = LoggerSetup.getFileHandlerString("%h/Java/deleteme0/unitTestjava%g.log");
            assertEquals("%h/Java/deleteme0/Logs/unitTestjava%g.log",result);
        
               MyFiles.INSTANCE.deleteDir(Paths.get(System.getProperty("user.home"),"Java/deleteme0").toFile());
            //-------
            
            result = LoggerSetup.getFileHandlerString("%h/deleteme0/unitTestjava%g.log");
            assertEquals("%h/Java/deleteme0/Logs/unitTestjava%g.log",result);
            MyFiles.INSTANCE.deleteDir(Paths.get(System.getProperty("user.home"),"Java/deleteme0").toFile());
            //-------
            
            result = LoggerSetup.getFileHandlerString("%h/deleteme0/deleteme1/a/b/c/unitTestjava%g.log");
            assertEquals("%h/Java/deleteme0/Logs/deleteme1/a/b/c/unitTestjava%g.log",result);
            {
                LOG.log(Level.SEVERE, "incorrect: {0}", result);
            }
            
            MyFiles.INSTANCE.deleteDir(Paths.get(System.getProperty("user.home"),"Java/deleteme0").toFile());
            //-------
        } catch (IOException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
             fail("unexpected exception");
        }
  
    }


    
}
