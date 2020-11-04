package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yegorr.todolist.entity.Role;

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

    @NotBlank(message = "{nickname.notblank}")
    @Length(max = 256, message = "{nickname.length}")
    private String nickname;

    @NotBlank(message = "{password.notblank")
    @Length(max = 256, message = "{password.length}")
    private String password;

    @NotBlank(message = "{phone.notblank}")
    @Pattern(regexp = "^((\\+7|7|8)+([0-9]){10})$", message = "{phone.wrong}")
    private String phone;

    private Role role;
}
