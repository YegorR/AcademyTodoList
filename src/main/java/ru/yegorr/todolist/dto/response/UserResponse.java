package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.entity.Role;

import java.util.UUID;

/**
 * Ответ на запрос о пользователе
 */
@Data
@ApiModel(value = "Пользователь (ответ)")
public class UserResponse {
    private UUID userId;

    private String email;

    private String nickname;

    private String phone;

    private Role role;
}
