/**
 * Package for providing status information
 */
package net.dbcrd.DBCUtilLib.status;
/**
 * Provides ability to generate an html display for the user, and to have that display printed using a browser after the
 * sourcing progam ends.
 *  <p>
 * Both headless and headfull options exist.
 * 
 * The notes are searlized via a blocking queue processed by a status worker that can be started like:<br />
 * {@code StatusWorker<Object> statusWorker = Status.getMyStatus().new StatusWorker<>();}
 * 
 * 
 */

