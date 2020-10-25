package ru.yegorr.todolist.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Приоритет задания
 */
public enum Priority {
    VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH;

    /**
     * Метод для Jackson JSON - десериализатор
     *
     * @param value строка
     * @return Priority
     */
    @JsonCreator
    public static Priority fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
