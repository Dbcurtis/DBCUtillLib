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

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.UNORDERED;

/**
 * <Strong> Lambdas of general use </strong>
 *
 * @author Daniel B. Curtis
 */
@ClassPreamble(
    author = "Daniel B. Curtis",
    date = "Aug 2010 ",
    currentRevision = 3,
    lastModified = "08/23/2017",
    lastModifiedBy = "Daniel B. Curtis"
)
@SuppressWarnings("ClassWithoutLogger")
public enum Lambdas
{
    INSTANCEOF;

    /** Opposite of {@link #FAILS}. */
    public static final boolean PASSES = true;
    /** Opposite of {@link #PASSES}. */
    public static final boolean FAILS = false;
    /** Opposite of {@link #FAILURE}. */
    public static final boolean SUCCESS = true;
    /** Opposite of {@link #SUCCESS}. */
    public static final boolean FAILURE = false;

    /**
     * Useful for {@link String} operations, which return an index of <tt>-1</tt> when
     * an item is not found.
     */
    public static final int NOT_FOUND = -1;
    /** System property - <tt>line.separator</tt> */
    public static final String NEW_LINE = System.getProperty("line.separator");
    /** System property - <tt>file.separator</tt> */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    /** System property - <tt>path.separator</tt> */
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    /** an empty string */
    public static final String EMPTY_STRING = "";

    /** Single String space */
    public static final String SPACE = " ";

    /** a string with a single tab character */
    public static final String TAB = "\t";
    /** A String with a single java new line */
    public static final String JNL = "\n";
    /** a char that is a tab character */
    public static final char TABC = '\t';
    /** a char that is the java new line character */
    public static final char JNLC = '\n';

    /** its a single quote string */
    public static final String SINGLE_QUOTE = "'";

    /** its a sing period string */
    public static final String PERIOD = ".";

    /** its a single double quote string */
    public static final String DOUBLE_QUOTE = "\"";
    /** trims the string, and checks if it is empty */
    public final static Predicate<String> IS_BLANK = (String::isEmpty);
    /** negate of IS_BLANK */
    public final static Predicate<String> IS_NOT_BLANK = IS_BLANK.negate();
    /** does nothing */
    public final static Runnable DO_NOTHING = () -> 
    {
    };
    @SuppressWarnings("PublicInnerClass")
    public enum TFenum{     
        F_ALSE,
        T_RUE;
    }
    public final static Supplier<Map<TFenum,Runnable>> TF_RunnableMap = () ->{
        Map<TFenum,Runnable> jj = new EnumMap<>(TFenum.class); 
        jj.put(TFenum.T_RUE, ()->{});
        jj.put(TFenum.F_ALSE, () ->{});
        return jj;
        
    };
    

    
    public final static Function<Boolean,TFenum> Bool_2TF = j ->j?TFenum.T_RUE:TFenum.F_ALSE;   
        
    /**
     * BiConsumer that can throw an IllegalArgumentException
     *
     * @param <T> any Object
     * @param <U> Extends CharSequence used as the message in the exception
     */
    @FunctionalInterface
    @SuppressWarnings("PublicInnerClass")
    public interface BiConsumerThrow<T, U extends CharSequence>
    {

        /**
         *
         * @param t The object to be consumed and that may cause the IllegalArgumentException
         * @param u A CharSequence used as the message in the exception
         */
        public void accept(T t, U u)
            throws IllegalArgumentException;

    }
    /**
     * lambda to test if a string is blank (trimed and then if empty)
     * param s1 is the string to be tested, s2 is the message to be included in the IllegalArgumentException if thrown
     */
    public final static BiConsumerThrow<String, String> BLANK_STR_ILLEGAL = (s1, s2) -> 
    {
        if (IS_BLANK.test(s1))
        {
            throw new IllegalArgumentException(s2);
        }
    };
    /**
     * lambda to test if a collection is empty
     * param s1 is the collection to be tested, s2 is the message to be included in the IllegalArgumentException if thrown
     */
    public final static BiConsumerThrow<Collection<?>, String> EMPTY_COL_ILLEGAL = (s1, s2) -> 
    {        
        if (s1.isEmpty())
        {
            throw new IllegalArgumentException(s2);
        }
    };

    /**
     *
     * @param <T>
     * @param <U>
     * @param <V>
     */
    @FunctionalInterface
    @SuppressWarnings("PublicInnerClass")
    public interface TriConsumer<T, U, V>
    {

