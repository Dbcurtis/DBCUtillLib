package net.dbcrd.DBCUtilLib;

/**
 * <strong>An Exception that is thrown when some characteristic that is assumed to be true for the system is false</strong>
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Sept 2013 ",
        currentRevision = 3,
        lastModified = "9/29/2013",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)

public class SystemNotAsExpectedException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * the constructor
     */
    public SystemNotAsExpectedException() {
        super();

    }

    /**
     *
     * @param message
     */
    public SystemNotAsExpectedException(final String message) {
        super(message);

    }

    /**
     *
     * @param message
     * @param cause
     */
    public SystemNotAsExpectedException(final String message, final Throwable cause) {
        super(message, cause);

    }

    /**
     *
     * @param cause
     */
    public SystemNotAsExpectedException(final Throwable cause) {
        super(cause);

    }

}
