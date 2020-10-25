package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Ответ на ошибки-исключения
 */
@ApiModel(value = "Исключение", description = "Описание исключения")
@Data
public class ExceptionResponse implements Serializable {
    private int httpCode;

    private String message;
}
