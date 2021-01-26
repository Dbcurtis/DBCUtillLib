/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib.status;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
public class StatusDialogTest {
    
    public StatusDialogTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isRunning method, of class StatusDialog.
     */
    @Test
      @Ignore
    public void testIsRunning() {
        System.out.println("isRunning");
        StatusDialog instance = new StatusDialog();
        boolean expResult = false;
        boolean result = instance.isRunning();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isAborting method, of class StatusDialog.
     */
    @Test
      @Ignore
    public void testIsAborting() {
        System.out.println("isAborting");
        StatusDialog instance = new StatusDialog();
        boolean expResult = false;
        boolean result = instance.isAborting();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

 

    /**
     * Test of dispose method, of class StatusDialog.
     */
    @Test
      @Ignore
    public void testDispose() {
        System.out.println("dispose");
        StatusDialog instance = new StatusDialog();
        instance.dispose();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVisible method, of class StatusDialog.
     */
    @Test
      @Ignore
    public void testSetVisible() {
        System.out.println("setVisible");
        boolean vis = false;
        StatusDialog instance = new StatusDialog();
        instance.setVisible(vis);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class StatusDialog.
     */
    @Test
    @Ignore
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        StatusDialog expResult = null;
        StatusDialog result = StatusDialog.main(args);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
