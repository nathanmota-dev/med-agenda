package com.ufu.gestaoConsultasMedicas.dto;

public class ChatWithContextRequest {
    private String model;          
    private String question;     
    private String instruction;    
    private Integer maxItems;     
    private Integer maxChars;     

    public String getModel() { return model; }
    public String getQuestion() { return question; }
    public String getInstruction() { return instruction; }
    public Integer getMaxItems() { return maxItems; }
    public Integer getMaxChars() { return maxChars; }

    public void setModel(String model) { this.model = model; }
    public void setQuestion(String question) { this.question = question; }
    public void setInstruction(String instruction) { this.instruction = instruction; }
    public void setMaxItems(Integer maxItems) { this.maxItems = maxItems; }
    public void setMaxChars(Integer maxChars) { this.maxChars = maxChars; }
}
