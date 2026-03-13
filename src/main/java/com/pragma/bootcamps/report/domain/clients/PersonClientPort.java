package com.pragma.bootcamps.report.domain.clients;

import com.pragma.bootcamps.report.domain.models.StudentReport;
import reactor.core.publisher.Mono;

public interface PersonClientPort {
    Mono<StudentReport> getPersonById(Long personId);
}
