package net.dbcrd.DBCUtilLib.settings;

/*
 * Copyright (c) 2015, Daniel B. Curtis <dbcurtis@dbcrd.net>
 * All rights reserved.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.dbcrd.DBCUtilLib.FileUtils;
import net.dbcrd.DBCUtilLib.status.MyStatus;
import org.json.JSONObject;
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
 * @author Daniel B. Curtis
 */
public class SettingTest {

    private static final Logger LOG = Logger.getLogger(SettingTest.class.getName());

    private static Properties sysProps;
    private static ExecutorService MY_WORKER_POOL;
    private static MyStatus mystatus;

    @BeforeClass
    public static void setUpClass() {
        sysProps = System.getProperties();
        MY_WORKER_POOL = Executors.newCachedThreadPool();
        mystatus = new MyStatus();

    }

    @AfterClass
    public static void tearDownClass() {
        MY_WORKER_POOL.shutdown();
        try {
            MY_WORKER_POOL.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException ignoreInterruptedException) {
        }
        MY_WORKER_POOL.shutdownNow();
    }

    public SettingTest() {
    }

    @Before
    public void setUp() {
        Setting.clear();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getSetting method, of class Setting.
     */
    @Test
    public void testGetSetting() {
        System.out.println("getSetting");
        Setting s = Setting.factory();
        Setting s1 = Setting.factory();
        Setting s2 = Setting.factory();
        Setting s3 = Setting.getSetting("test1");

        String key = s.getName();
        Setting expResult = s;
        Setting result = Setting.getSetting(key);
        assertEquals(expResult, result);
        assertEquals(s.getStype(), Setting.SETTING_TYPE.UND);
        assertEquals("undefined", s.getValue());
        assertFalse(s1.equals(s));

    }

    /**
     * Test of makeAbsolute method, of class Setting.
     */
    @Test
    public void testMakeAbsolute() {
        System.out.println("makeAbsolute");
        Setting sr = Setting.factory("relative", Setting.SETTING_TYPE.TPATH, Paths.get("relPath"));
        Setting sh = Setting.factory("home", Setting.SETTING_TYPE.THOMEDIR, Paths.get("c:\\"));
        final Object sro = sr.getValue();
        assert (sro instanceof Path);
        final Object sho = sh.getValue();
        assert (sho instanceof Path);

        Path srp = (Path) sro;
        Path shp = (Path) sho;

        String srps = srp.toString();
        String shps = shp.toString();
        assertEquals("relPath", srps);
        assertEquals("c:\\", shps);

        Path expResult = Paths.get("c:\\", "relPath");
        Path result = Setting.makeAbsolute(srp);
        assertEquals(expResult, result);

    }

    /**
     * Test of isNotUsed method, of class Setting.
     */
    @Test
    public void testIsNotUsed() {
        System.out.println("isNotUsed");
        Setting s = Setting.factory();
        Setting s1 = Setting.factory();
        Setting s2 = Setting.factory();
        Setting s3 = Setting.getSetting("test1");
        assertTrue(Setting.isNotUsed("junk"));
        assertFalse(Setting.isNotUsed(s.getName()));

    }

    /**
     * Test of removeSetting method, of class Setting.
     */
    @Test
    public void testRemoveSetting() {
        System.out.println("removeSetting");
        Setting s = Setting.factory();
        Setting s1 = Setting.factory();
        Setting s2 = Setting.factory();
        Setting s3 = Setting.getSetting("test1");
        Setting[] settings = {s, s1, s2, s3};
        for (Setting ss : settings) {
            assertFalse(Setting.isNotUsed(ss.getName()));
        }
        Setting.removeSetting(s3);
        for (int i = 0; i < 3; i++) {
            assertFalse(Setting.isNotUsed(settings[i].getName()));
        }
        assertTrue(Setting.isNotUsed(s3.getName()));
        for (Setting ss : settings) {
            Setting.removeSetting(ss);
        }
        for (Setting ss : settings) {
            assertTrue(Setting.isNotUsed(ss.getName()));
        }
    }

    /**
     * Test of clear method, of class Setting.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Setting s = Setting.factory();
        Setting s1 = Setting.factory();
        Setting s2 = Setting.factory();
        Setting s3 = Setting.getSetting("test1");
        assertFalse(Setting.isNotUsed("test1"));
        Setting s4 = Setting.getSetting("test1");
        assertEquals(s3, s4);
        Setting.clear();
        assertTrue(Setting.isNotUsed("test1"));

    }

    /**
     * Test of factory method, of class Setting.
     */
    @Test
    public void testFactory() {
        System.out.println("factory");

        Setting s = Setting.factory();
        Setting s1 = Setting.factory();
        Setting s2 = Setting.factory();
        Setting s3 = Setting.getSetting("test1");
        Setting s4 = Setting.getSetting("test1");
        assertEquals(s3, s4);
        assertTrue(s3.getStype() == Setting.SETTING_TYPE.UND);

        String sn = s.getName();
        String s1n = s1.getName();
        String s2n = s2.getName();

        JSONObject ja1 = new JSONObject(s.toJSONString());
        Setting s5 = Setting.factory(ja1);
        s = Setting.factory("testname", Setting.SETTING_TYPE.UND, "junk");
        String sstr = s.toString();

        assertEquals("testname:UND-junk", sstr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype. and still needs more work");
    }

    /**
     * Test of catalog method, of class Setting.
     */
    @Test
    public void testCatalog() {
        System.out.println("catalog");
        Setting instance = Setting.factory();
        String key1 = instance.getName();
        instance.setName("funny");

        instance.catalog();
        assertTrue(Setting.isNotUsed(key1));
        assertFalse(Setting.isNotUsed("funny"));

    }

    /**
     * Test of getName method, of class Setting.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Setting instance = Setting.factory("testname", Setting.SETTING_TYPE.UND, new Object());
        String expResult = "testname";
        String result = instance.getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of setName method, of class Setting.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");

        Setting instance = Setting.factory();
        String oldName = instance.getName();
        assertFalse(Setting.isNotUsed(oldName));
        String name = "newName";
        instance.setName(name);
        assertEquals(name, instance.getName());
        assertTrue(Setting.isNotUsed(oldName));
        assertFalse(Setting.isNotUsed(name));

    }

    /**
     * Test of getStype method, of class Setting.
     */
    @Test
    public void testGetStype() {
        System.out.println("getStype && setStype");
        Setting instance = Setting.factory();
        Setting.SETTING_TYPE expResult = Setting.SETTING_TYPE.UND;
        Setting.SETTING_TYPE result = instance.getStype();
        assertEquals(expResult, result);

        instance.setStype(Setting.SETTING_TYPE.TSTRING);
        instance.setValue("text");
        instance.setName("test");

        Setting s = Setting.getSetting("test");
        JSONObject jo = new JSONObject(s.toJSONString());
        Setting.removeSetting(s);
        assertTrue(Setting.isNotUsed("test"));

        Setting ss = Setting.factory(jo);
        assertFalse(Setting.isNotUsed("test"));
        assertEquals(Setting.SETTING_TYPE.TSTRING, ss.getStype());

    }

    /**
     * Test of setStype method, of class Setting.
     */
    @Test
    public void testSetStype() {
        System.out.println("setStype");
        Setting.SETTING_TYPE stype;
        Setting instance = Setting.getSetting("test");
        stype = instance.getStype();
        assertEquals(Setting.SETTING_TYPE.UND, stype);

        instance.setStype(Setting.SETTING_TYPE.TSTRING);
        instance.setValue("I am a String");
        String result = instance.toString();
        String expected = "test:TSTRING-I am a String";
        assertEquals(expected, result);

    }

    /**
     * Test of getValue method, of class Setting.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Setting instance = null;
        Object expResult = null;
        Object result = instance.getValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class Setting.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        Object value = null;
        Setting instance = null;
        instance.setValue(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toJSONString method, of class Setting.
     */
    @Test
    public void testToJSONString() {
        System.out.println("toJSONString");
        Setting s = Setting.factory();
        s.setStype(Setting.SETTING_TYPE.TPATH);
        s.setName("mytest");
        String userFile = System.getProperty("user.dir");

        s.setValue(Paths.get(userFile));
        String jResult = s.toJSONString();
        String jExpected = "{\"stype\":\"TPATH\",\"name\":\"mytest\",\"value\":\"J:\\\\Java\\\\DBCUtilLib\"}";
        assertEquals(jExpected, jResult);

        s.setValue(Setting.SETTING_TYPE.TPATH, Paths.get(userFile));
        jResult = s.toJSONString();
        assertEquals(jExpected, jResult);

    }

    /**
     * Test of toString method, of class Setting.
     */
    @Test

    public void testToString() {
        System.out.println("toString");
        Setting.SETTING_TYPE stype;
        Setting instance = Setting.getSetting("test");
        stype = instance.getStype();
        assertEquals(Setting.SETTING_TYPE.UND, stype);

        instance.setStype(Setting.SETTING_TYPE.TSTRING);
        instance.setValue("I am a String");
        String result = instance.toString();
        String expected = "test:TSTRING-I am a String";
        assertEquals(expected, result);
    }

    /**
     * Test of equals method, of class Setting.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Setting s = Setting.factory();
        Setting s1 = Setting.factory();
        Setting s2 = Setting.factory();
        Setting s3 = Setting.getSetting("test1");
        Setting s4 = Setting.getSetting("test1");

        Object o = null;
        Setting instance = s;
        assertFalse(s.equals(o));
        o = new Object();
        assertFalse(s.equals(o));
        assertFalse(s.equals(s1));
        assertEquals(s3, s4);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype. need more tests");
    }

    /**
     * Test of hashCode method, of class Setting.
     */
    @Test
    @Ignore
    public void testHashCode() {
        System.out.println("hashCode");
        Setting instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setupFile System and setStatus
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test
    public void testSetupFileSystem() throws URISyntaxException, IOException {
        System.out.println("SetStatus(not working) and setupFileSystem");

        final Path testPath = Paths.get(sysProps.getProperty("user.dir"), "test", "net", "dbcrd",
                "DBCUtilLib", "testfiles", "settingsTestFolder");
        try {
            FileUtils.deleteRecursive(testPath);
        } catch (FileNotFoundException ignore) {
        }

        Setting s1 = Setting.factory("test", Setting.SETTING_TYPE.TSTRING, "TestSetupFileSystem Settings");
        System.out.println("should get error messages via THE_LOGGER");
        Setting.setupFileSystem();
        //Setting.SetStatus(status);
        // System.out.println("should get error messzges via Status");
        //  Setting.setupFileSystem();
        Setting ht = Setting.factory("settingsTestFolder", Setting.SETTING_TYPE.THOMEDIR, testPath);
        Setting.setupFileSystem();
        assertTrue(Files.exists(testPath));
        Files.deleteIfExists(testPath);  // delete temp dir again
        Setting s2 = Setting.factory("testdir1", Setting.SETTING_TYPE.TDIR, Paths.get("dir1"));
        Setting s3 = Setting.factory("testdir2", Setting.SETTING_TYPE.TDIR, Paths.get("dir2"));
        Setting.setupFileSystem();
        assertTrue(Files.exists(testPath));
        assertTrue(Files.exists(Setting.makeAbsolute(s2.getValue())));
        assertTrue(Files.exists(Setting.makeAbsolute(s3.getValue())));
        FileUtils.deleteRecursive(testPath);
        Path outputdata = Paths.get("dir1", "test1.txt");
        Setting s4 = Setting.factory("testfile1", Setting.SETTING_TYPE.TPATH, outputdata);
        Setting.setupFileSystem();
        Path p = Setting.makeAbsolute(Setting.getSetting("testfile1").getValue());
        new FileUtils().bufferedWrite("this is text for test1.txt", p);
        assertTrue(Files.exists(Setting.makeAbsolute(s4.getValue())));
        Setting.setupFileSystem();
        assertTrue(Files.exists(Setting.makeAbsolute(s4.getValue())));
        try {
            FileUtils.deleteRecursive(testPath);
        } catch (FileNotFoundException ignore) {
        }
    }

    /**
     * Test of setValue method, of class Setting.
     */
    @Test
    public void testSetValue_SettingSETTING_TYPE_Object() {
        System.out.println("setValue");
        final Path testPath = Paths.get(sysProps.getProperty("user.dir"), "test", "net", "dbcrd",
                "DBCUtilLib", "testfiles", "settingsTestFolder");

        Setting s1 = Setting.factory();
        assertEquals(Setting.SETTING_TYPE.UND, s1.getStype());
        assertEquals("undefined", s1.getValue());

        s1.setValue(Setting.SETTING_TYPE.TSTRING, "a String");
        assertEquals(Setting.SETTING_TYPE.TSTRING, s1.getStype());
        assertEquals("a String", s1.getValue());

        File f = testPath.toFile();
        s1.setValue(Setting.SETTING_TYPE.TSTRING, f);
        Object o = s1.getValue();
        assertTrue(o instanceof String);
        assertEquals(f.toString(), o);

        s1.setValue(Setting.SETTING_TYPE.TFILE, f);
        o = s1.getValue();
        assertTrue(o instanceof Path);
        assertEquals(f, ((Path) o).toFile());

        s1.setValue(Setting.SETTING_TYPE.TPATH, f);
        o = s1.getValue();
        assertTrue(o instanceof Path);
        assertEquals(f.toPath(), o);

    }

    /**
     * Test of initialize method, of class Setting.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        ExecutorService mwp = MY_WORKER_POOL;
        MyStatus status = mystatus;
        Class<?> package4Prefs = SettingTest.class;
        Setting.initialize(mwp, status, package4Prefs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class Setting.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        MyStatus status = null;
        Setting.setStatus(status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class Setting.
     */
    @Test
    @Ignore
    public void testSetValue_Object() {
        System.out.println("setValue");
        Object value = null;
        Setting instance = null;
        instance.setValue(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSETTINGS method, of class Setting.
     */
    @Test
    public void testGetSETTINGS() {
        System.out.println("getSETTINGS");
        List<Setting> expResult = null;
        List<Setting> result = Setting.getSETTINGS();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLastHomePath method, of class Setting.
     */
    @Test
    public void testSetLastHomePath() {
        System.out.println("setLastHomePath");
        Path aLastHomePath = null;
        Setting.setLastHomePath(aLastHomePath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
