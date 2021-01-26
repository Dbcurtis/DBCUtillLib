package net.dbcrd.DBCUtilLib.settings;

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


import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Daniel B. Curtis <dbcurtis@dbcrd.net>
 */
public class SettingsTableModelTest {
    private static final Logger LOG = Logger.getLogger(SettingsTableModelTest.class.getName());
    final static List<Setting> presettings = new LinkedList<>();
    
    @BeforeClass
    public static void setUpClass() {
        presettings.add(Setting.factory());
        presettings.add(Setting.factory());
        presettings.add(Setting.factory());
        presettings.add(Setting.factory());

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    public SettingsTableModelTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getData method, of class SettingsTableModel.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        SettingsTableModel instance = new SettingsTableModel();
        List<Setting> expResult = presettings;
        List<Setting> result = instance.getData();
        assertEquals(expResult, result);
 
    }

    /**
     * Test of getRowCount method, of class SettingsTableModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        SettingsTableModel instance = new SettingsTableModel();
        int expResult = 4;
        int result = instance.getRowCount();
        assertEquals(expResult, result);

    }

    /**
     * Test of getColumnCount method, of class SettingsTableModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        SettingsTableModel instance = new SettingsTableModel();
        int expResult = 3;
        int result = instance.getColumnCount();
        assertEquals(expResult, result);
     
    }

    /**
     * Test of getValueAt method, of class SettingsTableModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        int rowIndex = 0;
        int columnIndex = 0;
        SettingsTableModel instance = new SettingsTableModel();
        Object expResult = "undefined0";
        Object result = instance.getValueAt(0, 0);
        assertEquals(expResult, result);
        result = instance.getValueAt(3, 0);
        expResult = "undefined3";
        assertEquals(expResult, result);

    }

    /**
     * Test of getColumnName method, of class SettingsTableModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");

        SettingsTableModel instance = new SettingsTableModel();
        String expResult = "Property";
        String result = instance.getColumnName(0);
        assertEquals(expResult, result);

        expResult = "Type";
        result = instance.getColumnName(1);
        assertEquals(expResult, result);

        expResult = "Value";
        result = instance.getColumnName(2);
        assertEquals(expResult, result);
        assertEquals(3, instance.getColumnCount());
    }

    /**
     * Test of isCellEditable method, of class SettingsTableModel.
     */
    @Test
    public void testIsCellEditable() {
        System.out.println("isCellEditable");
        int rowIndex = 0;
        int columnIndex = 0;
        SettingsTableModel instance = new SettingsTableModel();
        assertEquals(3, instance.getColumnCount());
        boolean expResult = true;
        boolean result = instance.isCellEditable(0, 0);
        assertEquals(expResult, result);
        result = instance.isCellEditable(0, 1);
        assertEquals(expResult, result);
          result = instance.isCellEditable(1, 1);
        assertEquals(expResult, result);
          result = instance.isCellEditable(1, 2);
         assertEquals(false, result);
               result = instance.isCellEditable(0, 2);
         assertEquals(false, result);
        
    }

    /**
     * Test of setValueAt method, of class SettingsTableModel.
     */
    @Test
    public void testSetValueAt() {
        System.out.println("setValueAt");
        Object value = "newValueString";
        int rowIndex = 0;
        int columnIndex = 0;
        SettingsTableModel instance = new SettingsTableModel();
        instance.setValueAt(value, rowIndex, columnIndex);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
