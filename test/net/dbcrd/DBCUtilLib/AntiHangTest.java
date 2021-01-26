/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;


import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author Dan
 */
public class AntiHangTest {

    public AntiHangTest() {
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
     * Test of values method, of class AntiHang.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        AntiHang[] expResult = {AntiHang.INSTANCE};
        AntiHang[] result = AntiHang.values();
        assertArrayEquals(expResult, result);

    }

    /**
     * Test of valueOf method, of class AntiHang.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "INSTANCE";
        AntiHang expResult = AntiHang.INSTANCE;
        AntiHang result = AntiHang.valueOf(name);
        assertEquals(expResult, result);
    
    }

    /**
     * Test of doit method, of class AntiHang.
     */
    @Test
    public void testDoit() {
        System.out.println("doit");
        WaitMessage wm = WaitMessage.main(new String[0]);
        assertTrue (wm.isDisplayable());
        AntiHang.doit();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            
        }
         assertFalse (wm.isDisplayable());
 
    }

}
