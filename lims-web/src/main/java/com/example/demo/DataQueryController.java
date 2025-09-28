package com.example.demo;

import com.example.lims.core.DataQueryService;
import com.example.lims.core.Sample;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/query")
public class DataQueryController {

    private final DataQueryService dataQueryService;

    public DataQueryController(DataQueryService dataQueryService) {
        this.dataQueryService = dataQueryService;
    }

    @PostMapping("/samples")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'LAB_MANAGER')")
    public List<Sample> querySamples(@RequestBody Map<String, String> criteria) {
        return dataQueryService.findSamplesByCriteria(criteria);
    }
}
