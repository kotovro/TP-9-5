package logic.protocol;

import logic.general.Replica;
import logic.general.Transcript;

public class TaskExtractor {
    private static final String MODEL_PATH = "dynamic-resources/Vikhr-Llama-3.2-1B-Q8_0.gguf";

    public static void getTasks(Transcript transcript) {
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

        LLMService service = new LLMService(MODEL_PATH);

        String tasks = service.generate(
                prompt,
                false,
                0.25f
        );
        System.out.println(tasks);
    }
}
