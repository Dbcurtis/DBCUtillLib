/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dan
 */
public class CheckInternetExistsTest {

    public CheckInternetExistsTest() {
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
     * Test of values method, of class CheckInternetExists.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        CheckInternetExists[] expResult = null;
        CheckInternetExists[] result = CheckInternetExists.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class CheckInternetExists.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        CheckInternetExists expResult = null;
        CheckInternetExists result = CheckInternetExists.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of call method, of class CheckInternetExists.
     */
    @Test
    public void testCall() {
        System.out.println("call");
        CheckInternetExists instance = null;
        Boolean expResult = null;
        Boolean result = instance.call();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
