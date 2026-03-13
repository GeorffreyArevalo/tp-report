package com.pragma.bootcamps.report.domain.clients;

import com.pragma.bootcamps.report.domain.models.TechnologyReport;
import reactor.core.publisher.Flux;

public interface TechnologyClientPort {

    Flux<TechnologyReport> getTechnologiesByCapabilityId(Long capabilityId);

}
