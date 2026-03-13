package com.pragma.bootcamps.report.domain.clients;

import com.pragma.bootcamps.report.domain.models.CapabilityReport;
import reactor.core.publisher.Flux;

public interface CapabilityClientPort {
    Flux<CapabilityReport> getCapabilitiesByBootcampId(Long bootcampId);
}
