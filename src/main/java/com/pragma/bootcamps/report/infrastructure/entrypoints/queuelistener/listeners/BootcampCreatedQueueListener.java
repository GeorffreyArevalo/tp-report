package com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.listeners;

import com.pragma.bootcamps.report.domain.api.BootcampReportServicePort;
import com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.dtos.BootcampCreatedDto;
import com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.mappers.BootcampReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootcampCreatedQueueListener {

    private final BootcampReportServicePort bootcampReportServicePort;
    private final BootcampReportMapper mapper;

    @RabbitListener(queues = { "${queue.rabbitmq.name-created-queue}" })
    public Mono<Void> receive(@Payload BootcampCreatedDto data) {

        return Mono.just(data)
                .doOnNext(bootcampCreated -> log.info("Received QUEUE message: {}", bootcampCreated))
                .map(mapper::toModel)
                .doOnNext(bootcampCreated -> log.info("Processing BootcampId={} with name={}", bootcampCreated.getBootcampId(), bootcampCreated.getName()))
                .flatMap(bootcampReportServicePort::handleBootcampCreation)
                .doOnSuccess(unused -> log.info("Successfully processed QUEUE message: {}", data))
                .doOnError(error -> log.error("Error processing QUEUE message: {}", data, error))
                .then();

    }

}
