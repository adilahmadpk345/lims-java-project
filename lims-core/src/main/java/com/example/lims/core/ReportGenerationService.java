package com.example.lims.core;

import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.Part;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReportGenerationService {

    private final GenerativeModel generativeModel;

    public ReportGenerationService(GenerativeModel generativeModel) {
        this.generativeModel = generativeModel;
    }

    public String generateReport(String prompt) {
        try {
            GenerateContentResponse response = generativeModel.generateContent(prompt);
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
