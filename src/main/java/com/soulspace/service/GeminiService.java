package com.soulspace.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    private static final String GEMINI_API_KEY = "AIzaSyC-mnfSGsyXxEHC6evL82Bb0xHos88VLHY"; 
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=" + GEMINI_API_KEY;

    // CHANGED: Now accepts a List of history items
    public String getAIResponse(List<Map<String, String>> conversationHistory) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();

        // 1. SYSTEM PROMPT (The "Personality")
        // We cheat a bit and add this as the very first "user" message to set the tone
        Map<String, Object> systemPart = new HashMap<>();
        systemPart.put("role", "user");
        systemPart.put("parts", Collections.singletonList(Map.of("text", 
            "You are SoulSpace AI, a supportive mental health assistant. " +
            "Keep your answers comforting, short, and helpful. " +
            "If the user asks for help, provide coping strategies.")));
        contents.add(systemPart);

        // 2. ADD CHAT HISTORY (The "Memory")
        // We loop through the list passed from the Controller
        for (Map<String, String> entry : conversationHistory) {
            Map<String, Object> message = new HashMap<>();
            // Map "user" -> "user", "bot" -> "model" for Gemini API
            String role = entry.get("role").equals("bot") ? "model" : "user";
            message.put("role", role);
            message.put("parts", Collections.singletonList(Map.of("text", entry.get("content"))));
            contents.add(message);
        }

        body.put("contents", contents);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // 3. SEND REQUEST (With Retry Logic)
        int maxRetries = 2;
        int attempt = 0;
        while (attempt <= maxRetries) {
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
                return parseResponse(response);
            } catch (HttpClientErrorException.TooManyRequests e) {
                attempt++;
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            } catch (Exception e) {
                e.printStackTrace();
                return "I'm having trouble connecting right now.";
            }
        }
        return "I am currently overloaded. Please try again.";
    }

    private String parseResponse(ResponseEntity<Map> response) {
        try {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> responseParts = (List<Map<String, Object>>) content.get("parts");
                    return (String) responseParts.get(0).get("text");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "I couldn't process that thought clearly.";
    }
}