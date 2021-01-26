/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib.status;

import java.awt.Color;
import static java.awt.Color.RED;
import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
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
public class DBCStatusTest {
    
    public DBCStatusTest() {
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
     * Test of dispose method, of class DBCStatus.
     */
    @Test
    @Ignore
    public void testDispose() {
        System.out.println("dispose");
        DBCStatus instance = new DBCStatusImpl();
        instance.dispose();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getText method, of class DBCStatus.
     */
    @Test
    @Ignore //done by other tests
    public void testGetText() {
        System.out.println("getText");
        DBCStatus instance = new DBCStatusImpl();
        String expResult = "";
        String result = instance.getText();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initProgressBarVals method, of class DBCStatus.
     */
    @Test
    @Ignore
    public void testInitProgressBarVals() {
        System.out.println("initProgressBarVals");
        Point phaseBarVal = null;
        Point taskBarVal = null;
        DBCStatus instance = new DBCStatusImpl();
        instance.initProgressBarVals(phaseBarVal, taskBarVal);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

 

    /**
     * Test of resetTask method, of class DBCStatus.
     */
    @Test
    public void testResetTask() {
        System.out.println("resetTask");
        DBCStatus instance = new DBCStatusImpl();
           String notein = "This is my note";
        instance.setNote(notein);
        String result = instance.getText();
        String expected = "black:This is my note\n";
        instance.resetTask();
        result = instance.getText();
        assertTrue (result.isEmpty());
       
    }

    /**
     * Test of setNote method, of class DBCStatus.
     */
    @Test
    public void testSetNote_String_Color() {
        System.out.println("setNote");
        String notein = "This is my note";
        Color color = RED;
        DBCStatus instance = new DBCStatusImpl();
        instance.setNote(notein, color);
        String result = instance.getText();
        String expected = "java.awt.Color[r=255,g=0,b=0]:This is my note\n";
        assertEquals(expected, result);
       
        
    }

    /**
     * Test of setNote method, of class DBCStatus.
     */
    @Test
    public void testSetNote_String() {
        System.out.println("setNote");
        String notein = "This is my note";
        DBCStatus instance = new DBCStatusImpl();
        instance.setNote(notein);
        String result = instance.getText();
        String expected = "java.awt.Color[r=0,g=0,b=0]:This is my note\n";
        assertEquals(expected, result);
       // fail("The test case is a prototype.");
    }

    /**
     * Test of setPhase method, of class DBCStatus.
     */
    @Test
     @Ignore
    public void testSetPhase() {
        System.out.println("setPhase");
        int val = 0;
        String phaseStringIn = "";
        DBCStatus instance = new DBCStatusImpl();
        instance.setPhase(val, phaseStringIn);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTask method, of class DBCStatus.
     */
    @Test
     @Ignore
    public void testSetTask() {
        System.out.println("setTask");
        int val = 0;
        DBCStatus instance = new DBCStatusImpl();
        instance.setTask(val);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTaskIndeterminate method, of class DBCStatus.
     */
    @Test
     @Ignore
    public void testSetTaskIndeterminate() {
        System.out.println("setTaskIndeterminate");
        String message = "";
        DBCStatus instance = new DBCStatusImpl();
        instance.setTaskIndeterminate(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTaskMax method, of class DBCStatus.
     */
    @Test
    @Ignore
    public void testSetTaskMax() {
        System.out.println("setTaskMax");
        int val = 0;
        DBCStatus instance = new DBCStatusImpl();
        instance.setTaskMax(val);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class DBCStatusImpl implements DBCStatus {
        final StringBuilder stuff = new StringBuilder(2000);
        @Override
        public void dispose() {
        }

        @Override
        public String getText() {
            return stuff.toString();
        }

        @Override
        public void initProgressBarVals(Point phaseBarVal, Point taskBarVal) {
        }

      

        @Override
        public void resetTask() {
            stuff.delete(0,stuff.length());
        }

        @Override
        public void setNote(String notein, Color... colorin) {
            Color color = Color.BLACK;
            if (colorin.length>0 ){
                color = colorin[0];
            }
            stuff.append(color.toString()).append(':').append(notein).append('\n');
        }

      

   

        @Override
        public void setPhase(int val, String phaseStringIn) {
        }

        @Override
        public void setTask(int val) {
        }

        @Override
        public void setTaskIndeterminate(String message) {
        }

        @Override
        public void setTaskMax(int val) {
        }

     
    }

    /**
     * Test of setNote method, of class DBCStatus.
     */
    @Test
    public void testSetNote()
    {
        System.out.println("setNote");
        String notein = "";
        Color[] color = null;
        DBCStatus instance = new DBCStatusImpl();
        instance.setNote(notein, color);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    

   
  
    
}
