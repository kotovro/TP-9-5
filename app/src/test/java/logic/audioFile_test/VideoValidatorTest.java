package logic.audioFile_test;

import logic.audio_extractor.VideoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static logic.audio_extractor.VideoValidator.*;
import static org.junit.jupiter.api.Assertions.*;

class VideoValidatorTest {

    @Test
    @DisplayName("Проверка поддерживаемых форматов")
    void isSupportedVideoFile_shouldReturnTrueForSupportedFormats(@TempDir Path tempDir) throws Exception {
        Path mp4File = tempDir.resolve("test.mp4");
        Path mkvFile = tempDir.resolve("test.mkv");
        Path movFile = tempDir.resolve("test.mov");
        Path aviFile = tempDir.resolve("test.avi");
        Path webmFile = tempDir.resolve("test.webm");

        Files.createFile(mp4File);
        Files.createFile(mkvFile);
        Files.createFile(movFile);
        Files.createFile(aviFile);
        Files.createFile(webmFile);

        assertTrue(isSupportVideoFile(mp4File.toString()), "MP4 файл должен быть поддерживаемым");
        assertTrue(isSupportVideoFile(mkvFile.toString()), "MKV файл должен быть поддерживаемым");
        assertTrue(isSupportVideoFile(movFile.toString()), "MOV файл должен быть поддерживаемым");
        assertTrue(isSupportVideoFile(aviFile.toString()), "AVI файл должен быть поддерживаемым");
        assertTrue(isSupportVideoFile(webmFile.toString()), "WEBM файл должен быть поддерживаемым");
    }

    @Test
    @DisplayName("Проверка не поддерживаемых форматов")
    void isSupportedVideoFile_shouldReturnFalseForUnsupportedFormats(@TempDir Path tempDir) throws Exception {
        Path txtFile = tempDir.resolve("test.txt");
        Path docFile = tempDir.resolve("test.doc");
        Path mp3File = tempDir.resolve("test.mp3");
        Path noExtFile = tempDir.resolve("test");

        Files.createFile(txtFile);
        Files.createFile(docFile);
        Files.createFile(mp3File);
        Files.createFile(noExtFile);

        assertFalse(isSupportVideoFile(txtFile.toString()), "TXT файл не должен быть поддерживаемым");
        assertFalse(isSupportVideoFile(docFile.toString()), "DOC файл не должен быть поддерживаемым");
        assertFalse(isSupportVideoFile(mp3File.toString()), "MP3 файл не должен быть поддерживаемым");
        assertFalse(isSupportVideoFile(noExtFile.toString()), "Файл без расширения не должен быть поддерживаемым");
    }

    @Test
    @DisplayName("Проверка временного файла поддерживаемого формата")
    void isSupportVideoFile_shouldReturnTrueForExistingSupportedFile(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.mp4");
        Files.createFile(testFile);
        assertTrue(VideoValidator.isSupportVideoFile(testFile.toString()),
                "Валидатор должен возвращать true для существующего файла с поддерживаемым форматом");
    }

//    @Test
//    @DisplayName("Проверка существующего файла поддерживаемого формата")
//    void isSupportVideoFile_shouldReturnTrueForRealVideoFile() {
//        Path testVideoPath = Paths.get("src", "test", "resources", "video_test.mp4").toAbsolutePath();
//
//        assertTrue(Files.exists(testVideoPath), "Тестовый видеофайл не найден");
//        assertTrue(VideoValidator.isSupportVideoFile(testVideoPath.toString()),
//                "Валидатор должен возвращать true для реального видеофайла");
//    }

    @Test
    @DisplayName("Проверка не существующего файла")
    void isSupportVideoFile_shouldReturnFalseForNonExistingFile() {
        assertFalse(isSupportVideoFile("nonexistent.mp4"));
    }

    @Test
    @DisplayName("Проверка временного файла неподдерживаемого формата")
    void isSupportVideoFile_shouldReturnFalseForUnsupportedFormat(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.createFile(testFile);
        assertFalse(isSupportVideoFile(testFile.toString()));
    }

    @Test
    @DisplayName("Должен возвращать все поддерживаемые форматы")
    void getSupportedFormats_shouldReturnAllSupportedFormats() {
        String[] formats = getSupportedFormats();
        assertEquals(5, formats.length, "Должно быть 5 поддерживаемых форматов");
        assertArrayEquals(new String[]{".mp4", ".mkv", ".mov", ".avi", ".webm"}, formats,
                "Возвращенные форматы не соответствуют ожидаемым");
    }
}
