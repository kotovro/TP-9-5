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
        StringBuilder textSB = new StringBuilder();
        for (Replica replicas: transcript.getReplicas()) {
            textSB.append(replicas.getText());
        }
        String inputText = textSB.toString();
        String prompt = String.format("""
        Ты — русский языковой помощник для анализа текстов.
        Извлеки ТОП-5 самых важных задач из текста ниже.
        Если задач больше пяти — выбери ключевые, остальные проигнорируй.
        
        Формат для каждой задачи:
        1. [Суть задачи, 10-20 слов].
            * Ответственный: [имя/должность или «не указан»].
            * Метки: [если есть], Срок: [дата или «не указан»].
        
        Пример:
        1. Запуск MVP нейроинтерфейсов для ритейла.
            * Ответственный: отдел R&D.
            * Метки: ИИ, Срок: 29.09.2025.
        
        ТЕКСТ:
        %s
        """, inputText);

        String tasks = service.generate(
                prompt,
                false,
                0.25f
        );

        //TODO: FIX TASKS EXTRACTION
        return new ArrayList<>();
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
