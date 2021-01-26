package net.dbcrd.DBCUtilLib;

/*
 * Copyright (c) 2015, Daniel B. Curtis <dbcurtis@dbcrd.net>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Daniel B. Curtis <dbcurtis@dbcrd.net>
 */
public class WaitMessageTest {

    public WaitMessageTest() {
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
     * Test of main method, of class WaitMessage.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        WaitMessage expResult = null;
        {
            final WaitMessage wm = WaitMessage.main(args);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertTrue(wm.isActive());
            assertTrue(wm.isDisplayable());
            assertTrue(wm.isShowing());
            assertTrue(wm.isValid());
            assertTrue(wm.isVisible());
            java.awt.EventQueue.invokeLater(() -> {
                wm.dispose();
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertFalse(wm.isActive());
            assertFalse(wm.isDisplayable());
            assertFalse(wm.isShowing());
            assertFalse(wm.isValid());
            assertFalse(wm.isVisible());
        }
        {
            final WaitMessage wm = WaitMessage.main(new String[0]);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertTrue(wm.isActive());
            assertTrue(wm.isDisplayable());
            assertTrue(wm.isShowing());
            assertTrue(wm.isValid());
            assertTrue(wm.isVisible());
            java.awt.EventQueue.invokeLater(() -> {
                wm.dispose();
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertFalse(wm.isActive());
            assertFalse(wm.isDisplayable());
            assertFalse(wm.isShowing());
            assertFalse(wm.isValid());
            assertFalse(wm.isVisible());
        }
        {
            String[] a = {"", "secondarg"};
            final WaitMessage wm = WaitMessage.main(a);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertTrue(wm.isActive());
            assertTrue(wm.isDisplayable());
            assertTrue(wm.isShowing());
            assertTrue(wm.isValid());
            assertTrue(wm.isVisible());
            java.awt.EventQueue.invokeLater(() -> {
                wm.dispose();
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertFalse(wm.isActive());
            assertFalse(wm.isDisplayable());
            assertFalse(wm.isShowing());
            assertFalse(wm.isValid());
            assertFalse(wm.isVisible());

        }
        {
            String[] a = {"All Done"};
            final WaitMessage wm = WaitMessage.main(a);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertTrue(wm.isActive());
            assertTrue(wm.isDisplayable());
            assertTrue(wm.isShowing());
            assertTrue(wm.isValid());
            assertTrue(wm.isVisible());
            java.awt.EventQueue.invokeLater(() -> {
                wm.dispose();
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {

            }
            assertFalse(wm.isActive());
            assertFalse(wm.isDisplayable());
            assertFalse(wm.isShowing());
            assertFalse(wm.isValid());
            assertFalse(wm.isVisible());

        }
    }

    /**
     * Test of dispose method, of class WaitMessage.
     */
    @Test
    @Ignore
    public void testDispose() {
        System.out.println("dispose");
        WaitMessage instance = null;
        instance.dispose();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
