package com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.mappers;

import com.pragma.bootcamps.report.domain.models.BootcampCreated;
import com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.dtos.BootcampCreatedDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface BootcampReportMapper {

    BootcampCreated toModel(BootcampCreatedDto bootcampCreatedDto);
}