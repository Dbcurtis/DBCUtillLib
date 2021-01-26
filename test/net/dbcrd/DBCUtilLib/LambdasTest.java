/*
 * Copyright (C) 2017 dbcurtis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.dbcrd.DBCUtilLib;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static net.dbcrd.DBCUtilLib.Lambdas.BLANK_STR_ILLEGAL;
import static net.dbcrd.DBCUtilLib.Lambdas.EMPTY_COL_ILLEGAL;
import static net.dbcrd.DBCUtilLib.Lambdas.IS_EMPTY;
import static net.dbcrd.DBCUtilLib.Lambdas.IS_NOT_BLANK;
import static net.dbcrd.DBCUtilLib.Lambdas.IS_NOT_EMPTY;
import static net.dbcrd.DBCUtilLib.Lambdas.IS_NOT_PRESENT;
import static net.dbcrd.DBCUtilLib.Lambdas.IS_PRESENT;
import net.dbcrd.DBCUtilLib.Lambdas.NavigableSetCollector;
import static net.dbcrd.DBCUtilLib.Lambdas.SLEEP;
import static net.dbcrd.DBCUtilLib.Lambdas.TRIMMED;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dbcurtis
 */
public class LambdasTest 
{

    public LambdasTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testNavigableSetCollector()
    {
        System.out.println("NavigableSetCollector");
        Integer zero = 0;
        NavigableSet<Integer> ints0 = new ArrayList<Integer>(2).stream().collect(new NavigableSetCollector<Integer>());
        assertTrue(ints0.isEmpty());

        List<Integer> ints1 = IntStream.range(1, 10001).boxed().collect(toList());
        ints1.addAll(IntStream.range(0, 10000).boxed().collect(toList()));
        NavigableSet<Integer> intset = ints1.parallelStream()
                .collect(new NavigableSetCollector<Integer>());
        Integer maxv = 10001;
        assertEquals(maxv.intValue(), intset.size());
        assertEquals(zero, intset.first());
        assertTrue(maxv - 1 - intset.last() == 0);

        fail("tests incomplete");
    }

    @Test
    public void testTRIMMED()
    {
        System.out.println("TRIMMED");
        Stream<String> stringS = Stream.of("one", " spaced one ", "", "     ");
        List<String> resultL = stringS.map(TRIMMED).collect(toList());
        assertEquals(4, resultL.size());
        assertEquals("one", resultL.get(0));
        assertEquals("spaced one", resultL.get(1));
        List<String> result2L = resultL.stream().filter(s -> !s.isEmpty()).collect(toList());
        assertEquals(2, result2L.size());

    }
  @Test
    public void testIS_BLANK()
    {
        System.out.println("IS_BLANK and IS_NOT_BLANK");
        Stream<String> stringS = Stream.of("one", " spaced one ", "", "     ");
        List<String> src = stringS.collect(toList());
        List<String> resultL = src.stream().filter(IS_NOT_BLANK).collect(toList());
        assertEquals(2, resultL.size());
        resultL = src.stream().map(TRIMMED).filter(IS_NOT_EMPTY).collect(toList());
        assertEquals(2, resultL.size());

    }
    @Test
    public void testEMPTY()
    {
        System.out.println("EMPTY");
        Stream<String> stringS = Stream.of("one", " spaced one ", "", "     ");
        List<String> src = stringS.collect(toList());
        List<String> resultL = src.stream().filter(IS_EMPTY).collect(toList());
        assertEquals(1, resultL.size());
        resultL = src.stream().map(TRIMMED).filter(IS_EMPTY).collect(toList());
        assertEquals(2, resultL.size());

    }

    @Test
    public void testNOT_EMPTY()
    {
        System.out.println("NOT_EMPTY");
        Stream<String> stringS = Stream.of("one", " spaced one ", "", "     ");
        List<String> src = stringS.collect(toList());
        List<String> resultL = src.stream().filter(IS_NOT_EMPTY).collect(toList());
        assertEquals(3, resultL.size());
        resultL = src.stream().map(TRIMMED).filter(IS_NOT_EMPTY).collect(toList());
        assertEquals(2, resultL.size());

    }

    @Test
    public void testPRESENT()
    {
        System.out.println("PRESENT and NOT_PRESENT");
        Stream<String> stringS = Stream.of("one", " spaced one ", "", "     ");
        List<Optional<String>> src = stringS.map(s -> Optional.of(s)).collect(toList());
        src.add(Optional.empty());
        assertEquals(5, src.size());
        List<Optional<String>> resultL = src.stream().filter(IS_PRESENT).collect(toList());
        assertEquals(4, resultL.size());
        resultL = src.stream().filter(IS_NOT_PRESENT).collect(toList());
        assertEquals(1, resultL.size());

    }
    
    @Test
    public void testEMPTY_COL_ILLEGAL()
            {
        System.out.println("testEMPTY_COL_ILLEGAL");
        Stream<String> stringS = Stream.of("one", " spaced one ", "", "     ");
        List<Optional<String>> src = stringS.map(s -> Optional.of(s)).collect(toList());
        src.add(Optional.empty());
        assertEquals(5, src.size());
        try{
            EMPTY_COL_ILLEGAL.accept(src,"should be legal");
        }catch (IllegalArgumentException iae){
            fail("exception should not be thrown");
        }
        src.clear();
          try{
            EMPTY_COL_ILLEGAL.accept(src,"should not be legal");
             fail("exception should  be thrown");
        }catch (IllegalArgumentException iae){
           
        }
  

    }
    
    @Test
    public void testSLEEP(){
         System.out.println("testEMPTY_STR_ILLEGAL");
         long start = System.currentTimeMillis();
         SLEEP.accept(1000);
         long end = System.currentTimeMillis();
         long dif = end-start;
         long difDiv10 = Math.abs(dif - 1000);
         assertTrue(difDiv10<20);
    }
     @Test
    public void testEMPTY_STR_ILLEGAL()
            {
        System.out.println("testEMPTY_STR_ILLEGAL");
        Stream<String> stringS = Stream.of("one", " spaced one ", "", "     ");
        List<Optional<String>> src = stringS.map(s -> Optional.of(s)).collect(toList());
        src.add(Optional.empty());
        assertEquals(5, src.size());
        try{
            BLANK_STR_ILLEGAL.accept(" not empty ","should be legal");
        }catch (IllegalArgumentException iae){
            fail("exception should not be thrown");
        }
        
          try{
            BLANK_STR_ILLEGAL.accept("","should not be legal");
             fail("exception should  be thrown");
        }catch (IllegalArgumentException iae){
           
        }
        try{
            BLANK_STR_ILLEGAL.accept("     ","should not be legal");
             fail("exception should  be thrown");
        }catch (IllegalArgumentException iae){
           
        }
  

    }
}
