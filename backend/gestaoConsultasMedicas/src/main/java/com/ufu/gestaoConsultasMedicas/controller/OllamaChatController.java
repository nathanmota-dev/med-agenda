package com.ufu.gestaoConsultasMedicas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufu.gestaoConsultasMedicas.dto.ChatRequest;
import com.ufu.gestaoConsultasMedicas.dto.ChatWithContextRequest;
import com.ufu.gestaoConsultasMedicas.service.ScrapingContextService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class OllamaChatController {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final ObjectMapper mapper = new ObjectMapper();
    private final ScrapingContextService contextService;

    public OllamaChatController(ScrapingContextService contextService) {
        this.contextService = contextService;
    }

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseBodyEmitter streamOllama(@RequestBody ChatRequest request) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(0L);

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                String finalPrompt;
                
                // Verifica se deve usar contexto
                boolean shouldUseContext = Boolean.TRUE.equals(request.getUseContext()) || 
                                         shouldUseContextAutomatically(request.getPrompt());
                
                if (shouldUseContext) {
                    // Constrói contexto baseado na pergunta
                    String contexto = contextService.buildContextFromQuestion(
                            request.getPrompt(),
                            request.getMaxItems() == null ? 500 : request.getMaxItems(),
                            request.getMaxChars() == null ? 12000 : request.getMaxChars()
                    );

                    // Constrói prompt final com contexto
                    String instruction = (request.getInstruction() == null || request.getInstruction().isBlank())
                            ? "Responda com base EXCLUSIVA no contexto fornecido. Se algo não estiver no contexto, diga que não há informação suficiente."
                            : request.getInstruction();

                    finalPrompt = """
                            %s

                            [CONTEXTO]
                            %s

                            [PERGUNTA]
                            %s

                            Resposta objetiva:
                            """.formatted(instruction, contexto, request.getPrompt());
                } else {
                    // Usa apenas o prompt original
                    finalPrompt = request.getPrompt();
                }

                URL url = new URL(OLLAMA_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                var payload = mapper.createObjectNode();
                payload.put("model", request.getModel());
                payload.put("prompt", finalPrompt);
                payload.put("stream", true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(mapper.writeValueAsBytes(payload));
                    os.flush();
                }

                int status = conn.getResponseCode();
                InputStream is = status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            emitter.send(line + "\n", MediaType.APPLICATION_JSON);
                        }
                    }
                }

                emitter.complete();
            } catch (Exception e) {
                try { emitter.completeWithError(e); } catch (Exception ignored) {}
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();

        return emitter;
    }

    @PostMapping(value = "/chat/with-context", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseBodyEmitter streamOllamaWithContext(@RequestBody ChatWithContextRequest request) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(0L);

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                // 1) Monta contexto de TODOS os scrappings
                String contexto = contextService.buildAllContext(
                        request.getMaxItems() == null ? 500 : request.getMaxItems(),
                        request.getMaxChars() == null ? 12000 : request.getMaxChars()
                );

                // 2) Constrói prompt final (instrução + contexto + pergunta)
                String instruction = (request.getInstruction() == null || request.getInstruction().isBlank())
                        ? "Responda com base EXCLUSIVA no contexto. Se algo não estiver no contexto, diga que não há informação suficiente."
                        : request.getInstruction();

                String prompt = """
                        %s

                        [CONTEXTO]
                        %s

                        [PERGUNTA]
                        %s

                        Resposta objetiva:
                        """.formatted(instruction, contexto, request.getQuestion());

                // 3) Chama Ollama em streaming
                URL url = new URL(OLLAMA_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                var payload = mapper.createObjectNode();
                payload.put("model", request.getModel());
                payload.put("prompt", prompt);
                payload.put("stream", true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(mapper.writeValueAsBytes(payload));
                    os.flush();
                }

                int status = conn.getResponseCode();
                InputStream is = status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            emitter.send(line + "\n", MediaType.APPLICATION_JSON);
                        }
                    }
                }

                emitter.complete();
            } catch (Exception e) {
                try { emitter.completeWithError(e); } catch (Exception ignored) {}
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();

        return emitter;
    }

    /**
     * Detecta automaticamente se a pergunta pode se beneficiar de contexto
     */
    private boolean shouldUseContextAutomatically(String prompt) {
        if (prompt == null) return false;
        
        String lowerPrompt = prompt.toLowerCase();
        
        String[] contextKeywords = {
            // CID
            "cid", "código", "doença", "diagnóstico", "classificação",
            // Notícias
            "notícia", "news", "governo", "saúde pública", "ministério",
            // Habilidades médicas
            "habilidade", "médica", "especialidade", "cfm", "resolução",
            // Guia de vigilância
            "vigilância", "guia", "termo", "definição",
            // Palavras gerais que podem indicar necessidade de contexto médico
            "o que é", "como é", "defina", "explique", "significado",
            "sintomas", "tratamento", "prevenção", "epidemiologia"
        };
        
        for (String keyword : contextKeywords) {
            if (lowerPrompt.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
}
