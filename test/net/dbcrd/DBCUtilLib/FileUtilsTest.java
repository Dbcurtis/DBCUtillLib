package net.dbcrd.DBCUtilLib;

/*
 * Copyright (c) 2015, Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class FileUtilsTest
{

    private static final Logger LOG = Logger.getLogger(FileUtilsTest.class.getName());
    static Properties sysprop;

    @BeforeClass
    public static void setUpClass()
    {
        sysprop = System.getProperties();
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    public FileUtilsTest()
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


    /**
     * Test of bufferedWrite method, of class FileUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testBufferedWrite_JSONArray_Path()
            throws Exception
    {
        System.out.println("bufferedWrite");

        Path dirpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles");
        File d = dirpath.toFile();
        assertTrue(d.exists() && d.isDirectory() && d.canWrite());
        File tf = File.createTempFile("deleteme", ".json", d);
        tf.deleteOnExit();
        Path tp = tf.toPath();
        StringBuilder sb = new StringBuilder(512);
        List<Jjj> myList = new LinkedList<>();
        for (int i = 0; i < 10; i++)
        {
            sb.delete(0, sb.length());
            sb.append(i).append("->").append("this ").append(i);
            Jjj j = new Jjj(i, sb.toString());
            // JSONObject jo1 = new JSONObject(j.toJSONString());
            myList.add(j);

        }

        Jjj[] jarray = myList.toArray(new Jjj[myList.size()]);
        JSONArray ja = new JSONArray(jarray);
        new FileUtils().bufferedWrite(ja, tp);

        sb = new FileUtils().readPathToSBSpace(tp);
        JSONArray jain = new JSONArray(sb.toString());
        assertTrue(jain.length() == ja.length());

        for (int i = 0; i < jain.length(); i++)
        {
            Jjj jin = new Jjj(jain.getJSONObject(i));
            Jjj jout = (Jjj) ja.get(i);
            assertEquals(jin.toString(), jout.toString());
        }
        tf.delete();
    }

    /**
     * Test of bufferedWrite method, of class FileUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testBufferedWrite_JSONObject_Path()
            throws Exception
    {

        System.out.println("bufferedWrite");

        Path dirpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles");
        File d = dirpath.toFile();
        assertTrue(d.exists() && d.isDirectory() && d.canWrite());
        File tf = File.createTempFile("deleteme", ".json", d);
        tf.deleteOnExit();
        Path tp = tf.toPath();
        StringBuilder sb = new StringBuilder(512);
        Map<String, Object> myMap = new HashMap<>(100);
        for (int i = 0; i < 10; i++)
        {
            sb.delete(0, sb.length());
            sb.append(i).append("->").append("this ").append(i);
            Jjj j = new Jjj(i, sb.toString());
            // JSONObject jo1 = new JSONObject(j.toJSONString());
            myMap.put(Integer.toString(i), j);

        }
        JSONObject jo = new JSONObject(myMap);
        new FileUtils().bufferedWrite(jo, tp);

        sb = new FileUtils().readPathToSBSpace(tp);
        JSONObject join = new JSONObject(sb.toString());
        assertTrue(join.length() == jo.length());
        Map<String, Object> myMapIn = new HashMap<>(100);
        Iterator<String> i = join.keys();
        while (i.hasNext())
        {
            JSONObject jo1 = join.getJSONObject(i.next());
            Jjj j = new Jjj(jo1);
            myMapIn.put(Integer.toString(j.key), j);

        }
        Set<String> koutSet = myMap.keySet();
        Set<String> kinSet = myMapIn.keySet();
        assertEquals(koutSet, kinSet);
        koutSet.stream().forEach((key) -> 
                {
                    final Object jino = myMapIn.get(key);
                    final Object jouto = myMap.get(key);
                    assertTrue(jino instanceof Jjj && jouto instanceof Jjj);
                    final Jjj jin = (Jjj) jino;
                    final Jjj jout = (Jjj) jouto;
                    assertEquals(jin.toString(), jouto.toString());
        });

//       
        tf.delete();

    }
    /**
     * Test of bufferedWrite method, of class FileUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testBufferedWrite_List_Path()
            throws IOException
    {
        System.out.println("bufferedWrite");
        Path inpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
        List<String> content = new FileUtils().readPathToLineStream(inpath).collect(toList());
        Path dirpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles");
        File d = dirpath.toFile();
        assertTrue(d.exists() && d.isDirectory() && d.canWrite());
        File tf = File.createTempFile("deleteme", ".txt", d);
        tf.deleteOnExit();
        Path tp = tf.toPath();
        new FileUtils().bufferedWrite(content, tp);
        List<String> result = new FileUtils().readPathToLineStream(tp).collect(toList());
        assertEquals(content, result);
        tf.delete();

    }

    /**
     * Test of bufferedWrite method, of class FileUtils.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testBufferedWrite_String_Path()
            throws IOException
    {
        System.out.println("bufferedWrite");
        Path inpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
        String content = new FileUtils().readPathToSBSpace(inpath).toString();
        Path dirpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles");
        File d = dirpath.toFile();
        assertTrue(d.exists() && d.isDirectory() && d.canWrite());
        File tf = File.createTempFile("deleteme", ".txt", d);
        tf.deleteOnExit();
        Path tp = tf.toPath();
        new FileUtils().bufferedWrite(content, tp);
        String result = new FileUtils().readPathToSBSpace(tp).toString();
        assertEquals(content, result);
        tf.delete();

    }
    /**
     * Test of copyURL2Path method, of class FileUtils.
     */
    @Test
    public void testCopyURL2Path()
            throws Exception
    {
        System.out.println("copyURL2Path");
        Path filePath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
        URL in = filePath.toUri().toURL();
        Path out = Files.createTempFile("l_am_", ".zip");
        
        boolean overwrite = true;
        FileUtils instance = new FileUtils();
        instance.copyURL2Path(in, out, overwrite);
        assertTrue(Files.exists(out));
        Files.delete(out);
        
    }


    /**
     * Test of deleteContentsOf method, of class FileUtils.
     */
    @Test
    public void testDeleteContentsOf()
            throws Exception
    {
        System.out.println("deleteContentsOf");
        Path userDirPth = Paths.get(System.getProperty("user.dir"), "test", "net", "dbcrd", "DBCUtilLib", "testfiles");
        assertTrue(null != userDirPth);
        assertTrue(Files.isDirectory(userDirPth));

        Path tempdirPath = Paths.get(userDirPth.toString(), "UnitTestTempDir");
        Path subDir1P = Paths.get("sub1");
        Path subDir2P = Paths.get("sub2");
        Path tstf1 = Paths.get("testfile1.tmp");
        Path tstf2 = Paths.get("testfile2.tmp");

        if (!Files.exists(tempdirPath))
        {
            Path subdir = tempdirPath.resolve(subDir1P);
            Files.createDirectories(subdir);
            Files.createFile(subdir.resolve(tstf1));
            Files.createFile(subdir.resolve(tstf2));
            subdir = tempdirPath.resolve(subDir2P);
            Files.createDirectories(subdir);
            Files.createFile(subdir.resolve(tstf1));
            Files.createFile(subdir.resolve(tstf2));

        }

        boolean expResult = true;
        boolean result = FileUtils.deleteContentsOf(tempdirPath);

        assertEquals(expResult, result);
        assertTrue(Files.exists(tempdirPath));
        assertTrue(Files.isDirectory(tempdirPath));
        assertFalse(Files.exists(tempdirPath.resolve(subDir1P)));
        assertFalse(Files.exists(tempdirPath.resolve(subDir2P)));
        assertTrue(Files.deleteIfExists(tempdirPath));

    }

    /**
     * Test of deleteRecursive method, of class FileUtils.
     */
    @Test
    @Ignore // tested in deleteContentsOf
    public void testDeleteRecursive()
            throws Exception
    {
        System.out.println("deleteRecursive");
        Path path = null;
        boolean expResult = false;
        boolean result = FileUtils.deleteRecursive(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of findFile method, of class FileUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testFindFile_Path()
            throws Exception
    {
        System.out.println("findFilePath");
        Path p = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
        FileUtils instance = new FileUtils();
        Path expResult = null;
        Path result = instance.findFile(p, 2).collect(toList()).get(0);
        assertEquals(p, result);
        try
        {
            assertTrue(instance.findFile(Paths.get("jjj,tst"), 3).collect(toList()).isEmpty());
            fail("should have an exception if file not found");
        } catch (FileNotFoundException fnfe)
        {

        }

        List<Path> results = instance.findFile(p.getFileName(), 6).collect(toList());
        assertEquals(1, results.size());
        assertEquals(p, results.get(0));
        try
        {
            results = instance.findFile(p.getFileName(), 5).collect(toList());
            fail("should have an exception as file cannot be found width depth 5");
        } catch (FileNotFoundException fnfe)
        {

        }
        final List<Path> hits = new FileUtils().findFile("SQLProcedureDefs.sql").collect(toList());
        if (hits.size()>1){
            final NavigableMap<FileTime,Path> modDate2Path =new ConcurrentSkipListMap<>();
           final NavigableMap<FileTime,Path> creationDate2Path =new ConcurrentSkipListMap<>();
             NavigableMap<FileTime,Path> date2Path =new ConcurrentSkipListMap<>();
            hits.stream().forEach(pp->{
                try
                {
                  Map<String,Object> m= Files.readAttributes(pp, "lastModifiedTime,creationTime");//"basic:creationTime,lastModifiedTime");
                  modDate2Path.put((FileTime) m.get("lastModifiedTime"), pp);
                  creationDate2Path.put((FileTime) m.get("creationTime"), pp);        
                  
                    
                } catch (IOException ex)
                {
                    fail("unexpected exception");
                }
            
            });
                   
            date2Path.putAll(modDate2Path);
            date2Path.putAll(creationDate2Path);
            
            int i=0;
            i++;
        }
        
        
        //TODO add some functionality in particular allow ../.././file.ext
        // TODO review the generated test code and remove the default call to fail.
        //  fail("The test case is a prototype.");
    }
    /**
     * Test of findFile method, of class FileUtils.
     */
    @Test
    public void testFindFile_Path_intArr()
            throws Exception
    {
        System.out.println("findFile");
        Path p = null;
        int[] depthin = null;
        FileUtils instance = new FileUtils();
        Stream<Path> expResult = null;
        Stream<Path> result = instance.findFile(p, depthin);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findFile method, of class FileUtils.
     */
    @Test
    public void testFindFile_String()
            throws Exception
    {
        System.out.println("findFileString");
        String fileNameIn = "test1.php";
        FileUtils instance = new FileUtils();
        Path expResult = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
        Path result = instance.findFile(fileNameIn, 6).collect(toList()).get(0);
        assertEquals(expResult, result);

    }
    /**
     * Test of findFile method, of class FileUtils.
     */
    @Test
    public void testFindFile_String_intArr()
            throws Exception
    {
        System.out.println("findFile");
        String fileNameIn = "";
        int[] depth = null;
        FileUtils instance = new FileUtils();
        Stream<Path> expResult = null;
        Stream<Path> result = instance.findFile(fileNameIn, depth);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of findFile method, of class FileUtils.
     */
    @Test
    public void testFindFile_URI()
            throws Exception
    {
        System.out.println("findFileURI");
        URI fileNameIn = Paths.get("test1.php").toUri();
        FileUtils instance = new FileUtils();
        Path expResult = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
        Path result = instance.findFile(fileNameIn, 6).collect(toList()).get(0);
        assertEquals(expResult, result);

    }

    /**
     * Test of findFile method, of class FileUtils.
     */
    @Test
    public void testFindFile_URI_intArr()
            throws Exception
    {
        System.out.println("findFile");
        URI uriIn = null;
        int[] depth = null;
        FileUtils instance = new FileUtils();
        Stream<Path> expResult = null;
        Stream<Path> result = instance.findFile(uriIn, depth);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of readPathToLineStream method, of class FileUtils.
     */
    @Test
    public void testReadPathToLineStream()
            throws Exception
    {
        System.out.println("readPathToLineStream");
        Path inpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
        File f = inpath.toFile();
        assertTrue(f.exists());
        FileUtils instance = new FileUtils();
        List<String> expResult = null;
        List<String> result = new ArrayList<>(1);
        try
        {
            result = instance.readPathToLineStream(inpath).collect(toList());
        } catch (FileNotFoundException ex)
        {
            fail("file not found");
        }
        assertTrue(67 == result.size());
        assertTrue(result.get(1).trim().startsWith("//"));
    }
    /**
     * Test of readPathToLines method, of class Utility.
     */
    @Test
    public void testReadPathToLines()
    {
        System.out.println("readPathToLines");
        
        Path inpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");//"topOlliHtml.html");
        File f = inpath.toFile();
        assertTrue(f.exists());
        FileUtils instance = new FileUtils();
        List<String> expResult = null;
        List<String> result = new LinkedList<>();
        try
        {
            result.addAll(instance.readPathToLines(inpath));
        } catch (FileNotFoundException ex)
        {
            fail("file not found");
        }
        assertTrue(67 == result.size());
        assertTrue(result.get(1).trim().startsWith("//"));
        
    }
    /**
     * Test of readPathToSBLines method, of class FileUtils.
     */
    /**
     * Test of readPathToSBLines method, of class Utility.
     */
    @Test
    public void testReadPathToSBLines()
    {
        System.out.println("readPathToSBLines");
        
        Path inpath = Paths.get((String) sysprop.get("user.dir"), "test", "net", "dbcrd",
                "DBCUtilLib", "testfiles", "test1.php");
        File f = inpath.toFile();
        assertTrue(f.exists());
        FileUtils instance = new FileUtils();
        
        StringBuilder result = new StringBuilder(100);
        try
        {
            result = instance.readPathToSBLines(inpath);
        } catch (FileNotFoundException ex)
        {
            fail("File Not Found Exception");
        }
        String[] lines = result.toString().split("\n");
        assertTrue(lines.length == 11);
        assertTrue(lines[1].trim().startsWith("//"));
        
    }
    /**
     * Test of readPathToSBSpace method, of class Utility.
     */
    @Test
    public void testReadPathToSBSpace()
    {
        System.out.println("readPathToSBSpace");
        
        Path inpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
                "dbcrd", "DBCUtilLib", "testfiles",
                "test1.php");
        File f = inpath.toFile();
        assertTrue(f.exists());
        FileUtils instance = new FileUtils();
        
        StringBuilder expResult = null;
        StringBuilder result = new StringBuilder(10);
        try
        {
            result = instance.readPathToSBSpace(inpath);
        } catch (FileNotFoundException ex)
        {
            fail("file not found");
        }
        int resultSize = result.length();
        assertTrue(1460 == resultSize);
        Matcher mx = Pattern.compile("$", Pattern.MULTILINE + Pattern.DOTALL).matcher(result);
        int lines = 0;
        while (mx.find())
        {
            lines++;
        }
        assertTrue(lines == 1);
        
    }
//    /**
//     * Test of testbufferedWrite method, of class FileUtils.
//     */
//    @Test
//    public void testTestbufferedWrite() throws FileNotFoundException, IOException
//    {
//        System.out.println("testbufferedWrite");
//        Path inpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
//                "dbcrd", "DBCUtilLib", "testfiles", "test1.php");
//        List<String> content = new FileUtils().readPathToLineStream(inpath).collect(toList());
//        Path dirpath = Paths.get((String) sysprop.get("user.dir"), "test", "net",
//                "dbcrd", "DBCUtilLib", "testfiles");
//        File d = dirpath.toFile();
//        assertTrue(d.exists() && d.isDirectory() && d.canWrite());
//        File tf = File.createTempFile("deleteme", ".txt", d);
//        tf.deleteOnExit();
//        Path tp = tf.toPath();
//        new FileUtils().testbufferedWrite(content, tp);
//        List<String> result = new FileUtils().readPathToLineStream(tp).collect(toList());
//        assertEquals(content, result);
//        tf.delete();
//        
//    }
    static class Jjj
            implements JSONString
    {
        
        Integer key;
        String value;
        
        Jjj(Integer key, String value)
        {
            this.key = key;
            this.value = value;
        }
        
        Jjj(JSONObject jo)
        {
            key = jo.getInt("key");
            value = jo.getString("value");
        }
        
        @Override
        public String toString()
        {
            return new StringBuilder(256).append(key).append("->").append(value).toString();
        }
        
        public Integer getKey()
        {
            return key;
        }
        
        public String getValue()
        {
            return value;
        }
        
        @Override
        public String toJSONString()
        {
            JSONObject jo = new JSONObject()
                    .put("key", key)
                    .put("value", value);
            return jo.toString();
        }
    }

    /**
     * Test of bufferedWrite method, of class FileUtils.
     */
    @Test
    public void testBufferedWrite_Stream_Path()
            throws Exception
    {
        System.out.println("bufferedWrite");
        Stream<String> content = null;
        Path fileP = null;
        FileUtils instance = new FileUtils();
        instance.bufferedWrite(content, fileP);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
