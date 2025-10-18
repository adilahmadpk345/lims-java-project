package com.example.lims.core;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatbotService {

    private final GeminiService geminiService;

    public ChatbotService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public String startChat() {
        // Return a session id; in production this would create server-side session state
        return UUID.randomUUID().toString();
    }

    public String sendMessage(String sessionId, String message) {
        // Defer to GeminiService placeholder method
        return geminiService.sendMessage(sessionId, message);
    }
}
