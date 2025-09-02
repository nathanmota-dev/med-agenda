package com.ufu.gestaoConsultasMedicas.dto;

public class ChatRequest {
    private String prompt;
    private String model = "llama3.2";
    private Boolean useContext = false;
    private String instruction;
    private Integer maxItems;
    private Integer maxChars;

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

    public Boolean getUseContext() {
        return useContext;
    }
    public void setUseContext(Boolean useContext) {
        this.useContext = useContext;
    }

    public String getInstruction() {
        return instruction;
    }
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Integer getMaxItems() {
        return maxItems;
    }
    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMaxChars() {
        return maxChars;
    }
    public void setMaxChars(Integer maxChars) {
        this.maxChars = maxChars;
    }
}
