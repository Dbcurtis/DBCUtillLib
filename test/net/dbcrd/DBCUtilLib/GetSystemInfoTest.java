/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
public class GetSystemInfoTest {
    
    public GetSystemInfoTest() {
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
     * Test of getSysDiskId method, of class GetSystemInfo.
     */
    @Test
    public void testGetSysDiskId() {
        System.out.println("getSysDiskId");
        GetSystemInfo instance = new GetSystemInfoImpl();
        String expResult = "";
        String result = instance.getSysDiskId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSysId method, of class GetSystemInfo.
     */
    @Test
    public void testGetSysId() {
        System.out.println("getSysId");
        GetSystemInfo instance = new GetSystemInfoImpl();
        String expResult = "";
        String result = instance.getSysId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSysDiskIdFound method, of class GetSystemInfo.
     */
    @Test
    public void testIsSysDiskIdFound() {
        System.out.println("isSysDiskIdFound");
        GetSystemInfo instance = new GetSystemInfoImpl();
        boolean expResult = false;
        boolean result = instance.isSysDiskIdFound();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSysIdFound method, of class GetSystemInfo.
     */
    @Test
    public void testIsSysIdFound() {
        System.out.println("isSysIdFound");
        GetSystemInfo instance = new GetSystemInfoImpl();
        boolean expResult = false;
        boolean result = instance.isSysIdFound();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReturnStatus method, of class GetSystemInfo.
     */
    @Test
    public void testGetReturnStatus() {
        System.out.println("getReturnStatus");
        GetSystemInfo instance = new GetSystemInfoImpl();
        int expResult = 0;
        int result = instance.getReturnStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class GetSystemInfoImpl
            implements GetSystemInfo {

        public String getSysDiskId() {
            return "";
        }

        public String getSysId() {
            return "";
        }

        public boolean isSysDiskIdFound() {
            return false;
        }

        public boolean isSysIdFound() {
            return false;
        }

        public int getReturnStatus() {
            return 0;
        }
    }

  
    
}
