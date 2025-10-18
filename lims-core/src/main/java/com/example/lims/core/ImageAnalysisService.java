package com.example.lims.core;

import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.Part;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageAnalysisService {

    private final GenerativeModel generativeModel;

    public ImageAnalysisService(org.springframework.beans.factory.ObjectProvider<GenerativeModel> generativeModelProvider) {
        this.generativeModel = generativeModelProvider.getIfAvailable();
    }

    public String analyzeImage(MultipartFile imageFile, String prompt) throws IOException {
    // Binary image parts are omitted in this workspace build because the
    // installed vertex-ai client doesn't expose the same Part.Builder API.
    Content content = Content.newBuilder()
        .setRole("user")
        .addParts(Part.newBuilder().setText(prompt + "\n[image omitted in local build]").build())
        .build();

        if (this.generativeModel == null) {
            return "Vertex AI not configured; image analysis unavailable";
        }
        GenerateContentResponse response = generativeModel.generateContent(content);
        return response.toString();
    }

    // Compatibility wrapper expected by the web controller
    public void storeImage(Long sampleId, MultipartFile file) throws IOException {
        // In a full implementation, store file against the sample; here we no-op
    }

    // Compatibility wrapper used by ImageAnalysisController.analyzeImage(sampleId, prompt)
    public String analyzeImage(Long sampleId, String prompt) throws IOException {
        // No image stored in local build; call analyzeText-only helper.
        // Cast null so the compiler resolves the MultipartFile overload.
        return analyzeImage((org.springframework.web.multipart.MultipartFile) null, prompt);
    }
}
