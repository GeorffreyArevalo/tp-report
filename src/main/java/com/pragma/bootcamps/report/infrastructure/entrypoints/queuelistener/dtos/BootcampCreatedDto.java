package com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampCreatedDto implements Serializable {
    private Long bootcampId;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer capabilityCount;
}
