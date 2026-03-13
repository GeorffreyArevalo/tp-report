package com.pragma.bootcamps.report.domain.exceptions;

import com.pragma.bootcamps.report.domain.enums.ExceptionStatusCode;

public class PersonMicroserviceException extends ReportException{
    public PersonMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}
