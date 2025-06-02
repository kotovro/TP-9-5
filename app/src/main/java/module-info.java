module vstrecheslav {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.xml.crypto;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires vosk;
    requires org.xerial.sqlitejdbc;

    requires org.bytedeco.javacv.platform;
    requires org.bytedeco.ffmpeg.platform;
    requires org.bytedeco.ffmpeg;
    requires org.bytedeco.javacv;
    requires org.bytedeco.ffmpeg.windows.x86;
    requires org.bytedeco.ffmpeg.windows.x86_64;
    requires org.bytedeco.ffmpeg.macosx.arm64;
    requires org.bytedeco.ffmpeg.macosx.x86_64;
    requires llama;

    opens ui to javafx.fxml;
    opens ui.custom_elements to javafx.fxml;
    opens ui.custom_elements.combo_boxes to javafx.fxml;
    opens ui.main_panes to javafx.fxml;
    opens styles to javafx.fxml;

    opens logic.persistence to java.sql, org.xerial.sqlitejdbc;
    opens logic.persistence.dao to java.sql, org.xerial.sqlitejdbc;

    opens logic.video_processing.audio_extractor to org.bytedeco.javacv.platform,
            org.bytedeco.ffmpeg.platform, org.bytedeco.ffmpeg, org.bytedeco.javacv,
            org.bytedeco.ffmpeg.windows.x86, org.bytedeco.ffmpeg.windows.x86_64,
            org.bytedeco.ffmpeg.macosx.arm64, org.bytedeco.ffmpeg.macosx.x86_64;

    exports ui;
    exports ui.custom_elements;
    exports ui.custom_elements.combo_boxes;
    exports ui.main_panes;
}