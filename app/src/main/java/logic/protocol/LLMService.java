package logic.protocol;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;


public class LLMService {
    private final String modelPath;

    public LLMService(String modelPath) {
        this.modelPath = modelPath;
    }

    public String generate(
            String prompt,
            boolean ignoreEos,
            float minP,
            float topP,
            boolean penalizeNl,
            float repeatPenalty,
            float temperature
    ) {

        ModelParameters mp = new ModelParameters()
                .setModel(modelPath);
        LlamaModel llama = new LlamaModel(mp);

        InferenceParameters ip = new InferenceParameters("")
                .setPrompt(prompt)
                .setIgnoreEos(ignoreEos)
                .setMinP(minP)
                .setTopP(topP)
                .setPenalizeNl(penalizeNl)
                .setRepeatPenalty(repeatPenalty)
                .setTemperature(temperature);

        StringBuilder result = new StringBuilder();
        for (LlamaOutput tok : llama.generate(ip)) {
            result.append(tok.text);
        }
        llama.close();
        return result.toString().trim();
    }

    public String generate(
            String prompt,
            boolean ignoreEos,
            float temperature
    ) {

        ModelParameters mp = new ModelParameters()
                .setModel(modelPath);
        LlamaModel llama = new LlamaModel(mp);

        InferenceParameters ip = new InferenceParameters("")
                .setPrompt(prompt)
                .setIgnoreEos(ignoreEos)
                .setTemperature(temperature);

        StringBuilder result = new StringBuilder();
        for (LlamaOutput tok : llama.generate(ip)) {
            result.append(tok.text);
        }
        llama.close();
        return result.toString().trim();
    }
}