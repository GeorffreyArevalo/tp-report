package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.dtos.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapabilityReportResponse {
    private Long id;
    private String name;
    private List<TechnologyReportResponse> technologies;
}
