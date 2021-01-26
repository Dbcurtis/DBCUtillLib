
package net.dbcrd.DBCUtilLib;

import static com.vladium.utils.ResourceLoader.getResource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dan
 */
public class DosCmdTest {

    public DosCmdTest() {
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
     * Test of call method, of class DosCmd.
     */
    @Test
    public void testCall()  {
        try {
            System.out.println("call");
            final URL r = getResource("net/dbcrd/DBCUtilLib/testfiles/windowscmdbatchtest.bat");
            File windowbatfile = new File( r. toURI()) ;
            DosCmd instance = new DosCmd(windowbatfile);
            String result = instance.call();
            
            String expResult = "";
            
            assertTrue(result.contains("Microsoft Corporation"));
        } catch (IOException | InterruptedException |URISyntaxException ex) {
            fail("unexpected exception");
        } 
   
    }

    /**
     * Test of setTimeOut method, of class DosCmd.
     */
    @Test
    public void testSetTimeOut() {
        System.out.println("setTimeOut");
        int to = 0;
        DosCmd instance = null;
        instance.setTimeOut(to);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
