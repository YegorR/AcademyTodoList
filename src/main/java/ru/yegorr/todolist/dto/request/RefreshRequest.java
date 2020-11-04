package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Запрос для обновления токенов
 */
@Data
@ApiModel("Запрос для обновления токенов")
public class RefreshRequest {
    @NotBlank(message = "{refreshToken.notBlank}")
    private String refreshToken;
}
