package com.pragma.bootcamps.report.domain.models;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampReport {
    private String id;
    private Long bootcampId;
    private String name;
    private String description;
    private Integer capabilityCount;
    private Integer technologyCount;
    private Integer enrolledStudentCount;
    private List<CapabilityReport> capabilities;
    private List<StudentReport> students;
}
