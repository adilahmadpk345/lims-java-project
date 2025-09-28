package com.example.demo;

import com.example.lims.core.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/start")
    public String startChat() {
        return chatService.startChat();
    }

    @PostMapping("/{sessionId}/send")
    public String sendMessage(@PathVariable String sessionId, @RequestBody String message) throws Exception {
        return chatService.sendMessage(sessionId, message);
    }
}
