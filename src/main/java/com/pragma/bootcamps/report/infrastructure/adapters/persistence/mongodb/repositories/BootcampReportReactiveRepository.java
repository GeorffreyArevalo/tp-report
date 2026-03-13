package com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.repositories;

import com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.documents.BootcampReportDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BootcampReportReactiveRepository extends ReactiveMongoRepository<BootcampReportDocument, String> {
}
