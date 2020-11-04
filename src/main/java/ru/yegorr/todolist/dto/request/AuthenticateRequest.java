package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * Запрос для аутентификации
 */
@Data
@ApiModel("Запрос для аутентификации")
public class AuthenticateRequest {
    @Email(message = "{email.wrong}")
    private String email;

    @NotBlank(message = "{password.notblank}")
    private String password;
}
