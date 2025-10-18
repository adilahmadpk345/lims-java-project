package com.example.demo;

import com.example.lims.core.GeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@SpringBootApplication(scanBasePackages = {"com.example.demo", "com.example.lims.core"})
@EnableJpaRepositories(basePackages = "com.example.lims.core")
@EntityScan(basePackages = "com.example.lims.core")
@RestController
public class DemoApplication {

    // Don't inject GeminiService eagerly into this application class to avoid
    // a circular dependency between the @Bean factory method below and this
    // bean instance. Fetch the service lazily from the context when handling
    // requests.
    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.beans.factory.ObjectProvider<GeminiService> geminiProvider;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public static GeminiService geminiService(@Value("${GEMINI_API_KEY:}") String apiKey) {
        // GEMINI_API_KEY is optional for local runs; the service will behave as a
        // placeholder when empty. For real integration, set GEMINI_API_KEY.
        return new GeminiService(apiKey, "gemini-1.5-pro-latest");
    }

    @GetMapping("/story")
    public String generateStory(@RequestParam(defaultValue = "Tell me a story about a brave robot.") String prompt) {
        GeminiService gemini = geminiProvider.getIfAvailable();
        if (gemini == null) {
            return "GeminiService not available/configured";
        }
        try {
            return gemini.generateContent(prompt);
        } catch (Exception e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            return "Error generating content: " + e.getMessage();
        }
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String sessionId, @RequestParam String prompt) {
        GeminiService gemini = geminiProvider.getIfAvailable();
        if (gemini == null) {
            return "GeminiService not available/configured";
        }
        try {
            return gemini.sendMessage(sessionId, prompt);
        } catch (Exception e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            return "Error during chat: " + e.getMessage();
        }
    }
}
