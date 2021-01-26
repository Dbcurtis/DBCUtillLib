/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import net.dbcrd.DBCUtilLib.TempFileRegistry.RegistryType;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class TempFileRegistryTest {
   
    static final List<String> MY_KEYS = Arrays.asList("Test123777","Test777123");
    
    public TempFileRegistryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
        try {
            Preferences prefsa = Preferences.userNodeForPackage(TempFileRegistry.class);
            List<String> keys = Arrays.asList(prefsa.keys());
            keys.stream().
                    filter((s) -> (MY_KEYS.contains(s))).
                    forEach((s) -> {
                        prefsa.remove(s);
                    });

        } catch (BackingStoreException ex) {
           LOG.log(Level.SEVERE, null, ex);
            fail("Unable to initialize");
        }
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            Preferences prefsa = Preferences.userNodeForPackage(TempFileRegistry.class);
            List<String> keys = Arrays.asList(prefsa.keys());
            final  List<Integer> countL = new ArrayList<>(1);
            countL.add(0);
            keys.stream().
                    filter((s) -> (MY_KEYS.contains(s))).
                    forEach((s) -> {
                        Integer a = countL.get(0);
                        a++;
                        countL.set(0, a);
                        prefsa.remove(s);
            });
            assertTrue(2 == countL.get(0));
        } catch (BackingStoreException ex) {
           LOG.log(Level.SEVERE, null, ex);
            fail("Unable to cleanup");
        }
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of cleanupTempFiles method, of class TempFileRegistry.
     */
    @Test
    @Ignore // tested in other tests
    public void testCleanupTempFiles() { 
        System.out.println("cleanupTempFiles");
//        TempFileRegistry instance = null;
//        List<Path> expResult = null;
//        List<Path> result = instance.cleanupTempFiles();
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTempFile method, of class TempFileRegistry.
     */
    @Test
    public void testCreateTempFile_3args()
            throws Exception {
        System.out.println("createTempFile");
       
        String prefix = "testPref1";
        String suffix = ".tmp";
        File findTempDir = File.createTempFile("deleteme", ".tmp");
        File directory = findTempDir.getParentFile();
        findTempDir.delete();
       
        TempFileRegistry instance =  new TempFileRegistry(MY_KEYS.get(0),  RegistryType.FILE_TYPE);
        
        File result = instance.createTempFile(prefix, suffix, directory);
        Path resultP = result.toPath();
        String fn = result.getName();
        assertTrue(fn.contains(prefix)&&fn.contains(suffix));
        assertTrue(result.exists());
        List<Path> cleanList = instance.cleanupTempFiles();
        assertFalse(cleanList.isEmpty());
        assertTrue( cleanList.contains(resultP));
        assertFalse(result.exists());

        //test numbered prefix and no dot on suffix
        cleanList.clear();
        prefix = "testpref123";
        suffix = "txt";
        result = instance.createTempFile(prefix, suffix, directory);
        resultP = result.toPath();
        fn = result.getName();
        assertTrue(fn.contains(prefix) && fn.contains(suffix));
        assertTrue(result.exists());
        cleanList = instance.cleanupTempFiles();
        assertFalse(cleanList.isEmpty());
        assertTrue(cleanList.contains(resultP));
        assertFalse(result.exists());
        
        result = instance.createTempFile("test1", ".tmp");
        result = instance.createTempFile("test1", ".tmp");
        result = instance.createTempFile("test2", ".tmp");
        result = instance.createTempFile("test3", ".tmp");
        result = instance.createTempFile("test4", ".tmp");
        result = instance.createTempFile("test5", "tmp");
        result = instance.createTempFile("test6", "tmp");
        
        cleanList.clear();
         cleanList = instance.cleanupTempFiles();
         assertEquals(7, cleanList.size());
 

    }

    /**
     * Test of createTempFile method, of class TempFileRegistry.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreateTempFile_String_String()
            throws Exception {
        System.out.println("createTempFile");
        String prefix = "testpref";
        String suffix = ".txt";
        TempFileRegistry instance = new TempFileRegistry(MY_KEYS.get(0), RegistryType.SYSTEM_TYPE);
       
        File result = instance.createTempFile(prefix, suffix);
        Path resultP = result.toPath();
        String fn = result.getName();
        assertTrue(fn.contains(prefix)&&fn.contains(suffix));
        assertTrue(result.exists());
        List<Path> cleanList = instance.cleanupTempFiles();
        assertFalse(cleanList.isEmpty());
        assertTrue( cleanList.contains(resultP));
        assertFalse(result.exists());

        //test numbered prefix and no dot on suffix
        cleanList.clear();
        prefix = "testpref123";
        suffix = "txt";
        result = instance.createTempFile(prefix, suffix);
        resultP = result.toPath();
        fn = result.getName();
        assertTrue(fn.contains(prefix) && fn.contains(suffix));
        assertTrue(result.exists());
        cleanList = instance.cleanupTempFiles();
        assertFalse(cleanList.isEmpty());
        assertTrue(cleanList.contains(resultP));
        assertFalse(result.exists());
        
        result = instance.createTempFile("test1", ".tmp");
        result = instance.createTempFile("test1", ".tmp");
        result = instance.createTempFile("test2", ".tmp");
        result = instance.createTempFile("test3", ".tmp");
        result = instance.createTempFile("test4", ".tmp");
        result = instance.createTempFile("test5", "tmp");
        result = instance.createTempFile("test6", "tmp");
        
        cleanList.clear();
         cleanList = instance.cleanupTempFiles();
         assertEquals(7, cleanList.size());
    }

    /**
     * Test of createTempFilePath method, of class TempFileRegistry.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreateTempFilePath_String_String()
            throws Exception {
        System.out.println("createTempFilePath");
        String prefix = "testpref";
        String suffix = ".txt";
        TempFileRegistry instance = new TempFileRegistry(MY_KEYS.get(1),  RegistryType.SYSTEM_TYPE);
        Path resultP = instance.createTempFilePath(prefix, suffix);
        String fn = resultP.getFileName().toString();
        assertTrue(fn.contains(prefix)&&fn.contains(suffix));
        assertTrue(Files.exists(resultP));
        List<Path> cleanList= instance.cleanupTempFiles();
        assertFalse(cleanList.isEmpty());
        assertTrue(cleanList.contains(resultP));
        assertFalse(Files.exists(resultP));

    }

    /**
     * Test of createTempFilePath method, of class TempFileRegistry.
     */
    @Test
    public void testCreateTempFilePath_3args()
            throws Exception {
        System.out.println("createTempFilePath");
        String prefix = "testPref1";
        String suffix = ".tmp";
        File findTempDir = File.createTempFile("deleteme", ".tmp");
        File directory = findTempDir.getParentFile();
        findTempDir.delete();
       
        TempFileRegistry instance =  new TempFileRegistry(MY_KEYS.get(1),  RegistryType.FILE_TYPE);
        
 
        Path resultP = instance.createTempFilePath(prefix, suffix);
        String fn = resultP.getFileName().toString();
        assertTrue(fn.contains(prefix)&&fn.contains(suffix));
        assertTrue(Files.exists(resultP));
        List<Path> cleanList= instance.cleanupTempFiles();
        assertFalse(cleanList.isEmpty());
        assertTrue(cleanList.contains(resultP));
        assertFalse(Files.exists(resultP));
    }
    private static final Logger LOG = Logger.getLogger(TempFileRegistryTest.class.getName());
    
}
