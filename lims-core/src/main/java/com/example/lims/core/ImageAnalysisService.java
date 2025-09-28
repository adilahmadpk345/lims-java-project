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

    public ImageAnalysisService(GenerativeModel generativeModel) {
        this.generativeModel = generativeModel;
    }

    public String analyzeImage(MultipartFile imageFile, String prompt) throws IOException {
        byte[] imageBytes = imageFile.getBytes();

        Content content = Content.newBuilder()
                .setRole("user")
                .addParts(Part.newBuilder().setText(prompt).build())
                .addParts(
                        Part.newBuilder()
                                .setMimeTypeValue(imageFile.getContentType())
                                .setData(ByteString.copyFrom(imageBytes))
                                .build()
                ).build();

        GenerateContentResponse response = generativeModel.generateContent(content);
        return response.toString();
    }
}
