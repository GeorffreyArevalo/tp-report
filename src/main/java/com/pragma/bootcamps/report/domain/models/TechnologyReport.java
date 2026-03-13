package com.pragma.bootcamps.report.domain.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TechnologyReport {
    private Long id;
    private String name;
}
