package com.example.lims.core;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final GeminiService geminiService;
    private final DataQueryService dataQueryService;

    public ChatService(GeminiService geminiService, DataQueryService dataQueryService) {
        this.geminiService = geminiService;
        this.dataQueryService = dataQueryService;
    }

    public String chat(List<String> messages) {
        // Implement chat logic here, potentially using dataQueryService for context
        return geminiService.chat(messages);
    }
}
