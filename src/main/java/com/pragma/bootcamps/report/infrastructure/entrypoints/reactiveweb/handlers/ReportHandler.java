package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.report.domain.api.BootcampReportServicePort;
import com.pragma.bootcamps.report.domain.enums.ExceptionStatusCode;
import com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.mappers.ReportRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Component
@RequiredArgsConstructor
public class ReportHandler {


    private final BootcampReportServicePort bootcampReportServicePort;
    private final ReportRequestMapper mapper;

    public Mono<ServerResponse> listenGetMostSuccessful(ServerRequest request) {
        return bootcampReportServicePort.getMostSuccessfulBootcamp()
                .map(mapper::toBootcampReportDto)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.OK.status(), dto))
                );
    }

}
