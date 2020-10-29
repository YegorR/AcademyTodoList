package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * Запрос об авторизации
 */
@Data
@ApiModel("Авторизация")
public class AuthRequest {

    @NotBlank(message = "{email.notblank}")
    @Email(message = "{email.wrong}")
    private String email;

    @NotBlank(message = "{password.notblank}")
    @Length(max = 256, message = "{password.length}")
    private String password;
}
