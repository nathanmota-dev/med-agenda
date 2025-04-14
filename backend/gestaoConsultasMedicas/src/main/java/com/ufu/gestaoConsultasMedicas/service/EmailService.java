package com.ufu.gestaoConsultasMedicas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("re_6gEiCUYB_D8rKKD2XZPJRhUXQCh6gQwpX")
    private String apiKey;

    @Value("onboarding@resend.dev")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String RESEND_API_URL = "https://api.resend.com/emails";

    public void sendEmail(String to, String subject, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> payload = new HashMap<>();
        payload.put("from", fromEmail);
        payload.put("to", to);
        payload.put("subject", subject);
        payload.put("html", "<strong>" + body + "</strong>");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(RESEND_API_URL, request, String.class);
    }
}

