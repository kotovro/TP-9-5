package logic.protocol;

import logic.general.Replica;
import logic.general.Transcript;

public class TextSummarizer {

    private static final String MODEL_PATH = "dynamic-resources/Vikhr-Llama-3.2-1B-Q8_0.gguf";

    public static void summarize(Transcript transcript) {
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

        LLMService service = new LLMService(MODEL_PATH);
        String summary = service.generate(
                prompt,
                false,
                0.06f,
                0.92f,
                true,
                1.05f,
                0.25f
        );
        System.out.println(summary);
    }

}
