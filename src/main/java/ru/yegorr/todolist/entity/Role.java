package ru.yegorr.todolist.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Роль пользователя в системе
 */
public enum Role {
    ROLE_ADMIN, ROLE_USER;

    /**
     * Метод для Jackson JSON - десериализатор
     *
     * @param value строка
     * @return Role
     */
    @JsonCreator
    public static Role fromString(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return valueOf(value.toUpperCase());
    }
}
