package logic.protocol;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;


public class LLMService {
    private static final int MAX_CONTEXT_TOKENS = 131072;
    private final String MODEL_PATH = "dynamic-resources/ai-models/LLM/LLM.gguf";
    private final ModelParameters parameters = new ModelParameters()
                                                                    .setModel(MODEL_PATH)
                                                                    .setThreads(3)
                                                                    .enableMlock();
    private final LlamaModel model = new LlamaModel(parameters);

    public int calculateNPredict(String prompt) {
        int[] tokens = model.encode(prompt);
        int promptTokens = tokens.length;
        int available = MAX_CONTEXT_TOKENS - promptTokens;
        return Math.max(available, 0);
    }

    public String generate(
            String prompt,
            int nPredict,
            float temperature,
            float topP,
            float minP,
            float repeatPenalty,
            float presencePenalty,
            float frequencyPenalty,
            String stopStrings
    ) {
        InferenceParameters ip = new InferenceParameters("")
                .setPrompt(prompt)
                .setNPredict(nPredict)
                .setTemperature(temperature)
                .setTopP(topP)
                .setMinP(minP)
                .setRepeatPenalty(repeatPenalty)
                .setPresencePenalty(presencePenalty)
                .setFrequencyPenalty(frequencyPenalty)
                .setStopStrings(stopStrings)
                .setRepeatLastN(128);

        StringBuilder result = new StringBuilder();
        for (LlamaOutput tok : model.generate(ip)) {
            result.append(tok.text);
        }

        return result.toString().trim();
    }

    public void freeResources() {
        model.close();
    }

}