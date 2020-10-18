package ru.yegorr.todolist.service.filtering;

import lombok.Value;

/**
 * @param <T> Action for filtering
 */
@Value
public class Action<T> {
    String key;

    T value;

    String operator;
}
