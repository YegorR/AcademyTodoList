package ru.yegorr.todolist.dto.response;

import lombok.Data;

/**
 * Response for exceptions
 */
@Data
public class ExceptionResponse {
    private int httpCode;

    private String message;
}
