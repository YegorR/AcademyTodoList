package ru.yegorr.todolist.dto.request;

import lombok.Data;

/**
 * Запрос для аутентификации
 */
@Data
public class AuthenticateRequest {
    private String email;

    private String password;
}
