package ru.yegorr.todolist.dto.request;

import lombok.Data;

/**
 * Запрос для обновления токенов
 */
@Data
public class RefreshRequest {
    private String refreshToken;
}
