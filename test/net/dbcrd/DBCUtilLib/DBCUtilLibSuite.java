/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dbcrd.DBCUtilLib;

import net.dbcrd.DBCUtilLib.settings.SettingsSuite;
import net.dbcrd.DBCUtilLib.status.StatusSuite;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Dan
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AntiHangTest.class, StopWatchTest.class, FileUtilsTest.class, GetSystemInfoTest.class, StatusSuite.class,
            WaitMessageTest.class, WindowPlacementUtilTest.class, MyFilesTest.class, OSProcessTest.class,
            GetLinuxDriveCSNumTest.class, FormSubmitTest.class, LambdasTest.class, LoggerSetupTest.class,
            MultiInvocationExceptionTest.class, Limit21executableTest.class, ForwardingSetTest.class, SettingsSuite.class,
            TempFileRegistryTest.class, GetWindowsDriveCSNumTest.class, TextTransferTest.class, 
            BareBonesBrowserLaunchTest.class, SystemNotAsExpectedExceptionTest.class, CheckInternetExistsTest.class,
            UserAbortExceptionTest.class, HTMLTagContentTest.class, DosCmdTest.class})
public class DBCUtilLibSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
