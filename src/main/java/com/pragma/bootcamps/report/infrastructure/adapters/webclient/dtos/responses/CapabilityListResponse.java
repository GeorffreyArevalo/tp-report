package com.pragma.bootcamps.report.infrastructure.adapters.webclient.dtos.responses;

import com.pragma.bootcamps.report.domain.models.CapabilityReport;

import java.util.List;

public record CapabilityListResponse(
        List<CapabilityReport> data
) {
}
