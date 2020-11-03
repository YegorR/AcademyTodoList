package ru.yegorr.todolist.dto.response;

import lombok.Data;

/**
 * Ответ при успешной аутентификации
 */
@Data
public class AuthenticateResponse {
    private String accessToken;

    private String refreshToken;
}
