package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Ответ на запрос о всех пользователях
 */
@Data
@ApiModel(value = "Пользователи")
public class UsersResponse {
    private List<UserResponse> users;
}
