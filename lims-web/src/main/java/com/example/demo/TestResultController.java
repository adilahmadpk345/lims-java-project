package com.example.demo;

import com.example.lims.core.TestResult;
import com.example.lims.core.TestResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    private final TestResultService testResultService;

    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

    @PostMapping
    public TestResult createTestResult(@RequestBody TestResult testResult) {
        return testResultService.createTestResult(testResult);
    }

    @GetMapping
    public List<TestResult> getAllTestResults() {
        return testResultService.getAllTestResults();
    }

    @GetMapping("/{id}")
    public TestResult getTestResultById(@PathVariable Long id) {
        return testResultService.getTestResultById(id);
    }
}
