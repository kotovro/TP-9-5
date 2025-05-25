package logic.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import logic.general.Task;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JsonTaskFiller {


    private static class JsonTask {
        public String task;
        public String responsible;
        public String due_date;
    }

    public static List<Task> createTasksList(String tasksJson, int transcriptId) {
        List<Task> tasks = Collections.emptyList();
        try {
            int idx = tasksJson.indexOf('[');
            if (idx == -1) {
                throw new IllegalArgumentException("В ответе не найден JSON-массив");
            }
            String cleanJson = tasksJson.substring(idx);

            ObjectMapper mapper = new ObjectMapper();
            List<JsonTask> list = mapper.readValue(
                    cleanJson,
                    new TypeReference<List<JsonTask>>() {}
            );

            tasks = list.stream()
                    .map(t -> {
                        String desc = String.format(
                                "%s. Дата: %s.",
                                t.task.trim(),
                                t.due_date.trim()
                        );
                        return new Task(transcriptId, desc);
                    })
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException | JsonProcessingException e) {
            System.err.println("Не удалось разобрать задачи: " + e.getMessage());
        }
        return tasks;
    }

}
