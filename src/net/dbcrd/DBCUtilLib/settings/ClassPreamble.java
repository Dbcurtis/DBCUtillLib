
package net.dbcrd.DBCUtilLib.settings;

import net.dbcrd.DBCUtilLib.*;
import java.lang.annotation.Documented;



/**
 *
 * @author dbcurtis
 */
@Documented
public @interface ClassPreamble {

    /**
     *
     * @return a String
     */
    String author() default "Dan Curtis";

    /**
     *
     * @return a String of the date
     */
    String date();

    /**
     *
     * @return an int of the curren revision
     */
    int currentRevision() default 1;

    /**
     *
     * @return a String of the date last modified
     */
    String lastModified() default "N/A";

    /**
     *
     * @return a String of the name of the one who last modified
     */
    String lastModifiedBy() default "N/A";

    /**
     *
     * @return a string of the copyright notice.
     */
    String copyright() default "(C) 2009-2010, Daniel B. Curtis, all rights reserved.";

}

