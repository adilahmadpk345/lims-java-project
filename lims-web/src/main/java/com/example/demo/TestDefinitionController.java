package com.example.demo;

import com.example.lims.core.TestDefinition;
import com.example.lims.core.TestDefinitionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-definitions")
public class TestDefinitionController {

    private final TestDefinitionService testDefinitionService;

    public TestDefinitionController(TestDefinitionService testDefinitionService) {
        this.testDefinitionService = testDefinitionService;
    }

    @PostMapping
    public TestDefinition createTestDefinition(@RequestBody TestDefinition testDefinition) {
        return testDefinitionService.createTestDefinition(testDefinition);
    }

    @GetMapping
    public List<TestDefinition> getAllTestDefinitions() {
        return testDefinitionService.getAllTestDefinitions();
    }

    @GetMapping("/{id}")
    public TestDefinition getTestDefinitionById(@PathVariable Long id) {
        return testDefinitionService.getTestDefinitionById(id);
    }
}
