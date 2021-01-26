/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
public class OSProcessTest {

    private static final Logger LOG = Logger.getLogger(OSProcessTest.class.getName());
    List<String> cmdlist = Arrays.asList("cmd", "/c", "help");

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    public OSProcessTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doit method, of class OSProcess.
     */
    @Test
    @Ignore
    public void testCall() {
        try {
            System.out.println("call");
            OSProcess instance = new OSProcess();
            ProcessBuilder expResult = null;
            ProcessBuilder result = instance.call();
            assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
            fail("The test case is a prototype.");
        } catch (IOException | InterruptedException ex) {
            fail("unexpected exception");
        }
    }

    /**
     * Test of command method, of class OSProcess.
     */
    @Test
    public void testCommand_0args() {

        System.out.println("command");
        OSProcess instance = new OSProcess();
        try {
            ProcessBuilder pb = instance.call();

            fail("should have caused an error");
        } catch (IOException ioe) {
            String msg = ioe.getMessage();
            assertTrue(msg.contains("error=87"));
            assertEquals(-100, instance.getReturnStatus());
            assertTrue(instance.getErrorList().isEmpty());
            assertTrue(instance.getOutputList().isEmpty());
            assertEquals(1, instance.command().size());
            assertTrue(instance.command().get(0).isEmpty());

        } catch (InterruptedException ex) {
            fail("unexpected exception");
        }

        instance.command(cmdlist);
        try {
            instance.call();
            assertEquals(3, instance.command().size());
            List<String> el = instance.getErrorList();
            List<String> ol = instance.getOutputList();

            assertTrue(el.isEmpty());
            assertEquals(98, ol.size());
            assertEquals(1, instance.getReturnStatus());

            List<String> expResult = cmdlist;
            List<String> result = instance.command();
            assertEquals(expResult, result);
        } catch (IOException | InterruptedException ex) {
            fail("unexpected exception");

        }
    }

    /**
     * Test of command method, of class OSProcess.
     */
    @Test
    public void testCommand_Array() {
        System.out.println("command_array");
        String[] jj = {"cmd", "/c", "help"};
        OSProcess instance = new OSProcess(jj, 5);

        try {
            instance.call();
            assertEquals(3, instance.command().size());
            List<String> el = instance.getErrorList();
            List<String> ol = instance.getOutputList();

            assertTrue(el.isEmpty());
            assertEquals(98, ol.size());
            assertEquals(1, instance.getReturnStatus());

            List<String> expResult = cmdlist;
            List<String> result = instance.command();
            assertEquals(expResult, result);

        } catch (IOException | InterruptedException ex) {
            fail("unexpected exception");

        }
    }

    /**
     * Test of command method, of class OSProcess.
     */
    @Test
    public void testCommand_List() {
        System.out.println("command_list");
        OSProcess instance = new OSProcess(cmdlist, 5);

        try {
            instance.call();
            assertEquals(3, instance.command().size());
            List<String> el = instance.getErrorList();
            List<String> ol = instance.getOutputList();

            assertTrue(el.isEmpty());
            assertEquals(98, ol.size());
            assertEquals(1, instance.getReturnStatus());

            List<String> expResult = cmdlist;
            List<String> result = instance.command();
            assertEquals(expResult, result);
            try {
                instance.command(Arrays.asList("dir"));
                instance.call();
            } catch (IOException ioe) {
                String msg = ioe.getMessage();
                if (!msg.contains("error=2")) {
                    fail("incorrect return");
                }
            }
            instance.command(Arrays.asList("cmd", "/c", "systeminfo"));
            instance.clear();
            instance.call();
            assertEquals(3, instance.command().size());
            el = instance.getErrorList();
            ol = instance.getOutputList();
            String msg = ol.get(4);
            assertEquals("OS Manufacturer:           Microsoft Corporation", msg);
            assertTrue(el.isEmpty());
          //  assertEquals(47, ol.size());
            assertEquals(0, instance.getReturnStatus());

        } catch (IOException | InterruptedException ex) {
            fail("unexpected exception");

        }
    }

    /**
     * Test of getErrorList method, of class OSProcess.
     */
    @Test
    @Ignore
    public void testGetErrorList() {
        System.out.println("getErrorList");
        OSProcess instance = new OSProcess();
        List<String> expResult = null;
        List<String> result = instance.getErrorList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutputList method, of class OSProcess.
     */
    @Test
    @Ignore
    public void testGetOutputList() {
        System.out.println("getOutputList");
        OSProcess instance = new OSProcess();
        List<String> expResult = null;
        List<String> result = instance.getOutputList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReturnStatus method, of class OSProcess.
     */
    @Test
    @Ignore
    public void testGetReturnStatus() {
        System.out.println("getReturnStatus");
        OSProcess instance = new OSProcess();
        int expResult = 0;
        int result = instance.getReturnStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class OSProcess.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        OSProcess instance = new OSProcess();
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
