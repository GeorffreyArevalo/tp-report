package com.pragma.bootcamps.report.infrastructure.adapters.persistence.mongodb.config;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MongoDBSecret {
    private final String uri;
}
