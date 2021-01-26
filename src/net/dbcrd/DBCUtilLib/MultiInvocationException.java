package net.dbcrd.DBCUtilLib;

import java.util.logging.Logger;

/**
 * <Strong>An exception that is thrown by Limit21Eexecutable if another instance of the program is executing.</strong>
 * @author Daniel B. Curtis (dbcurtis@dbcrd.net)
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 1,
        lastModified = "8/18/2013",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public class MultiInvocationException extends Exception {
    /* the logoger */

    /**
     *
     */

    private static final Logger LOG = Logger.getLogger(MultiInvocationException.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 0x98ca9dfc96af9dc6L;

    /**
     *  Constructor for the class
     */
    public MultiInvocationException() {
        super();

    }

    /**
     *
     * @param message a String that is the error message
     */
    public MultiInvocationException(String message) {
        super(message);

    }

    /**
     *
     * @param message a String that is the error message
     * @param cause a Throwable that caused the exception
     */
    public MultiInvocationException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     *
     * @param cause a Throwable that caused the exception
     */
    public MultiInvocationException(Throwable cause) {
        super(cause);

    }

}
