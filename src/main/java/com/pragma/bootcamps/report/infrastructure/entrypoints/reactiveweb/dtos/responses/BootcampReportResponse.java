package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.dtos.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampReportResponse {
    private String id;
    private Long bootcampId;
    private String name;
    private String description;
    private Integer capabilityCount;
    private Integer technologyCount;
    private Integer enrolledStudentCount;
    private List<CapabilityReportResponse> capabilities;
    private List<StudentReportResponse> students;
}
