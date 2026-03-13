package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.routes;

import com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.docs.ReportOpenApi;
import com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.handlers.ReportHandler;
import com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.routes.paths.ReportPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class ReportRouter {

    private final ReportPath reportPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ReportHandler handler) {
        return route()
                .GET(reportPath.getBootcampTopEnrolled(), handler::listenGetMostSuccessful, ReportOpenApi::getMostSuccessful)
                .build();
    }

}
