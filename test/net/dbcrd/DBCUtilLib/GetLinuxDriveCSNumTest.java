/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dbcurtis
 */
public class GetLinuxDriveCSNumTest {
    
    private static final Logger LOG = Logger.getLogger(GetLinuxDriveCSNumTest.class.getName());
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    public GetLinuxDriveCSNumTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    /**
     * Test of getSysDiskId method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testGetSysDiskId() {
        System.out.println("getSysDiskId");
        GetLinuxDriveCSNum instance = null;
        String expResult = "";
        String result = instance.getSysDiskId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of getSysId method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testGetSysId() {
        System.out.println("getSysId");
        GetLinuxDriveCSNum instance = null;
        String expResult = "";
        String result = instance.getSysId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSysDiskIdFound method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testIsSysDiskIdFound() {
        System.out.println("isSysDiskIdFound");
        GetLinuxDriveCSNum instance = null;
        boolean expResult = false;
        boolean result = instance.isSysDiskIdFound();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSysIdFound method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testIsSysIdFound() {
        System.out.println("isSysIdFound");
        GetLinuxDriveCSNum instance = null;
        boolean expResult = false;
        boolean result = instance.isSysIdFound();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of main method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testMain()
            throws Exception {
        System.out.println("main");
        String[] args = null;
        GetLinuxDriveCSNum.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of valueOf method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        GetLinuxDriveCSNum expResult = null;
        GetLinuxDriveCSNum result = GetLinuxDriveCSNum.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of values method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        GetLinuxDriveCSNum[] expResult = null;
        GetLinuxDriveCSNum[] result = GetLinuxDriveCSNum.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReturnStatus method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testGetReturnStatus() {
        System.out.println("getReturnStatus");
        GetLinuxDriveCSNum instance = null;
        int expResult = 0;
        int result = instance.getReturnStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class GetLinuxDriveCSNum.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        GetLinuxDriveCSNum instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
