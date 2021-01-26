/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.io.File;
import java.net.URL;
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
public class BareBonesBrowserLaunchTest {

    public BareBonesBrowserLaunchTest() {
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
     * Test of openURL method, of class BareBonesBrowserLaunch.
     */
    @Test
    public void testOpenURL() {
        System.out.println("openURL");
        String url = "";
        BareBonesBrowserLaunch.openURL(url);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of openWindowsNotepad method, of class BareBonesBrowserLaunch.
     */
    @Test
    public void testOpenWindowsNotepad() {
        System.out.println("openWindowsNotepad");
        String url = "";
        BareBonesBrowserLaunch.openWindowsNotepad(url);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of send2Browser method, of class BareBonesBrowserLaunch.
     */
    @Test
    public void testSend2Browser_URL_String() {
        System.out.println("send2Browser");
        URL url = null;
        String threadId = "";
        BareBonesBrowserLaunch.send2Browser(url, threadId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of send2Browser method, of class BareBonesBrowserLaunch.
     */
    @Test
    public void testSend2Browser_File_String() throws Exception {
        System.out.println("send2Browser");
        File tempfile = null;
        String threadId = "";
        BareBonesBrowserLaunch.send2Browser(tempfile, threadId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
