package net.dbcrd.DBCUtilLib.status;

import com.vladium.utils.PropertyLoader;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author dbcurtis
 */
public class MyStatusTest {

    static ExecutorService mwp;
    private static final Logger LOG = Logger.getLogger(MyStatusTest.class.getName());
    
    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    
      
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
       
    }

    /**
     *
     */
    public MyStatusTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of setNote method, of class MyStatus.
     */
    @Test
    public void testSetNote() {
        System.out.println("setNote");
        MyStatus status = new MyStatus(true,"","Test Status Worker");
        MyStatus.StatusWorker<Object> jjj =status.new StatusWorker<>();

        //status.initialize(true);
        status.initProgressBarVals(new Point(0, 9), new Point(0, 100));
        int cnt = 10;
        int phase = 0;
        while (cnt-- > 0) {
            status.setPhase(phase++, "phase " + Integer.toString(phase));
            status.resetTask();

            status.setNote(new StringBuilder("my count is: ").append(cnt).toString());
            for (int i = 0; i < 100; i++) {
                status.setTask(i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyStatusTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        String result = status.getText();
        assertTrue(result.contains("my count is: 9"));
        assertTrue(result.contains("my count is: 8"));
        assertTrue(result.contains("my count is: 7"));
        assertTrue(result.contains("my count is: 6"));
        assertTrue(result.contains("my count is: 5"));
        assertTrue(result.contains("my count is: 4"));
        assertTrue(result.contains("my count is: 3"));
        assertTrue(result.contains("my count is: 2"));
        assertTrue(result.contains("my count is: 1"));
        assertTrue(result.contains("my count is: 0"));
        status.dispose();
   
        String aaa = "net.dbcrd.DBCUtilLib.status.testproperties";
        Properties test =  PropertyLoader.loadProperties(aaa);
        MyStatus astatus = new MyStatus(false,aaa,"Test Status Worker");
        MyStatus.StatusWorker<Object> jj =astatus.new StatusWorker<>();
        astatus.setNote("start string");
        astatus.setNote("aaa");
        astatus.setNote("bbb");
        astatus.setNote("ccc");
        astatus.setNote("done");
        String aaaa = astatus.getText();
//        String expected = "<!DOCTYPE html><html><head><title>Test Status Worker</title><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body><h1>Test Status Worker</h1><span style=\"color:#2E2EFE\">The program that generated this listing has (C) 2010-2015 by Daniel B. Curtis</span><br/><span style=\"color:#000000\">Thu Aug 13 21:17:33 PDT 2015: start string</span><br/><span style=\"color:#000000\">Thu Aug 13 21:17:33 PDT 2015: Status initalized</span><br/><span style=\"color:#000000\">Thu Aug 13 21:17:33 PDT 2015: this is aaa</span><br/><span style=\"color:#000000\">Thu Aug 13 21:17:33 PDT 2015: this is bbb</span><br/><span style=\"color:#000000\">Thu Aug 13 21:17:33 PDT 2015: ccc</span><br/>";
        //  assertEquals(expected, aaaa);
        assertTrue(aaaa.contains("this is aaa"));
        assertTrue(aaaa.contains("this is bbb"));
        assertTrue(aaaa.contains("ccc</span>"));
        
    }

    /**
     *
     */
    @Test
    @Ignore
    public void TestAbort() {
        System.out.println("abort");
        MyStatus status = new MyStatus(true,"","title");

        //status.initialize(true);
        //assertFalse(Main.abort);
        status.initProgressBarVals(new Point(0, 9), new Point(0, 100));
        status.setNote("Press Abort!!");
        while (status.isAborting()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                break;
            }
        }

        String result = status.getText();
        assertTrue(result.endsWith("Press Abort!!"));
        status.dispose();
    }

    /**
     * Test of isAborting method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testIsAborting() {
        System.out.println("isAborting");
        boolean expResult = false;
      //  boolean result = MyStatus.isAborting();
        boolean result = false;
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAborting method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testSetAborting() {
        System.out.println("setAborting");
        boolean aborting = false;
        MyStatus instance = null;
        instance.setAborting(aborting);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dispose method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testDispose() {
        System.out.println("dispose");
        MyStatus instance = null;
        instance.dispose();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initProgressBarVals method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testInitProgressBarVals() {
        System.out.println("initProgressBarVals");
        Point phaseBarVal = null;
        Point taskBarVal = null;
        MyStatus instance = null;
        instance.initProgressBarVals(phaseBarVal, taskBarVal);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPhase method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testSetPhase() {
        System.out.println("setPhase");
        int val = 0;
        String phaseStringIn = "";
        MyStatus instance = null;
        instance.setPhase(val, phaseStringIn);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTaskIndeterminate method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testSetTaskIndeterminate() {
        System.out.println("setTaskIndeterminate");
        String message = "";
        MyStatus instance = null;
        instance.setTaskIndeterminate(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTask method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testSetTask() {
        System.out.println("setTask");
        int val = 0;
        MyStatus instance = null;
        instance.setTask(val);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTaskMax method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testSetTaskMax() {
        System.out.println("setTaskMax");
        int val = 0;
        MyStatus instance = null;
        instance.setTaskMax(val);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resetTask method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testResetTask() {
        System.out.println("resetTask");
        MyStatus instance = null;
        instance.resetTask();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getText method, of class MyStatus.
     */
    @Test
     @Ignore
    public void testGetText() {
        System.out.println("getText");
        MyStatus instance = null;
        String expResult = "";
        String result = instance.getText();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }



    /**
     * Test of initialize method, of class MyStatus.
     */
  
    @Test
    @Ignore
    public void testInitialize() {
        System.out.println("initialize");
        boolean headful = false;
        String propertiesFileID = "";
        String title = "testInitialize headful false, no properties file";
        MyStatus instance = new MyStatus();
        String initialText = instance.getText();
        
        
        //instance.initialize(headful);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shutdown method, of class MyStatus.
     */
    @Test
    public void testShutdown() {
        System.out.println("shutdown");
        MyStatus.shutdown();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of debuggingGetBlockingQCopy method, of class MyStatus.
     */
    @Test
    public void testDebuggingGetBlockingQCopy() {
        System.out.println("debuggingGetBlockingQCopy");
        MyStatus instance = new MyStatus();
        List<String> expResult = null;
        List<String> result = instance.debuggingGetBlockingQCopy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of debuggingGetBlockingQDrain method, of class MyStatus.
     */
    @Test
    public void testDebuggingGetBlockingQDrain() {
        System.out.println("debuggingGetBlockingQDrain");
        MyStatus instance = new MyStatus();
        List<String> expResult = null;
        List<String> result = instance.debuggingGetBlockingQDrain();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNote method, of class MyStatus.
     */
    @Test
    public void testSetNote_String() {
        System.out.println("setNote");
        String notein = "";
        MyStatus instance = new MyStatus();
        instance.setNote(notein);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNote method, of class MyStatus.
     */
    @Test
    public void testSetNote_String_Color() {
        System.out.println("setNote");
        String notein = "";
        Color color = null;
        MyStatus instance = new MyStatus();
        instance.setNote(notein, color);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBlockingQueue method, of class MyStatus.
     */
    @Test
    public void testGetBlockingQueue() {
        System.out.println("getBlockingQueue");
        MyStatus instance = new MyStatus();
        BlockingQueue<String> expResult = null;
        BlockingQueue<String> result = instance.getBlockingQueue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