        /**
         *
         * @param t
         * @param u
         * @param v
         */
        public void accept(T t, U u, V v);
    }

    /**
     * tbd
     */
    @FunctionalInterface
    @SuppressWarnings("PublicInnerClass")
    interface DoIf
    {

        /**
         *
         * @param b
         * @param r
         */
        public void accept(Boolean b, Runnable r);
    }

    /**
     * runs code dependant on a boolean
     */
    @FunctionalInterface
    @SuppressWarnings("PublicInnerClass")
    interface DoIfElse
    {

        /**
         *
         * @param b  boolean to test
         * @param rt a runnable to run if b is true
         * @param rf a runnable to run if b is false
         */
        public void accept(boolean b, Runnable rt, Runnable rf);
    }

    /** trims a string */
    public final static Function<String, String> TRIMMED = (String::trim);

    /** returns true if string is empty */
    public final static Predicate<String> IS_EMPTY = (String::isEmpty);//.negate();

    /** returns true if string is not empty */
    public final static Predicate<String> IS_NOT_EMPTY = IS_EMPTY.negate();

    /** returns true if an optional is present */
    public final static Predicate<Optional<?>> IS_PRESENT = Optional::isPresent;

    /** returns true if an optional is not present */
    public final static Predicate<Optional<?>> IS_NOT_PRESENT = IS_PRESENT.negate();

//    /**
//     *
//     * @deprecated
//     */
//    @Deprecated
//    final static BiConsumer<Boolean, Runnable> DO_IFT = ((b, r) ->
//                                                         {
//                                                             if (b)
//                                                             {
//                                                                 r.run();
//                                                             }
//                                                         });
//
//    /**
//     *
//     * @deprecated
//     */
//    @Deprecated
//    final static BiConsumer<Boolean, Runnable> DO_IFF = ((b, r) ->
//                                                         {
//                                                             if (!b)
//                                                             {
//                                                                 r.run();
//                                                             }
//                                                         });
//
//    /**
//     *
//     * @deprecated
//     */
//    @Deprecated
//    final static DoIfElse DO_IFE = ((boolean b, Runnable rt, Runnable rf) ->
//                                    {
//                                        if (b)
//                                        {
//                                            rt.run();
//                                        } else
//                                        {
//                                            rf.run();
//                                        }
//                                    });
    /** Sleep Lambda */
    public static final Consumer<Integer> SLEEP = (val) -> 
    {
        try
        {
            Thread.sleep(val);
        } catch (InterruptedException ignoreInterruptedException)
        {
        }

    };

    /**
     * A Navigable ConcurrentSkipList collector
     *
     * @param <T>
     */
    @SuppressWarnings("PublicInnerClass")
    public static class NavigableSetCollector<T>
        implements Collector<
             T, Set<T>, NavigableSet<T>>
    {

        @Override
        public BiConsumer<Set<T>, T> accumulator()
        {
            return (Set<T> acc, T di) -> acc.add(di);
        }

        @Override
        public Set<Collector.Characteristics> characteristics()
        {
            return Collections.unmodifiableSet(EnumSet.of(UNORDERED, CONCURRENT));
        }

        @Override
        public BinaryOperator<Set<T>> combiner()
        {
            return (set1, set2) -> 
            {
                set1.addAll(set2);
                return set1;
            };
        }

        @Override
        public Function<Set<T>, NavigableSet<T>> finisher()
        {
            return (acc) -> 
            {
                NavigableSet<T> result = new ConcurrentSkipListSet<>();
                result.addAll(acc);
                return result;
            };
        }

        @Override
        public Supplier<Set<T>> supplier()
        {
            return () -> Collections.newSetFromMap(new ConcurrentHashMap<>(200));
        }

    }

//    /**
//     * The caller references the constants using <tt>Lambdas.EMPTY_STRING</tt>,
//     * and so on. Thus, the caller should be prevented from constructing objects of
//     * this class, by declaring this private constructor.
//     */
//    private Lambdas()
//    {
//        //this prevents even the native class from 
//        //calling this ctor as well :
//        throw new AssertionError();
//    }
    
       @SuppressWarnings("PublicInnerClass")
    public static class TFMapGen<V>
    {
       public Map<Lambdas.TFenum, V> get()
        {
            return new EnumMap<>(Lambdas.TFenum.class);

        }
    }
}
  
 
