package com.pragma.bootcamps.report.domain.exceptions;

import com.pragma.bootcamps.report.domain.enums.ExceptionStatusCode;

public class TechnologyMicroserviceException extends ReportException {
    public TechnologyMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}
