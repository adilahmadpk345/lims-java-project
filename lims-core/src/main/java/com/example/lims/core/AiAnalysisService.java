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

    public AiAnalysisService(GenerativeModel generativeModel) {
        this.generativeModel = generativeModel;
    }

    public String analyze(String prompt) throws IOException {
        GenerateContentResponse response = this.generativeModel.generateContent(prompt);
        return response.toString();
    }

    public String analyze(MultipartFile imageFile, String prompt) throws IOException {
        byte[] imageBytes = imageFile.getBytes();

        Content content = Content.newBuilder()
                .setRole("user")
                .addParts(Part.newBuilder().setText(prompt).build())
                .addParts(
                        Part.newBuilder()
                                .setMimeType(imageFile.getContentType())
                                .setData(ByteString.copyFrom(imageBytes))
                                .build()
                ).build();

        GenerateContentResponse response = this.generativeModel.generateContent(content);
        return response.toString();
    }

    public String chat(List<Content> history, String prompt) throws IOException {
        ChatSession chatSession = new ChatSession(this.generativeModel);
        // Add history to the chat session
        for (Content content : history) {
            chatSession.getHistory().add(content);
        }

        GenerateContentResponse response = chatSession.sendMessage(prompt);
        return response.toString();
    }
}
