package com.example.demo;

import com.example.lims.core.Sample;
import com.example.lims.core.SampleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/samples")
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @PostMapping
    public Sample createSample(@RequestBody Sample sample) {
        return sampleService.createSample(sample);
    }

    @GetMapping
    public List<Sample> getAllSamples() {
        return sampleService.getAllSamples();
    }

    @GetMapping("/{id}")
    public Sample getSampleById(@PathVariable Long id) {
        return sampleService.getSampleById(id);
    }
}
