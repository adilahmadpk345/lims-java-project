package com.example.demo;

import com.example.lims.core.GeminiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
    public GeminiService geminiService() throws IOException {
        // IMPORTANT: Replace with your actual project ID, location, and model name
        // You should externalize this configuration to application.properties or environment variables
        return new GeminiService("your-google-cloud-project-id", "us-central1", "gemini-1.5-pro-preview-0409");
    }

    @GetMapping("/story")
    public String generateStory(@RequestParam(defaultValue = "Tell me a story about a brave robot.") String prompt) {
        try {
            return geminiService.generateContent(prompt);
        } catch (IOException e) {
            return "Error generating content: " + e.getMessage();
        }
    }
}
