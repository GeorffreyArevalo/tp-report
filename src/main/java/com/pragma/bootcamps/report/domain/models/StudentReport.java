package com.pragma.bootcamps.report.domain.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StudentReport {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
