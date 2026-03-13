package com.pragma.bootcamps.report.domain.spi;

import com.pragma.bootcamps.report.domain.models.BootcampReport;
import com.pragma.bootcamps.report.domain.models.StudentReport;
import reactor.core.publisher.Mono;

public interface BootcampReportPersistencePort {

    Mono<BootcampReport> save(BootcampReport bootcampReport);
    Mono<Void> addStudentToReport(Long bootcampId, StudentReport student);
    Mono<BootcampReport> findMostEnrolledBootcamp();

}
