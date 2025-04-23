module vsrecheslav {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.sql;
    requires java.desktop;
    requires java.xml.crypto;

    exports logic.general;
    exports logic.text_edit;
    exports ui;

    opens ui to javafx.fxml;
    opens fx_screens to javafx.fxml;
}