/*
 * Copyright (c) 2015, Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 * All rights reserved.
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
package net.dbcrd.DBCUtilLib;

import java.awt.Frame;
import java.awt.Window;

/**
 * <strong>
 * A singleton to stop java from never exiting.</strong>
 * <p> The code in the finally shuts down the thread pool, and kills any hanging windows
 * that may stopping the JVM from a timely exit.</p>
 * <pre>
 * {@code
 *    try
 *            {
 *               doit() // do the application code
 *            } catch (MultiInvocationException ex)
 *            {
 *            }
 *             finally
 *            {
 *              shutDownTheThreadPool();
 *              AntiHang.doit();      // needs to be the last statment of the program
 *           }
 * }
 * </pre>
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 1,
        lastModified = "8/18/2013",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
        )
public enum AntiHang {

    /**
     * The Singleton
     */
     INSTANCE;

    /**
     * Kill any left over windows/frames that will keep the VM running
     */
    public static void doit() {
        /**
         * The following code is needed to kill any left over windows/frames which will
         * keep the VM running.
         * This kills those suckers and to hell with them (I have spent months trying to figure this out)
         */
        java.awt.EventQueue.invokeLater(() -> {
            for (Frame frm : Frame.getFrames()) {
                frm.removeAll();
                frm.dispose();
            }
            for (Window win : Window.getOwnerlessWindows()) {
                win.dispose();
            }
        });
    }

}
