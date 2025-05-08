package logic.transcriptExporter_test;

import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.utils.TranscriptExporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
public class TranscriptExportTest {
    @Test
    @DisplayName("Успешное сохранение стенограммы")
    void exportTranscript(@TempDir Path tempDir) throws IOException
    {
        ///TODO: вместо такого теста сделать соответствие стенограммы некотрому формату
//        Replica r1 = new Replica();
//        r1.setText("Как же круто использовать Docker!.\n С его помощью многократно упрощается работа с зависимостями, он позвоялет запускать программы" +
//                "вне зависимости от имеющегося окружения на конкретной машине, и легче виртальных машин!\n Контейнры - это круто!");
//        r1.setSpeaker(new Speaker("Инженер", null, 228));
//
//        Replica r2 = new Replica();
//        r2.setText("Звучит неплохо, но я бы посмотрел на конкретные данныt и примеры, в которых контеёнеризация задет большой выигрыш. Возможно, нам надо  будет внердрит это на все проекты.\n");
//        r2.setSpeaker(new Speaker("Руководитель", null, 228));
//
//        Replica r3 = new Replica();
//        r3.setText("Хорошо, после встречи подготволю соответсвующий документ");
//        r3.setSpeaker(new Speaker("Инженер", null, 2022));
//
//        Date testDate = new Date();
//        Transcript testTranscript = new Transcript("Пример", testDate);
//        testTranscript.addReplica(r1);
//        testTranscript.addReplica(r2);
//        testTranscript.addReplica(r3);
//
//        Path txtFile = tempDir.resolve("test.txt");
//        Files.createFile(txtFile);
//        StringBuilder sb = TranscriptExporter.exportToTextFile(testTranscript, txtFile.toString());
//        try {
//            String content = Files.readString(txtFile.toAbsolutePath());
//            assertEquals(sb.toString(), content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
