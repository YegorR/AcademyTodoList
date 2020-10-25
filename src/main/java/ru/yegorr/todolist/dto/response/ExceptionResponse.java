package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Ответ на ошибки-исключения
 */
@ApiModel(value = "Исключение", description = "Описание исключения")
@Data
public class ExceptionResponse {
    private int httpCode;

    private String message;
}
