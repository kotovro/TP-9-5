package logic.text_edit;

import logic.general.Replica;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/// here we can store some info that was acquired via ctrl+x/ctrl+v
public class Buffer  {
    public static Replica bufferedReplica = new Replica("");
    public static String bufferedString = "";
    public static Clipboard clipboardForString = Toolkit.getDefaultToolkit().getSystemClipboard(); //support for system clipboard

    public static void setString(String selectedString) {
        StringSelection stringSelection = new StringSelection(selectedString);
        clipboardForString.setContents(stringSelection, null);
        bufferedString = selectedString;
    }

    public static void setReplica(Replica replica) {
        bufferedReplica = replica;
    }

    public static Replica getReplica() {
        return bufferedReplica;
    }

    public static String getString() throws IOException, UnsupportedFlavorException {
        Transferable contents = clipboardForString.getContents(null);
        String resultText = "";

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) { //check if clipboard contains a string
            resultText = (String) contents.getTransferData(DataFlavor.stringFlavor);
        } else {
            resultText = bufferedString;
        }
        return resultText;
    }

}
