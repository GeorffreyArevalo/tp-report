package com.pragma.bootcamps.report.domain.exceptions;

import com.pragma.bootcamps.report.domain.enums.ExceptionStatusCode;

public class CapabilityMicroserviceException extends  ReportException{
    public CapabilityMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }

}
