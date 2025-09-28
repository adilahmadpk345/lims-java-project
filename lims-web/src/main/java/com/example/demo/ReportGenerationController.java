package com.example.demo;

import com.example.lims.core.ReportGenerationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reports")
public class ReportGenerationController {

    private final ReportGenerationService reportGenerationService;

    public ReportGenerationController(ReportGenerationService reportGenerationService) {
        this.reportGenerationService = reportGenerationService;
    }

    @PostMapping("/samples/{sampleId}/generate")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'LAB_MANAGER')")
    public String generateReport(@PathVariable Long sampleId) {
        try {
            return reportGenerationService.generateReportForSample(sampleId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating report", e);
        }
    }
}
