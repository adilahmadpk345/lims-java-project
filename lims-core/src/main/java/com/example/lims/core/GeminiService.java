package com.example.lims.core;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GeminiService {

    private final GenerativeModelFutures model;

    public GeminiService(String apiKey, String modelName) {
        model = new GenerativeModelFutures(modelName, apiKey);
    }

    public String generateContent(String prompt) throws ExecutionException, InterruptedException {
        Content content = new Content.Builder().addText(prompt).build();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        return GenerativeModel.extractText(response.get());
    }
}
