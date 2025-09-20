package com.example.demo;

import com.example.lims.core.GeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
@RestController
public class DemoApplication {

    private final GeminiService geminiService;

    public DemoApplication(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public GeminiService geminiService(@Value("${GEMINI_API_KEY}") String apiKey) {
        // IMPORTANT: Make sure you have the GEMINI_API_KEY environment variable set.
        return new GeminiService(apiKey, "gemini-1.5-pro-latest");
    }

    @GetMapping("/story")
    public String generateStory(@RequestParam(defaultValue = "Tell me a story about a brave robot.") String prompt) {
        try {
            return geminiService.generateContent(prompt);
        } catch (ExecutionException | InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
            return "Error generating content: " + e.getMessage();
        }
    }
}
