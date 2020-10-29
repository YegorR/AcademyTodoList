package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.UUID;

/**
 * Ответ об авторизации
 */
@Data
@ApiModel("Результат авторизации")
public class AuthResponse {

    private boolean success;

    private UUID userId;
}
