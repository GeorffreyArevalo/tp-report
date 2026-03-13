package com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEnrollDto {
    private Long bootcampId;
    private Long personId;
}
