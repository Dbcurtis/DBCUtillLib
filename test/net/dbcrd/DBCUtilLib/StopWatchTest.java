/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
public class StopWatchTest {

    private static final Logger LOG = Logger.getLogger(StopWatchTest.class.getName());

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    public StopWatchTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of end method, of class StopWatch.
     */
    @Test
    public void testEnd() {
        try {
            System.out.println("end");
            StopWatch instance = new StopWatch();
            StopWatch instance2 = new StopWatch();
            Thread.sleep(1000);
            instance.end();
            assertEquals(1, instance.getTotalSeconds());
            assertTrue(1000 <= instance.getTotalMilliSeconds() && instance.getTotalMilliSeconds() < 1100);
            List<LocalDateTime> laps = instance.getLapTimes().collect(toList());
            assertEquals(1, laps.size());

            laps = instance2.getLapTimes().collect(toList());
            assertEquals(0, laps.size());
            Thread.sleep(1000);
            instance2.lap();
            laps = instance2.getLapTimes().collect(toList());
            assertEquals(1, laps.size());
            instance2.end();
            laps = instance2.getLapTimes().collect(toList());
            assertEquals(2, laps.size());
            Thread.sleep(1000);
            instance2.end();
            long secondEnd = instance2.getTotalSeconds();
            assertEquals(3, secondEnd);

        } catch (InterruptedException ex) {
            fail("Unexpected exception");
        }
    }

    /**
     * Test of getLapTimes method, of class StopWatch.
     */
    @Test
    public void testGetLapTimes() {
        System.out.println("getLapTimes");
        StopWatch instance = new StopWatch();
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                instance.lap();
            }
            instance.end();
            List<LocalDateTime> result = instance.getLapTimes().collect(toList());
            assertEquals(6, result.size());
            result.stream().limit(5).forEach((ldt) -> assertEquals(1, ldt.getSecond()));

        } catch (InterruptedException ex) {
            fail("Unexpected exception");
        }
    }
    /**
     * Test of getTotalMilliSeconds method, of class StopWatch.
     */
    @Test
    public void testGetTotalMilliSeconds() {
        System.out.println("getTotalMilliSeconds");
        
        StopWatch instance = new StopWatch();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            
        }
        instance.end();
        long mls = instance.getTotalMilliSeconds();
        assertTrue(mls > 2000L);
        assertTrue(mls < 2100L);
        
    }

    /**
     * Test of getTotalSeconds method, of class StopWatch.
     */
    @Test
    public void testGetTotalSeconds() {
        System.out.println("getTotalSeconds");

        StopWatch instance = new StopWatch();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {

        }
        instance.end();
        Optional< LocalDateTime> t1 = instance.getTotalTime();
        Assert.assertTrue(t1.isPresent());
        t1.get().getSecond();
        // long secs = instance.getTotalSeconds();   
        long expResult = 2L;
        long result = instance.getTotalSeconds();
        assertEquals(expResult, result);

    }

    /**
     * Test of getTotalTime method, of class StopWatch.
     */
    @Test
    public void testGetTotalTime() {
        System.out.println("getTotalTime");
        StopWatch instance = new StopWatch();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {

        }
        instance.end();
        LocalDateTime expResult = LocalDateTime.MIN.withYear(2000).withSecond(2);
        Optional< LocalDateTime> result = instance.getTotalTime();
        assertEquals(expResult, result.get());

    }

    /**
     * Test of lap method, of class StopWatch.
     */
    @Test
    public void testLap() {
        System.out.println("lap");
        StopWatch instance = new StopWatch();
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(2000);
                instance.lap();
            } catch (InterruptedException ex) {

            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {

        }
        instance.end();
        Stream<LocalDateTime> laps = instance.getLapTimes();
        // assertEquals(6, laps.size());
        laps
                .map((ldt) -> ldt.getSecond())
                .forEach((i) -> assertTrue(2 == i));

    }

    /**
     * Test of toString method, of class StopWatch.
     */
    @Test
    public void testToString() {
        try {
            System.out.println("toString");
            StopWatch instance = new StopWatch();
            String expResult = "running, Laps:   0";
            String result = instance.toString();
            assertEquals(expResult, result);
            instance.lap();
            expResult = "running, Laps:   1";
            result = instance.toString();
            assertEquals(expResult, result);
            Thread.sleep(1000);
            instance.end();
            result = instance.toString();
            expResult = "complete at  1 sec. Laps: 1";
            assertEquals(expResult, result);

        } catch (InterruptedException ex) {
            fail("unexpected exception");
        }
    }

    /**
     * Test of getTotalMiliSeconds method, of class StopWatch.
     */
    @Test
    public void testGetTotalMiliSeconds()
    {
        System.out.println("getTotalMiliSeconds");
        LocalDateTime startLDT = null;
        LocalDateTime endLDT = null;
        long expResult = 0L;
        long result = StopWatch.getTotalMiliSeconds(startLDT, endLDT);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTotalSeconds method, of class StopWatch.
     */
    @Test
    public void testGetTotalSeconds_LocalDateTime_LocalDateTime()
    {
        System.out.println("getTotalSeconds");
        LocalDateTime startLDT = null;
        LocalDateTime endLDT = null;
        long expResult = 0L;
        long result = StopWatch.getTotalSeconds(startLDT, endLDT);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTotalSeconds method, of class StopWatch.
     */
    @Test
    public void testGetTotalSeconds_0args()
    {
        System.out.println("getTotalSeconds");
        StopWatch instance = new StopWatch();
        long expResult = 0L;
        long result = instance.getTotalSeconds();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


}
