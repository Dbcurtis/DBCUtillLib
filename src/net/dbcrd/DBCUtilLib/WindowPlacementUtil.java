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
 */package net.dbcrd.DBCUtilLib;

import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <strong> Utility to remember where a window was located and to restore the window 
 * to the same location when instantiated</strong>
 *
 * @param <T> is the class of the JFrame subclass
 *
 * Calling sequence is:
 *
 * <pre>{@code private final WindowPlacementUtil<T> wpu; }</pre>where T is the class of the JFrame subclass
 * in the constructor for the JFrame subclass something like:
 * <pre>{@code
 * super();
 * initComponents();
 * wpu=new WindowPlacementUtil<>(this,ORG);
 * }</pre>
 * in the dispose for the JFrame subclass use:
 * <pre>{@code
 * (AT)Override
 * public void dispose(){
 *  wpu.rememberPosition();
 *  super.dispose();
 * }
 * }</pre>
* 

 * Just before the setVisable(true) on the AWT thread do
 * <pre>{@code wpu.getRememberedPosition(); }</pre>---- it also does a pack();
 * it will put the frame where it was located when last disposed, but if something goes wrong, will put it
 * in the center of the screen with a setLocationRelativeTo(null)
 *
 */
@ClassPreamble(author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 2,
        lastModified = "07/21/2015",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis")

public final class WindowPlacementUtil<T extends javax.swing.JFrame> {

    /** holds "0,0" */
    private final static String ZEROZERO = "0,0";

    /** Pattern to recognize the x position */
    private static final Pattern XPOS_PAT = Pattern.compile("(?<=x=)\\d*");

    /** Pattern to recognize the y position  */
    private static final Pattern YPOS_PAT = Pattern.compile("(?<=y=)\\d*");

    /** The Logger */
    private static final Logger THE_LOGGER = Logger.getLogger(WindowPlacementUtil.class.getName());

    /** the organization */
    private final String org;

    /** the preferences */
   // private Preferences prefs;

    /**
     * TBD
     */
    private T jframe;

    /**
     *
     * @param jframe a <pre>{@code <T>}</pre> that is some form of <pre>{@code javax.swing.JFrame}</pre>
     * @param org a String that is an identifier for the 
     */
    public WindowPlacementUtil(T jframe, String org) {

        this.jframe = jframe;
        Preferences prefs;
        try {
            prefs = Preferences.userNodeForPackage(jframe.getClass());
        }
        catch (SecurityException sex) {
            THE_LOGGER.log(Level.WARNING,"Cannot Save Preferences",sex);
            prefs = null;
        }
        this.org = org;
    }

    /**
     * Recall the remembered position and set the window to that position, if there is no remembered position
     * Place the window in the middle of the display.  org is the key used to retrieve the position.
     */
    public void getRememberedPosition() {
     //   if (null != prefs) {
            jframe.pack();
            String xyloc;// = ZEROZERO;
            jframe.setLocationRelativeTo(null);

            xyloc = Preferences.userNodeForPackage(jframe.getClass()).get(org, ZEROZERO);
            if (ZEROZERO.equals(xyloc)) {
                final Rectangle bounds = jframe.getBounds();
                Preferences.userNodeForPackage(jframe.getClass()).put(org, bounds.getLocation().toString());
                try {
                    Preferences.userNodeForPackage(jframe.getClass()).sync();
                }
                catch (BackingStoreException ex) {
                    THE_LOGGER.log(Level.SEVERE, "setPosition", ex);
                }
            }
            final String topleftstr = Preferences.userNodeForPackage(jframe.getClass()).get(org, ZEROZERO);
            final Matcher matcherX = XPOS_PAT.matcher(topleftstr);
            final Matcher matcherY = YPOS_PAT.matcher(topleftstr);
            try {
                final boolean mxOK = matcherX.find();
                final boolean myOK = matcherY.find();
                if (mxOK && myOK) {
                    jframe.setLocation(
                            Integer.parseInt(matcherX.group()),
                            Integer.parseInt(matcherY.group()));
                }
            }
            catch (NumberFormatException nfe) {
                jframe.setLocationRelativeTo(null);
            }
      //  }
    }

    /**
     *  Remember the current position of the frame.
     */
   
    public void rememberPosition() {
      //  if (null != prefs) {
            if (jframe != null) {
                final Rectangle bounds = jframe.getBounds();

                Preferences.userNodeForPackage(jframe.getClass()).put(org, bounds.getLocation().toString());
                try {
                    Preferences.userNodeForPackage(jframe.getClass()).sync();
                }
                catch (BackingStoreException ex) {
                    THE_LOGGER.log(Level.SEVERE, null, ex);
                }

                jframe = null;
                //prefs = null;
            }
       // }
    }
}
