package com.pragma.bootcamps.report.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampCreated {
    private Long bootcampId;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer capabilityCount;
}
