package net.dbcrd.DBCUtilLib;

/**
 *
 * @author Dan
 */
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <strong> Transfer text to and from the clipboard.</strong>
 *
 * @author Dan
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 1,
        lastModified = "07/20/2015",
        copyright = "none",
        lastModifiedBy = "Daniel B. Curtis"
)
public final class TextTransfer
        implements ClipboardOwner
{

    /**
     *
     */
    private static final Logger THE_LOGGER = Logger.getLogger(TextTransfer.class.getName());

    /**
     * Empty implementation of the ClipboardOwner interface.
     *
     * @param aClipboard a Clipboard
     * @param aContents  a Transferable
     */
    @Override
    public void lostOwnership(Clipboard aClipboard, Transferable aContents)
    {
        //do nothing
    }

    /**
     * Place a String on the clipboard, and make this class the owner of the Clipboard's contents.
     *
     * @param aString a String to put in the clipboard
     */
    public void setClipboardContents(String aString)
    {
        final StringSelection stringSelection = new StringSelection(aString);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an empty String.
     */
    public String getClipboardContents()
    {
        String result = "";
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        final Transferable contents = clipboard.getContents(null);
        final boolean hasTransferableText =
                (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.stringFlavor);

        if (hasTransferableText)
        {
            try
            {
                if (contents != null)
                {
                    result = (String) contents.getTransferData(DataFlavor.stringFlavor);
                }
            } catch (UnsupportedFlavorException ex)
            {
                //highly unlikely since we are using a standard DataFlavor
                THE_LOGGER.log(Level.SEVERE, "Unsupported Flavor", ex);
            } catch (IOException ex)
            {
                THE_LOGGER.log(Level.SEVERE, "get transfer error", ex);
            }
        }
        return result;
    }
}
