package com.pragma.bootcamps.report.infrastructure.adapters.webclient.dtos.responses;

import com.pragma.bootcamps.report.domain.models.TechnologyReport;

import java.util.List;

public record TechnologyListResponse(
        List<TechnologyReport> data
) {
}
