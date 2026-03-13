package com.pragma.bootcamps.report.domain.exceptions;

import com.pragma.bootcamps.report.domain.enums.ExceptionStatusCode;

public class NotFoundException extends ReportException{

    public NotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }

}
