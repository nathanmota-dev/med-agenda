package com.ufu.gestaoConsultasMedicas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufu.gestaoConsultasMedicas.dto.ChatRequest;
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

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseBodyEmitter streamOllama(@RequestBody ChatRequest request) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(0L);

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(OLLAMA_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                var payload = mapper.createObjectNode();
                payload.put("model", request.getModel());
                payload.put("prompt", request.getPrompt());
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
                try {
                    emitter.completeWithError(e);
                } catch (Exception ignored) {}
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();

        return emitter;
    }
}
