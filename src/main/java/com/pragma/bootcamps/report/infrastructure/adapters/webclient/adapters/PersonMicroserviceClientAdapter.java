package com.pragma.bootcamps.report.infrastructure.adapters.webclient.adapters;

import com.pragma.bootcamps.report.domain.clients.PersonClientPort;
import com.pragma.bootcamps.report.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.report.domain.exceptions.PersonMicroserviceException;
import com.pragma.bootcamps.report.domain.models.StudentReport;
import com.pragma.bootcamps.report.infrastructure.adapters.webclient.dtos.responses.PersonResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonMicroserviceClientAdapter implements PersonClientPort {

    private static final String GET_PERSON_URL = "/persons/{personId}";

    @Value("${adapter.clients.clients.person.base-url}")
    private String personMicroserviceBaseUrl;

    private final WebClient client;


    @Override
    public Mono<StudentReport> getPersonById(Long personId) {
        return client.get()
                .uri(String.format("%s%s", personMicroserviceBaseUrl, GET_PERSON_URL), personId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new PersonMicroserviceException(
                                        ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(body)
                                ))
                        )
                )
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PersonResponseWrapper.class)
                .map(PersonResponseWrapper::data)
                .doOnError(e -> log.error("Error calling Person Microservice for ID: {}", personId, e));
    }
}
