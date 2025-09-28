package com.example.lims.core;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {

    private final SampleRepository sampleRepository;

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public Sample createSample(Sample sample) {
        return sampleRepository.save(sample);
    }

    public List<Sample> getAllSamples() {
        return sampleRepository.findAll();
    }

    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id).orElse(null);
    }

    public Sample updateSample(Sample sample) {
        return sampleRepository.save(sample);
    }
}
