package com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.mappers;

import com.pragma.bootcamps.report.domain.models.BootcampReport;
import com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.documents.BootcampReportDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface BootcampReportDocumentMapper {

    BootcampReportDocument toDocument(BootcampReport bootcampReport);
    BootcampReport toDomain(BootcampReportDocument document);

}
