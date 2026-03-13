package com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.mappers;

import com.pragma.bootcamps.report.domain.models.BootcampCreated;
import com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.dtos.BootcampCreatedDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BootcampReportMapper {

    @Mappings({
            @Mapping(target = "bootcampId", source = "bootcampCreatedDto.bootcampId"),
            @Mapping(target = "name", source = "bootcampCreatedDto.name"),

    })
    BootcampCreated toModel(BootcampCreatedDto bootcampCreatedDto);
}