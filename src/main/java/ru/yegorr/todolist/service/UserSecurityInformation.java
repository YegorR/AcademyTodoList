package ru.yegorr.todolist.service;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Вспомогательный класс для быстрого получения id пользователя, работающего в системе
 */
public class UserSecurityInformation {

    /**
     * Получает id пользователя, работающего в системе
     *
     * @return id пользователя
     */
    public static UUID getUserId() {
        return (UUID)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
