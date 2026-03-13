package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.mappers;

import com.pragma.bootcamps.report.domain.models.BootcampReport;
import com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.dtos.responses.BootcampReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring"
)
public interface ReportRequestMapper {

    BootcampReportResponse toBootcampReportDto(BootcampReport bootcampReport);

}
