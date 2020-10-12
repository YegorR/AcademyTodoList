package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Response for exceptions
 */
@ApiModel(value = "Exception", description = "Exception description")
@Data
public class ExceptionResponse {
    private int httpCode;

    private String message;
}
