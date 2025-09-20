package com.example.lims.core;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

import java.io.IOException;

public class GeminiService {

    private final GenerativeModel model;

    public GeminiService(String projectId, String location, String modelName) throws IOException {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            model = new GenerativeModel(modelName, vertexAI);
        }
    }

    public String generateContent(String prompt) throws IOException {
        GenerateContentResponse response = model.generateContent(prompt);
        return response.toString();
    }
}
