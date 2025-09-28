package com.example.demo;

import com.example.lims.core.AiAnalysisService;
import com.example.lims.core.TestResult;
import com.example.lims.core.TestResultService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
public class AiAnalysisController {

    private final AiAnalysisService aiAnalysisService;
    private final TestResultService testResultService;

    public AiAnalysisController(AiAnalysisService aiAnalysisService, TestResultService testResultService) {
        this.aiAnalysisService = aiAnalysisService;
        this.testResultService = testResultService;
    }

    @PostMapping("/test-results/{id}/analyze")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'LAB_MANAGER')")
    public String analyzeTestResult(@PathVariable Long id) {
        TestResult testResult = testResultService.getTestResultById(id);
        if (testResult == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TestResult not found");
        }

        try {
            return aiAnalysisService.analyzeTestResult(testResult);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error communicating with AI service", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error analyzing test result", e);
        }
    }
}
