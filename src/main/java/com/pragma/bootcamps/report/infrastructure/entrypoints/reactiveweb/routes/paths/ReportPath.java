package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.routes.paths;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class ReportPath {
    private String bootcampTopEnrolled;
}
