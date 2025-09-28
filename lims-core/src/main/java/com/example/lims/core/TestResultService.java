package com.example.lims.core;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultService {

    private final TestResultRepository testResultRepository;

    public TestResultService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    public TestResult createTestResult(TestResult testResult) {
        return testResultRepository.save(testResult);
    }

    public TestResult getTestResultById(Long id) {
        return testResultRepository.findById(id).orElse(null);
    }

    public List<TestResult> getTestResultsForSample(Long sampleId) {
        return testResultRepository.findBySampleId(sampleId);
    }
}
