package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.dtos.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StudentReportResponse {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
