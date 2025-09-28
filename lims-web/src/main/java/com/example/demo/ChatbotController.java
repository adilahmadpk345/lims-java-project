package com.example.demo;

import com.example.lims.core.ChatbotService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/start")
    @PreAuthorize("isAuthenticated()")
    public String startChat() {
        return chatbotService.startChat();
    }

    @PostMapping("/{sessionId}/send")
    @PreAuthorize("isAuthenticated()")
    public String sendMessage(@PathVariable String sessionId, @RequestBody String message) {
        try {
            return chatbotService.sendMessage(sessionId, message);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending message to chatbot", e);
        }
    }
}
