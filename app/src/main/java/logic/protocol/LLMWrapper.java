package logic.protocol;

import logic.general.Protocol;
import logic.general.Replica;
import logic.general.Task;
import logic.general.Transcript;

import java.util.ArrayList;
import java.util.List;

public class LLMWrapper {
    private LLMService service = null;

    public List<Task> getTasks(Transcript transcript) {
        List<Task> tasks = new ArrayList<>();
        StringBuilder textSB = new StringBuilder();
        for (Replica replicas: transcript.getReplicas()) {
            textSB.append(replicas.getText());
        }
        String inputText = textSB.toString();
        String prompt = String.format("""
        Ты — русский языковой помощник для анализа текстов.
        Извлеки ТОП‑5 самых важных задач из текста ниже.
        Если задач больше пяти — выбери ключевые, остальные проигнорируй.
        
        Формат для каждой задачи (каждая задача — новая строка):
        1. [Суть задачи, 10–20 слов]. Ответственный: [имя/должность или «не указан»]. Метки: [если есть], Срок: [дата или «не указан»].
        
        Пример:
        1. Запуск MVP нейроинтерфейсов для ритейла. Ответственный: отдел R&D. Метки: ИИ, Срок: 29.09.2025.
        2. Разработка API для мобильного приложения. Ответственный: команда backend. Метки: REST, Срок: 15.06.2025.
        
        Важно: каждую задачу выводи с новой строки — без пустых строк между ними.
        
        ТЕКСТ:
        %s
        """, inputText);


        String tasksSTR = service.generate(
                prompt,
                false,
                0.25f
        );

        String[] lines = tasksSTR.split("\\r?\\n");

        for (String line : lines) {
            if (line.isBlank()) continue;
            tasks.add(new Task(transcript.getId(), line));
        }
        return tasks;
    }

    public Protocol summarize(Transcript transcript) {
        StringBuilder textSB = new StringBuilder();
        for (Replica replicas: transcript.getReplicas()) {
            textSB.append(replicas.getText());
        }
        String inputText = textSB.toString();

        String prompt = String.format("""
            Ты — русский языковой помощник.
            Перескажи следующий текст на русском языке.
            Пересказ должен быть:
            - Максимально сжатым, но содержательным.
            - Без HTML‑тегов: используй обычные переносы строк, а не <br>.
            - Сохраняй точность и ключевые детали оригинала.
            ТЕКСТ:
            %s

            ПЕРЕСКАЗ:
            """, inputText);

        String summary = service.generate(
                prompt,
                false,
                0.06f,
                0.92f,
                true,
                1.05f,
                0.25f
        );

        return new Protocol(summary);
    }

    public void init() {
        if (service == null) service = new LLMService();
    }

    public boolean isInit() {
        return service != null;
    }

    public void freeResources() {
        service.freeResources();
        service = null;
    }
}
