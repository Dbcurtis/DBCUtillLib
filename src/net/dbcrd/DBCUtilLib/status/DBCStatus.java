/*
 * Copyright (c) 2015, Daniel B. Curtis <dbcurtis@dbcrd.net>
 * 
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
package net.dbcrd.DBCUtilLib.status;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "June 2015",
        currentRevision = 1,
        lastModified = "06/09/15",
        copyright = "(C) 2015 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public interface DBCStatus {

    /**
     * Cancels the {@code StatusWorker} triggers the blocking Q, waits for the {@code StatusWorker} to complete, clears the
     * blocking queue, remembers last position of window on display and disposes the dialog.
     */
    void dispose();

    /**
     * Get the text from the status window.
     *
     * @return a String of the text that is being displayed in the status window.
     */
    String getText();

    /**
     * Initializes the progress bars. If you do not call this, phaseProgress and TaksProgress are set to 0-100
     *
     * @param phaseBarVal a Point with x=min val, y=max val for the phase bar
     * @param taskBarVal a Point with x=min val, y=max val for the task bar
     */
    void initProgressBarVals(final Point phaseBarVal, final Point taskBarVal);

    /**
     * Initialize the status window.
     *
     * @param headful a boolean true if the status window is to be displayed, false if not
     *
     */
    // void initialize(final boolean headful);
    /**
     * Resets the status box, makes the task progress bar indeterminate, sets the bar to its minimum value and resets any text in
     * the bar
     */
    void resetTask();

    /**
     * Prepare a note and add to the blocking queue with the specified color. The string to be logged is prepended with the date
     * info
     *
     * @param notein a String to be logged.
     * @param color a java.awt.Color for the color of the displayed text, if not specified it will be BLACK
     */
    void setNote(final String notein, final Color... color);

//    /**
//     * Prepare a note and add to the blocking queue with the color black. The string to be logged is prepended with the date info
//     *
//     * @param notein a String that is the note to be added in black.
//     */
//    void setNote(final String notein);

    /**
     * Sets the phase bar
     *
     * @param val an int that identifies which phase the program is in. see
     * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/BoundedRangeModel.html"><u>
     * Bounded Range Model</u> </a>
     * to understand how val interacts with the maximum and minimum of the phaseProgress bar.
     *
     * @param phaseStringIn a String that is painted in the bar.
     */
    void setPhase(final int val, final String phaseStringIn);

    /**
     * Sets a value in the task bar, makes the bar determinate, and allows text on the bar
     *
     * @param val an int for the value see
     * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/BoundedRangeModel.html">
     * <u>Bounded Range Model</u> </a>
     * to understand how val interacts with the maximum and minimum of the phaseProgress bar.
     */
    void setTask(final int val);

    /**
     * Set the task bar as indeterminate.
     *
     * @param message a String to be displayed in the task bar.
     */
    void setTaskIndeterminate(final String message);

    /**
     * Sets the maximum value for the task bar.
     *
     * @param val an int that sets the maximum for the task bar see
     * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/BoundedRangeModel.html"><u>
     * Bounded Range Model </u></a>
     * to understand how val interacts with the maximum and minimum of the phaseProgress bar.
     */
    void setTaskMax(final int val);

}
