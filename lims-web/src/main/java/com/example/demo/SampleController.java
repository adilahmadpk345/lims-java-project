package com.example.demo;

import com.example.lims.core.Sample;
import com.example.lims.core.SampleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;

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

    @PutMapping("/{id}")
    public Sample updateSample(@PathVariable Long id, @RequestBody Sample sample) {
        sample.setId(id);
        return sampleService.updateSample(sample);
    }

    @DeleteMapping("/{id}")
    public void deleteSample(@PathVariable Long id) {
        sampleService.deleteSample(id);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
