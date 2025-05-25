package logic.protocol;

import logic.general.*;
import logic.utils.JsonTaskFiller;

import java.util.List;

public class LLMWrapper {
    private JsonTaskFiller jsonTaskFiller;
    private LLMService service = null;

    public List<Task> getTasks(Transcript transcript) {
        List<Task> tasks;
        StringBuilder textSB = new StringBuilder();
        for (Replica replicas: transcript.getReplicas()) {
            textSB.append(replicas.getText());
        }
        String inputText = textSB.toString();
        String prompt = String.format("""
        Ты — русский языковой помощник для анализа текстов.
        Извлеки самые важные задачи из текста ниже.
        Выведи результат строго в формате JSON — без лишних пояснений.
        
        Требуемая схема JSON:
        [
          {
            "task": "10–20 слов — суть задачи",
            "responsible": "Имя и/или фамилия",
            "due_date": "ДД.MM.ГГГГ"
          }
        ]
        
        ТЕКСТ:
        %s
        <<END>>
        """, inputText);

        int n_pred = service.calculateNPredict(prompt);

        String tasksSTR = service.generate(
                prompt,
                n_pred,
                0.1f,
                0.75f,
                0.01f,
                1.10f,
                0.00f,
                0.0f,
                "<<END>>");

        tasks = JsonTaskFiller.createTasksList(tasksSTR, transcript.getId());

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
            <<END>>
            """, inputText);

        int n_pred = service.calculateNPredict(prompt);

        String summary = service.generate(
                prompt,
                n_pred,
                0.25f,
                0.90f,
                0.00f,
                1.30f,
                0.40f,
                0.10f,
                "<<END>>");

        return new Protocol(summary);
    }


    public void init() {
        if (service == null) service = new LLMService();
    }

    public boolean isInit() {
        return service != null;
    }

    public void freeResources() {
        if (service != null) service.freeResources();
        service = null;
    }

}
