package com.pragma.bootcamps.report.infrastructure.adapters.webclient.adapters;

import com.pragma.bootcamps.report.domain.clients.TechnologyClientPort;
import com.pragma.bootcamps.report.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.report.domain.exceptions.TechnologyMicroserviceException;
import com.pragma.bootcamps.report.domain.models.TechnologyReport;
import com.pragma.bootcamps.report.infrastructure.adapters.webclient.dtos.responses.TechnologyListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TechnologyMicroserviceClientAdapter implements TechnologyClientPort {

    private static final String GET_TECHNOLOGIES_URL = "/tech/capabilities/{capabilityId}/techs";

    @Value("${adapter.clients.clients.tech.base-url}")
    private String techMicroserviceBaseUrl;

    private final WebClient client;

    @Override
    public Flux<TechnologyReport> getTechnologiesByCapabilityId(Long capabilityId) {
        return client.get()
                .uri(String.format("%s%s", techMicroserviceBaseUrl, GET_TECHNOLOGIES_URL), capabilityId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new TechnologyMicroserviceException(
                                        ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(body)
                                ))
                        )
                )
                .bodyToMono(TechnologyListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.data() != null ? response.data() : List.of()));
    }
}
