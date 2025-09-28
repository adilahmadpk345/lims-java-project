package com.example.lims.core;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestDefinitionService {

    private final TestDefinitionRepository testDefinitionRepository;

    public TestDefinitionService(TestDefinitionRepository testDefinitionRepository) {
        this.testDefinitionRepository = testDefinitionRepository;
    }

    public TestDefinition createTestDefinition(TestDefinition testDefinition) {
        return testDefinitionRepository.save(testDefinition);
    }

    public List<TestDefinition> getAllTestDefinitions() {
        return testDefinitionRepository.findAll();
    }

    public TestDefinition getTestDefinitionById(Long id) {
        return testDefinitionRepository.findById(id).orElse(null);
    }
}
