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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;



/**
 *
 * @author dbcurtis
 */
public class HTMLTagContentTest
{
    
    private static final Logger LOG = Logger.getLogger(HTMLTagContentTest.class.getName());
    
  final  private static String TEST_DATA =
            
             
"<div class=\"singleColumn\">"
+" <h2>System Alerts</h2>"
+" <ul class=\"subnav\">"
+" <li><a href=\"#active\">Active Alerts</a></li>"
+" <li><a href=\"#InformationAlerts\">Informational Alerts</a></li>"
+" <li><a href=\"#nonactive\">Non-Active Alerts</a></li>"
+" </ul>"
+" <a name=\"active\">"
+" <div class=\"sectionHeadBox\">"
+" <h3 class=\"sectionHeadText\">Active Alerts</h3>"
+" </div>"
+" <div class=\"text\">"
+" <a name=\"1330\"/>"
+" 03/29/2017<br/>"
+" <strong>Daily Transaction Files Delayed</strong><br/>"
+" The Daily Transaction Files for the ULS databases were not created. We are working on the problem and"
+" expect the files to be available later today."
+" <br/>"
+" </div>"
+" <div class=\"backToTop\"><img src=\"http://wireless.fcc.gov/images/arrow-up.gif\" alt=\"Return to Top Arrow\" border=\"0\">"
            +"<a href=\"#top\">Return to Top</a></div>"
+" <a name=\"InformationAlerts\">"
+" <div class=\"sectionHeadBox\">"
+" <h3 class=\"sectionHeadText\">Informational Alerts</h3>"
+" </div>"
+" <div class=\"text\">No information alerts</div>"
+" <br/>"
+" <div class=\"backToTop\"><img src=\"http://wireless.fcc.gov/images/arrow-up.gif\" alt=\"Return to Top Arrow\" border=\"0\">"
            + "<a href=\"#top\">Return to Top</a></div>"
+" <a name=\"nonactive\">"
+" <div class=\"sectionHeadBox\">"
+" <h3 class=\"sectionHeadText\">Non-Active Alerts</h3>"
+" </div>"
+" <div class=\"text\">"
+" <a name=\"1329\"/>"
+" 03/28/2017<br/>"
+" <strong> Internal Universal Licensing System (ULS) System Down for Scheduled Maintenance</strong><br/>"
+" Internal Universal Licensing System (including Application Search, License Search, License Manager, Tower"
            +" Construction Notification System, Antenna Structure Registration, and Ownership Information Search) will"
            +" be down for scheduled maintenance starting at 6:00 PM ET on Tuesday, March 28, 2017 until 8:00 PM ET Tuesday,"
            +" March 28, 2017."
+"We apologize for any inconvenience. "

+" <br/>"
+" </div>"
+" <div class=\"text\">"
+" <a name=\"1328\"/>"
+" 03/23/2017<br/>"
+" <strong>Daily Transaction Files Delayed</strong><br/>"
+"The Daily Transaction Files for the ULS databases were not created."
            +" We are working on the problem and expect the files to be available later today."

+" <br/>"
+" </div>"
+" <div class=\"text\">"
+" <a name=\"1327\"/>"
+" 03/22/2017<br/>"
+" <strong>Universal Licensing System (ULS) System Down for Scheduled Maintenance</strong><br/>"
+"The Universal Licensing System (including Application Search, License Search, License Manager,"
            +" Tower Construction Notification System, Antenna Structure Registration, and Ownership"
            +" Information Search) will be down for scheduled maintenance starting at 6:00 AM ET on"
            +" Thursday, March 23, 2017 until 8:00 AM ET Thursday, March 23, 2017."
+"During the down time, VECs, COLEMs, Land Mobile, and Microwave batch filers can still send"
            +" and retrieve files, but the files will not be processed until maintenance is completed."
+"Public Access files will be posted after the maintenance is completed. "
+"We apologize for any inconvenience. "

+" <br/>"
+" </div>"
+" <div class=\"text\">"
+" <a name=\"1326\"/>"
+" 03/20/2017<br/>"
+" <strong>Daily Transaction Files Delayed</strong><br/>"
+"The Daily Transaction Files for the ULS databases were not created."
            +" We are working on the problem and expect the files to be available later today."

+" <br/></div>";

    /**
     *
     */
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    /**
     *
     */
    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     *
     */
    public HTMLTagContentTest()
    {
    }
    
    /**
     *
     */
    @Before
    public void setUp()
    {
    }
    
    /**
     *
     */
    @After
    public void tearDown()
    {
    }

    /**
     * Test of equals method, of class HTMLTagContent.
     */
    @Test
    public void testEquals()
    {
        System.out.println("* HTMLTagContent: equals");
        Object o = null;
        HTMLTagContent i1 = new HTMLTagContent("strong");
       HTMLTagContent i2 = new HTMLTagContent("strong");
        HTMLTagContent i3 = new HTMLTagContent("div");
        assertNotEquals(i1,null);
        assertNotEquals(i1, new Object());
        boolean expResult = false;
    
        assertEquals(i1, i1);
       assertSame(i1,i1);
        assertNotSame(i1,i2);
        assertEquals(i1,i2);
        
        assertNotEquals(i1,i3);
       

    }
    /**
     * Test of equals method, of class HTMLTagContent.
     */
    @Test
    public void testGetMatcher()
    {
        System.out.println("* HTMLTagContent: matcher");
        
        StringBuilder sb = new StringBuilder(TEST_DATA);  
        try{
            HTMLTagContent i1 = new HTMLTagContent("  ");
            fail("failed empty tag");
        }catch (IllegalArgumentException iae){
            
        }
        try{
            HTMLTagContent i1 = new HTMLTagContent("aa vv");
            fail("failed non alpha only tag");
        }catch (IllegalArgumentException iae){
            
        }
        
        int idx = sb.lastIndexOf(">Active Alerts</h3>");       
        HTMLTagContent htc1 = new HTMLTagContent("strong");
        HTMLTagContent htc2 = new HTMLTagContent("h3");
        boolean expResult = false;
        Matcher mxStrong = htc1.matcher.get();
        mxStrong.reset(sb);
        assertTrue(mxStrong.find(idx));
        String g = mxStrong.group();
        assertEquals("<strong>Daily Transaction Files Delayed</strong>",g);
        mxStrong.reset(sb);
        List<String> strongs = new ArrayList<>(100);
        while(mxStrong.find()){
            strongs.add(mxStrong.group());
        }
        assertEquals(5,strongs.size());
        assertEquals("<strong>Universal Licensing System (ULS) System Down for Scheduled Maintenance</strong>", strongs.get(3));
        Matcher mxH3 = htc2.matcher.get();
        mxH3.reset(sb);
        List<String> h3s = new ArrayList<>(100);
        while(mxH3.find()){
            h3s.add(mxH3.group());
        }
        assertEquals(3, h3s.size());
        assertEquals("<h3 class=\"sectionHeadText\">Informational Alerts</h3>", h3s.get(1));
        
    }

    /**
     * Test of hashCode method, of class HTMLTagContent.
     */
    @Test
    @Ignore
    public void testHashCode()
    {
        System.out.println("* HTMLTagContent: hashCode");
        HTMLTagContent instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class HTMLTagContent.
     */
    @Test
    public void testToString()
    {
        System.out.println("* HTMLTagContent: toString");
        HTMLTagContent i1 = new HTMLTagContent("strong");
     
        String result = i1.toString();
        assertEquals("strong", result);
 
    }
    
}
