/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
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
public class MyFilesTest {

    public MyFilesTest() {
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
     * Test of values method, of class MyFiles.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        MyFiles[] expResult = null;
        MyFiles[] result = MyFiles.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class MyFiles.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        MyFiles expResult = null;
        MyFiles result = MyFiles.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of copyRecursive method, of class MyFiles.
     */
    @Test
    public void testCopyRecursive() throws Exception {
        System.out.println("copyRecursive");
        Path source = null;
        Path target = null;
        boolean prompt = false;
        CopyOption[] options = null;
        MyFiles instance = null;
        FileVisitResult expResult = null;
        FileVisitResult result = instance.copyRecursive(source, target, prompt, options);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteDir method, of class MyFiles.
     */
    @Test
    public void testDeleteDir()
    {
        System.out.println("deleteDir");
        File file = null;
        MyFiles instance = null;
        instance.deleteDir(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
