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
        normalizeAndValidate(sample);
        return sampleRepository.save(sample);
    }

    public List<Sample> getAllSamples() {
        return sampleRepository.findAll();
    }

    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id).orElse(null);
    }

    public Sample updateSample(Sample sample) {
        normalizeAndValidate(sample);
        return sampleRepository.save(sample);
    }

    private void normalizeAndValidate(Sample sample) {
        if (sample == null) throw new IllegalArgumentException("Sample cannot be null");
        // Normalize type: map common misspellings / case to enum labels
        if (sample.getType() != null) {
            String t = sample.getType().trim();
            // common fixes
            if (t.equalsIgnoreCase("horomes")) t = "Hormones";
            // match against known types
            boolean matched = false;
            for (SampleType st : SampleType.values()) {
                if (st.toString().equalsIgnoreCase(t) || st.name().equalsIgnoreCase(t)) { sample.setType(st.toString()); matched = true; break; }
            }
            if (!matched) throw new IllegalArgumentException("Invalid sample type: " + sample.getType());
        }
        // Normalize/validate status
        if (sample.getStatus() != null) {
            String s = sample.getStatus().trim();
            if (s.equalsIgnoreCase("in process")) s = "In Progress";
            boolean matched = false;
            for (SampleStatus ss : SampleStatus.values()) {
                if (ss.toString().equalsIgnoreCase(s) || ss.name().equalsIgnoreCase(s)) { sample.setStatus(ss.toString()); matched = true; break; }
            }
            if (!matched) throw new IllegalArgumentException("Invalid sample status: " + sample.getStatus());
        }
    }

    public void deleteSample(Long id) {
        sampleRepository.deleteById(id);
    }
}
