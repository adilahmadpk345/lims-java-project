package com.example.lims.core;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class AiAnalysisService {

    private final GenerativeModel generativeModel;

    public AiAnalysisService(org.springframework.beans.factory.ObjectProvider<GenerativeModel> generativeModelProvider) {
        this.generativeModel = generativeModelProvider.getIfAvailable();
    }

    public String analyze(String prompt) throws IOException {
        if (this.generativeModel == null) {
            return "Vertex AI not configured; AI analysis unavailable for prompt: " + prompt;
        }
        GenerateContentResponse response = this.generativeModel.generateContent(prompt);
        return response.toString();
    }

    // Compatibility helper: analyze a TestResult entity
    public String analyzeTestResult(TestResult testResult) throws IOException {
        if (testResult == null) return "";
        String prompt = "Analyze test result: " + testResult.getResult();
        return analyze(prompt);
    }

    public String analyze(MultipartFile imageFile, String prompt) throws IOException {
    // Some Vertex AI client versions in the dependency tree don't expose the binary
    // Part builder methods used originally. To keep the module buildable in this
    // example workspace, we send the prompt as a single text part and avoid
    // attaching raw bytes. You can restore binary image parts after aligning the
    // vertex-ai client version in the POM.
    Content content = Content.newBuilder()
        .setRole("user")
        .addParts(Part.newBuilder().setText(prompt + "\n[image omitted in local build]").build())
        .build();

        if (this.generativeModel == null) {
            return "Vertex AI not configured; image analysis unavailable";
        }
        GenerateContentResponse response = this.generativeModel.generateContent(content);
        return response.toString();
    }

    public String chat(List<Content> history, String prompt) throws IOException {
        if (this.generativeModel == null) {
            return "Vertex AI not configured; chat unavailable";
        }
        ChatSession chatSession = new ChatSession(this.generativeModel);
        // Add history to the chat session
        for (Content content : history) {
            chatSession.getHistory().add(content);
        }

        GenerateContentResponse response = chatSession.sendMessage(prompt);
        return response.toString();
    }
}
