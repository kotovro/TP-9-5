package logic.protocol;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;


public class LLMService {
    private final String MODEL_PATH = "dynamic-resources/ai-models/LLM";
    private final ModelParameters parameters = new ModelParameters().setModel(MODEL_PATH);
    private final LlamaModel model = new LlamaModel(parameters);

    public String generate(
            String prompt,
            boolean ignoreEos,
            float minP,
            float topP,
            boolean penalizeNl,
            float repeatPenalty,
            float temperature
    ) {
        InferenceParameters ip = new InferenceParameters("")
                .setPrompt(prompt)
                .setIgnoreEos(ignoreEos)
                .setMinP(minP)
                .setTopP(topP)
                .setPenalizeNl(penalizeNl)
                .setRepeatPenalty(repeatPenalty)
                .setTemperature(temperature);

        StringBuilder result = new StringBuilder();
        for (LlamaOutput tok : model.generate(ip)) {
            result.append(tok.text);
        }
        return result.toString().trim();
    }

    public String generate(
            String prompt,
            boolean ignoreEos,
            float temperature
    ) {
        InferenceParameters ip = new InferenceParameters("")
                .setPrompt(prompt)
                .setIgnoreEos(ignoreEos)
                .setTemperature(temperature);

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