/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
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
public class TextTransferTest {

    public TextTransferTest() {
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
     * Test of lostOwnership method, of class TextTransfer.
     */
    @Test
    public void testLostOwnership() {
        System.out.println("lostOwnership");
        Clipboard aClipboard = null;
        Transferable aContents = null;
        TextTransfer instance = new TextTransfer();
        instance.lostOwnership(aClipboard, aContents);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setClipboardContents method, of class TextTransfer.
     */
    @Test
    public void testSetClipboardContents() {
        System.out.println("setClipboardContents");
        String aString = "";
        TextTransfer instance = new TextTransfer();
        instance.setClipboardContents(aString);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClipboardContents method, of class TextTransfer.
     */
    @Test
    public void testGetClipboardContents() {
        System.out.println("getClipboardContents");
        TextTransfer instance = new TextTransfer();
        String expResult = "";
        String result = instance.getClipboardContents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
