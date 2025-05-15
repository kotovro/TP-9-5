package logic.protocol;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;
import logic.general.Replica;
import logic.general.Transcript;

public class TextSummarizer {

    public static void summarize(Transcript transcript) {
        StringBuilder textSB = new StringBuilder();
        for (Replica replicas: transcript.getReplicas()) {
            textSB.append(replicas.getText());
        }
        String inputText = textSB.toString();

        ModelParameters mp = new ModelParameters()
                .setModel("dynamic-resources/Vikhr-Llama-3.2-1B-Q8_0.gguf");
        LlamaModel llama = new LlamaModel(mp);

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

        InferenceParameters ip = new InferenceParameters("")
                .setPrompt(prompt)
                .setIgnoreEos(false)
                .setMinP(0.06f)
                .setTopP(0.92f)
                .setPenalizeNl(true)
                .setRepeatPenalty(1.05f)
                .setTemperature(0.25f);

        StringBuilder summary = new StringBuilder();
        for (LlamaOutput tok : llama.generate(ip)) {
            summary.append(tok.text);
        }
        System.out.println(summary.toString().trim());
        llama.close();
    }
}
