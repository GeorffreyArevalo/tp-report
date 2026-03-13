package com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.docs;

import com.pragma.bootcamps.report.infrastructure.entrypoints.reactiveweb.dtos.responses.BusinessResponse;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class ReportOpenApi {

    private static final String TAG = "Report";

    private static final String GET_MOST_SUCCESSFUL_CODE = String.valueOf(HttpStatus.OK.value());
    private static final String NOT_FOUND_CODE = String.valueOf(HttpStatus.NOT_FOUND.value());

    private static final String GET_MOST_SUCCESSFUL_DESC = "Most successful bootcamp retrieved successfully";
    private static final String NOT_FOUND_DESC = "Most successful bootcamp not found";

    private static final String OPERATION_GET_MOST_SUCCESSFUL = "getMostSuccessful";
    private static final String OPERATION_GET_MOST_SUCCESSFUL_DESC = "Retrieves the bootcamp with the highest number of enrollments";


    public void getMostSuccessful(Builder builder) {
        builder
                .operationId(OPERATION_GET_MOST_SUCCESSFUL)
                .description(OPERATION_GET_MOST_SUCCESSFUL_DESC)
                .tag(TAG)
                .response(responseBuilder()
                        .responseCode(GET_MOST_SUCCESSFUL_CODE)
                        .description(GET_MOST_SUCCESSFUL_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))
                .response(responseBuilder()
                        .responseCode(NOT_FOUND_CODE)
                        .description(NOT_FOUND_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))));
    }
}
