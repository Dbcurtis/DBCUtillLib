package net.dbcrd.DBCUtilLib;

/**
 *
 * @author Daniel B. Curtis
 */
@net.dbcrd.DBCUtilLib.ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Sept 2013 ",
        currentRevision = 3,
        lastModified = "9/29/2013",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)

public class UserAbortException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * the constructor
     */
    public UserAbortException() {
        super();

    }

    /**
     *
     * @param message a String
     */
    public UserAbortException(final String message) {
        super(message);

    }

    /**
     *
     * @param message a String
     * @param cause a Throwable
     */
    public UserAbortException(final String message, final Throwable cause) {
        super(message, cause);

    }

    /**
     *
     * @param cause a Throwable
     */
    public UserAbortException(final Throwable cause) {
        super(cause);

    }

   
}
