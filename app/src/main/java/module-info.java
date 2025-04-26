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

    opens ui to javafx.fxml;
    opens logic.persistence to java.sql, org.xerial.sqlitejdbc;
    opens logic.persistence.dao to java.sql, org.xerial.sqlitejdbc;

    opens logic.audio_extractor to org.bytedeco.javacv.platform,
            org.bytedeco.ffmpeg.platform, org.bytedeco.ffmpeg, org.bytedeco.javacv,
            org.bytedeco.ffmpeg.windows.x86, org.bytedeco.ffmpeg.windows.x86_64;

    exports ui;
}