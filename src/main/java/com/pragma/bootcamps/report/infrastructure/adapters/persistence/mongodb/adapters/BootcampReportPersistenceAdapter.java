package com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.adapters;

import com.pragma.bootcamps.report.domain.models.BootcampReport;
import com.pragma.bootcamps.report.domain.models.StudentReport;
import com.pragma.bootcamps.report.domain.spi.BootcampReportPersistencePort;
import com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.documents.BootcampReportDocument;
import com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.mappers.BootcampReportDocumentMapper;
import com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.repositories.BootcampReportReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampReportPersistenceAdapter implements BootcampReportPersistencePort {

    private final BootcampReportReactiveRepository bootcampReportReactiveRepository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final BootcampReportDocumentMapper mapper;

    @Override
    public Mono<BootcampReport> save(BootcampReport bootcampReport) {
        return Mono.just(bootcampReport)
                .map(mapper::toDocument)
                .doOnNext( document -> log.info("Saving Document: {}", document) )
                .flatMap(bootcampReportReactiveRepository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> addStudentToReport(Long bootcampId, StudentReport student) {
        var query = new Query(Criteria.where("bootcampId").is(bootcampId)
                .and("students.id").ne(student.getId()));

        var update = new Update()
                .push("students", student)
                .inc("enrolledStudentCount", 1);

        return mongoTemplate.updateFirst(query, update, BootcampReportDocument.class)
                .then();
    }

    @Override
    public Mono<BootcampReport> findMostEnrolledBootcamp() {
        var query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "enrolledStudentCount"))
                .limit(1);

        return mongoTemplate.findOne(query, BootcampReportDocument.class)
                .map(mapper::toDomain);
    }
}
