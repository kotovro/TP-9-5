module vsrecheslav {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.xml.crypto;
    requires org.bytedeco.javacv;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires vosk;
    requires java.sql;

    exports logic.general;
    exports logic.text_edit;
    exports ui;

    opens ui to javafx.fxml;
    opens fx_screens to javafx.fxml;
    exports ui.custom_elements;
    opens ui.custom_elements to javafx.fxml;
}