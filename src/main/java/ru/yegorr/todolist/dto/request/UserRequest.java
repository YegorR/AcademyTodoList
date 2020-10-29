package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Запрос для создания или редактирования пользователя
 */
@Data
@ApiModel(value = "Пользователь")
public class UserRequest implements Serializable {
    @Email(message = "{email.wrong}")
    private String email;

    @NotBlank(message = "{nickname.notblank")
    @Length(max = 256, message = "{nickname.length}")
    private String nickname;

    @NotBlank(message = "{password.notblank")
    @Length(max = 256, message = "{password.length}")
    private String password;

    @NotBlank(message = "{password.notblank}")
    @Pattern(regexp = "^(\\\\+\\\\d{1,3}( )?)?((\\\\(\\\\d{3}\\\\))|\\\\d{3})[- .]?\\\\d{3}[- .]?\\\\d{4}$")
    private String phone;
}
