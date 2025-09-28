package com.example.demo;

import com.example.lims.core.ImageAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageAnalysisController {

    private final ImageAnalysisService imageAnalysisService;

    public ImageAnalysisController(ImageAnalysisService imageAnalysisService) {
        this.imageAnalysisService = imageAnalysisService;
    }

    @PostMapping("/samples/{sampleId}/upload")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public ResponseEntity<Void> uploadImage(@PathVariable Long sampleId, @RequestParam("file") MultipartFile file) {
        try {
            imageAnalysisService.storeImage(sampleId, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error storing image", e);
        }
    }

    @PostMapping("/samples/{sampleId}/analyze")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public String analyzeImage(@PathVariable Long sampleId, @RequestBody String prompt) {
        try {
            return imageAnalysisService.analyzeImage(sampleId, prompt);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error analyzing image", e);
        }
    }
}
