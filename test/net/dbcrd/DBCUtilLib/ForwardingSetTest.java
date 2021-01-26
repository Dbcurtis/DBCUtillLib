/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.util.Collection;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
public class ForwardingSetTest {
    
    public ForwardingSetTest() {
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
     * Test of clear method, of class ForwardingSet.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        ForwardingSet instance = null;
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contains method, of class ForwardingSet.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        Object o = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.contains(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEmpty method, of class ForwardingSet.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class ForwardingSet.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        ForwardingSet instance = null;
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of iterator method, of class ForwardingSet.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        ForwardingSet instance = null;
        Iterator expResult = null;
        Iterator result = instance.iterator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class ForwardingSet.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Object e = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.add(e);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class ForwardingSet.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Object o = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.remove(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of containsAll method, of class ForwardingSet.
     */
    @Test
    public void testContainsAll() {
        System.out.println("containsAll");
        Collection c = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAll method, of class ForwardingSet.
     */
    @Test
    public void testAddAll() {
        System.out.println("addAll");
        Collection c = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeAll method, of class ForwardingSet.
     */
    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        Collection c = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retainAll method, of class ForwardingSet.
     */
    @Test
    public void testRetainAll() {
        System.out.println("retainAll");
        Collection c = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toArray method, of class ForwardingSet.
     */
    @Test
    public void testToArray_0args() {
        System.out.println("toArray");
        ForwardingSet instance = null;
        Object[] expResult = null;
        Object[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toArray method, of class ForwardingSet.
     */
    @Test
    public void testToArray_GenericType() {
        System.out.println("toArray");
        Object[] a = null;
        ForwardingSet instance = null;
        Object[] expResult = null;
        Object[] result = instance.toArray(a);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class ForwardingSet.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        ForwardingSet instance = null;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class ForwardingSet.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        ForwardingSet instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class ForwardingSet.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        ForwardingSet instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
