package net.dbcrd.DBCUtilLib;

import java.util.HashMap;
import java.util.Map;
import net.dbcrd.DBCUtilLib.FormSubmit.EmailFields;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Dan
 */
public class FormSubmitTest {

    public FormSubmitTest() {
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
     * Test of values method, of class FormSubmit.
     */
    @Test
    @Ignore
    public void testValues() {
        System.out.println("values");
        FormSubmit[] expResult = null;
        FormSubmit[] result = FormSubmit.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class FormSubmit.
     */
    @Test
    @Ignore
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        FormSubmit expResult = null;
        FormSubmit result = FormSubmit.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doSubmit method, of class FormSubmit.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDoSubmit() throws Exception {
        System.out.println("doSubmit");
        StringBuilder sb = new StringBuilder(1000);
        StringBuilder sbhtml = new StringBuilder(1000);
        sbhtml.append("<html><body>");
        for (int i = 0; i < 5; i++) {
            sb.append(i).append(": my test message via java   \n");
            sbhtml.append(i).append(": my test message via java   <br>");
        }
        sbhtml.append("</body></html>");
        Map<EmailFields, String> data = new HashMap<>(50);
        data.put(EmailFields.SUBJECT, "my test subject via java no html");
        data.put(EmailFields.MESSAGE, sb.toString());
    data.put(EmailFields.ATTACH, sb.toString());
        data.put(EmailFields.FILENAME, "myfile.txt");
        data.put(EmailFields.HTML, sbhtml.toString());
        FormSubmit instance = FormSubmit.INSTANCE;
        instance.doSubmit( data);

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
