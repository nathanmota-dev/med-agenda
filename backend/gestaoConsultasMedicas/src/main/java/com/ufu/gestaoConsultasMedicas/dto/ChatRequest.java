package com.ufu.gestaoConsultasMedicas.dto;

public class ChatRequest {
    private String prompt;
    private String model = "llama3.2";

    public String getPrompt() {
        return prompt;
    }
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
}
