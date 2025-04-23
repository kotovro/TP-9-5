package logic.text_edit;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/// here we can store some info that was acquired via ctrl+x/ctrl+v
public class TextBuffer {
    private static final Clipboard clipboardForString = Toolkit.getDefaultToolkit().getSystemClipboard();

    public static boolean isEmpty() {
        try {
            Transferable contents = clipboardForString.getContents(null);
            return (contents == null) || !contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return true;
        }
    }

    public static void setValue(String selectedString) {
        StringSelection stringSelection = new StringSelection(selectedString);
        clipboardForString.setContents(stringSelection, null);
    }

    public static String getValue() {
        try {
            return (String) clipboardForString.getContents(null).getTransferData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
