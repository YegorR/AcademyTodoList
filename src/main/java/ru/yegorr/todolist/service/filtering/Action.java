package ru.yegorr.todolist.service.filtering;

import lombok.Value;

import java.util.List;

/**
 * Action for filtering.
 * If ActionType is AND, OR, NOT, then actions must not be null or empty, else property and value must not be null or empty
 *
 */
@Value
public class Action {
    public enum ActionType {
        AND, OR, NOT, EQUAL, MORE, LESS, MORE_OR_EQUAL, LESS_OR_EQUAL, NOT_EQUAL
    }

    ActionType actionType;

    List<Action> actions;

    String property;

    Object value;
}
