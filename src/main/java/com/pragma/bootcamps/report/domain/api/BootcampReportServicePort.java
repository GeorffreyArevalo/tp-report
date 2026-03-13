package com.pragma.bootcamps.report.domain.api;

import com.pragma.bootcamps.report.domain.models.BootcampCreated;
import com.pragma.bootcamps.report.domain.models.BootcampReport;
import reactor.core.publisher.Mono;

public interface BootcampReportServicePort {

    Mono<Void> handleBootcampCreation(BootcampCreated bootcampCreated);
    Mono<Void> addPersonToBootcampReport(Long bootcampId, Long personId);
    Mono<BootcampReport> getMostSuccessfulBootcamp();

}
