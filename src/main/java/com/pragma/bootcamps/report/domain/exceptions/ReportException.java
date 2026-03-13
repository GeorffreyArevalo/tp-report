package com.pragma.bootcamps.report.domain.exceptions;

import com.pragma.bootcamps.report.domain.enums.ExceptionStatusCode;
import lombok.Getter;

@Getter
public class ReportException extends RuntimeException{

    private final ExceptionStatusCode statusCode;
    private final int status;

    public ReportException(ExceptionStatusCode statusCode, String message, int status) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }

}
