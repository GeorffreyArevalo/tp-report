package com.pragma.bootcamps.report.infrastructure.adapters.webclient.adapters;

import com.pragma.bootcamps.report.domain.clients.CapabilityClientPort;
import com.pragma.bootcamps.report.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.report.domain.exceptions.CapabilityMicroserviceException;
import com.pragma.bootcamps.report.domain.models.CapabilityReport;
import com.pragma.bootcamps.report.infrastructure.adapters.webclient.dtos.responses.CapabilityListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CapabilityMicroserviceClientAdapter implements CapabilityClientPort {

    private static final String GET_CAPABILITIES_URL = "/capability/botcamps/{bootcampId}/capabilities";

    @Value("${adapter.clients.clients.capability.base-url}")
    private String capabilityMicroserviceBaseUrl;

    private final WebClient client;

    @Override
    public Flux<CapabilityReport> getCapabilitiesByBootcampId(Long bootcampId) {
        return client.get()
                .uri(String.format("%s%s", capabilityMicroserviceBaseUrl, GET_CAPABILITIES_URL), bootcampId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new CapabilityMicroserviceException(
                                        ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(body)
                                ))
                        )
                )
                .bodyToMono(CapabilityListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.data() != null ? response.data() : List.of()));
    }
}
