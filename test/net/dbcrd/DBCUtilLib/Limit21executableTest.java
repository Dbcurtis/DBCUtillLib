/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dan
 */
public class Limit21executableTest
{

    private static final Logger LOG = Logger.getLogger(Limit21executableTest.class.getName());
    private static final String TEST_ID = "unit test";
private static final String TEST_IDA = "unit_testLock.tmp";
    @BeforeClass
    public static void setUpClass()
    {
        File f  = new File(System.getProperty("user.home"),TEST_IDA);
        f.delete();
    }

    @AfterClass
    public static void tearDownClass()
    {
        File f  = new File(System.getProperty("user.home"),TEST_IDA);
        if (!f.exists()){
            fail("lock file not created");
        }
        f.delete();
    }

    public Limit21executableTest()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of limitToOneExecutible method, of class Limit21executable.
     */
    @Test
    public void testLimitToOneExecutible()
    {
        System.out.println("* Limit21executable: limitToOneExecutible");
        try
        {
            Limit21executable.limitToOneExecutible(TEST_ID);
        } catch (MultiInvocationException mive)
        {
            fail(" unexpected exception");
        }
        try
        {
            Limit21executable.limitToOneExecutible(TEST_ID);
            fail(" expected exception did not happen");
        } catch (MultiInvocationException mive)
        {

        }
    }

    /**
     * Test of valueOf method, of class Limit21executable.
     */
    @Test
    public void testValueOf()
    {
        System.out.println("* Limit21executable: valueOf ignored");

    }

    /**
     * Test of values method, of class Limit21executable.
     */
    @Test
    public void testValues()
    {
        System.out.println("* Limit21executable: values ignored");

    }

}
