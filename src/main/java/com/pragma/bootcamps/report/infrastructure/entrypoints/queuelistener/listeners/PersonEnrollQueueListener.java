package com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.listeners;

import com.pragma.bootcamps.report.domain.api.BootcampReportServicePort;
import com.pragma.bootcamps.report.infrastructure.entrypoints.queuelistener.dtos.PersonEnrollDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonEnrollQueueListener {

    private final BootcampReportServicePort bootcampReportServicePort;


    @RabbitListener(queues = { "${queue.rabbitmq.name-enrollment-queue}" })
    public Mono<Void> receive(@Payload PersonEnrollDto dataMessage) {

        return Mono.just(dataMessage)
                .doOnNext(data -> log.info("Processing BootcampId={} with PersonId={}", data.getBootcampId(), data.getPersonId()))
                .flatMap(data -> bootcampReportServicePort.addPersonToBootcampReport(data.getBootcampId(), data.getPersonId()))
                .doOnError(error -> log.error("Error processing SQS message: {}", dataMessage, error))
                .then();

    }


}
