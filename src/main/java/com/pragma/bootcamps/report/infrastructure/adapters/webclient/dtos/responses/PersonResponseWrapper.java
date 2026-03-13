package com.pragma.bootcamps.report.infrastructure.adapters.webclient.dtos.responses;

import com.pragma.bootcamps.report.domain.models.StudentReport;

public record PersonResponseWrapper(
        StudentReport data
) {
}
