/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Dan
 */
public class GetWindowsDriveCSNumTest {
    private static final Logger LOG = Logger.getLogger(GetWindowsDriveCSNumTest.class.getName());


    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    public GetWindowsDriveCSNumTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    /**
     * Test of getSysDiskId method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testGetSysDiskId() {
        try {
            System.out.println("getSysDiskId");
            GetWindowsDriveCSNum instance = GetWindowsDriveCSNum.INSTANCE;
            GetWindowsDriveCSNum.main(new String[1]);
            
            String result = instance.getSysDiskId();
            Matcher mx = Pattern.compile("[a-z0-9]{4,4}-[a-z0-9]{4,4}",Pattern.CASE_INSENSITIVE).matcher(result);
            assertTrue(mx.matches());
        } catch (IOException ex) {
            fail("unexpected exception");
        }
        
    }


    /**
     * Test of getSysDiskIdFound method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testGetSysDiskIdFound() {
        try {
            System.out.println("getSysDiskIdFound");
            GetWindowsDriveCSNum instance = GetWindowsDriveCSNum.INSTANCE;
            instance.main(new String[1]);
            boolean expResult = true;
            boolean result = instance.getSysDiskIdFound();
            assertEquals(expResult, result);
            result = instance.isSysDiskIdFound();
            assertEquals(expResult, result);
        
          
        } catch (IOException ex) {
             fail("unexpected exception.");
        }
    }
    /**
     * Test of getSysId method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testGetSysId() {
        try {
            System.out.println("getSysId");
            GetWindowsDriveCSNum instance = GetWindowsDriveCSNum.INSTANCE;
            instance.main(new String[1]);
            String expResult = "";
            String result = instance.getSysId();
            assertTrue( result.contains("Windows"));
         
        } catch (IOException ex) {
            Logger.getLogger(GetWindowsDriveCSNumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Test of isSysDiskIdFound method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testIsSysDiskIdFound() {
        try {
            System.out.println("isSysDiskIdFound");
            GetWindowsDriveCSNum instance = GetWindowsDriveCSNum.INSTANCE;
            instance.main(new String[1]);
            boolean expResult = true;
            boolean result = instance.isSysDiskIdFound();
            assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
         
        } catch (IOException ex) {
            Logger.getLogger(GetWindowsDriveCSNumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of isSysIdFound method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testIsSysIdFound() {
        try {
            System.out.println("isSysIdFound");
            GetWindowsDriveCSNum instance = GetWindowsDriveCSNum.INSTANCE;
            instance.main(new String[1]);
            boolean expResult = true;
            boolean result = instance.isSysIdFound();
            assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
        
        } catch (IOException ex) {
            Logger.getLogger(GetWindowsDriveCSNumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Test of main method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testMain()
            {
        try {
            System.out.println("main");
            String[] args = null;
            GetWindowsDriveCSNum.main(args);
            // TODO review the generated test code and remove the default call to fail.
            
        } catch (IOException ex) {
            Logger.getLogger(GetWindowsDriveCSNumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Test of valueOf method, of class GetWindowsDriveCSNum.
     */
    @Test
    @Ignore
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        GetWindowsDriveCSNum expResult = null;
        GetWindowsDriveCSNum result = GetWindowsDriveCSNum.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of values method, of class GetWindowsDriveCSNum.
     */
    @Test
    @Ignore
    public void testValues() {
        System.out.println("values");
        GetWindowsDriveCSNum[] expResult = null;
        GetWindowsDriveCSNum[] result = GetWindowsDriveCSNum.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        GetWindowsDriveCSNum instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReturnStatus method, of class GetWindowsDriveCSNum.
     */
    @Test
    public void testGetReturnStatus() {
        System.out.println("getReturnStatus");
        GetWindowsDriveCSNum instance = null;
        int expResult = 0;
        int result = instance.getReturnStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
