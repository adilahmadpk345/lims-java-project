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

    // Accept a possibly-missing GenerativeModel (Vertex AI client). For local
    // development we don't require the cloud client; if it's not available the
    // service returns a meaningful placeholder message.
    public ReportGenerationService(org.springframework.beans.factory.ObjectProvider<GenerativeModel> generativeModelProvider) {
        this.generativeModel = generativeModelProvider.getIfAvailable();
    }

    public String generateReport(String prompt) {
        if (this.generativeModel == null) {
            return "Vertex AI GenerativeModel not configured; returning placeholder report for prompt: " + prompt;
        }
        try {
            GenerateContentResponse response = generativeModel.generateContent(prompt);
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Compatibility method used by the web controller
    public String generateReportForSample(Long sampleId) {
        // In a full implementation you'd load the sample data and craft a prompt
        return generateReport("Generate report for sample " + sampleId);
    }
}
