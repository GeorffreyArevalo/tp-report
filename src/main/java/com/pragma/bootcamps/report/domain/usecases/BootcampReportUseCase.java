package com.pragma.bootcamps.report.domain.usecases;

import com.pragma.bootcamps.report.domain.api.BootcampReportServicePort;
import com.pragma.bootcamps.report.domain.clients.CapabilityClientPort;
import com.pragma.bootcamps.report.domain.clients.PersonClientPort;
import com.pragma.bootcamps.report.domain.clients.TechnologyClientPort;
import com.pragma.bootcamps.report.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.report.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.report.domain.models.BootcampCreated;
import com.pragma.bootcamps.report.domain.models.BootcampReport;
import com.pragma.bootcamps.report.domain.spi.BootcampReportPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.pragma.bootcamps.report.domain.utils.BootcampReportUtils.buildBootcampReport;
import static com.pragma.bootcamps.report.domain.utils.BootcampReportUtils.buildCapabilityWithTechnologies;

@RequiredArgsConstructor
public class BootcampReportUseCase implements BootcampReportServicePort {

    private final BootcampReportPersistencePort bootcampReportPersistencePort;
    private final CapabilityClientPort capabilityClientPort;
    private final TechnologyClientPort technologyClientPort;
    private final PersonClientPort personClientPort;

    public Mono<Void> handleBootcampCreation(BootcampCreated bootcampCreated) {
        return capabilityClientPort.getCapabilitiesByBootcampId(bootcampCreated.getBootcampId())
                .flatMap(capability -> technologyClientPort.getTechnologiesByCapabilityId(capability.getId())
                        .collectList()
                        .map(techs -> buildCapabilityWithTechnologies(capability, techs))
                )
                .collectList()
                .map(capabilities -> buildBootcampReport(bootcampCreated, capabilities))
                .flatMap(bootcampReportPersistencePort::save)
                .then();
    }

    public Mono<Void> addPersonToBootcampReport(Long bootcampId, Long personId) {
        return personClientPort.getPersonById(personId)
                .flatMap(student -> bootcampReportPersistencePort.addStudentToReport(bootcampId, student))
                .then();
    }

    public Mono<BootcampReport> getMostSuccessfulBootcamp() {
        return bootcampReportPersistencePort.findMostEnrolledBootcamp()
                .switchIfEmpty(Mono.error(new NotFoundException(ExceptionMessages.BOOTCAMP_NOT_FOUND.getMessage())));
    }


}
