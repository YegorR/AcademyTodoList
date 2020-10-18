package ru.yegorr.todolist.service.filtering;

import lombok.Value;

import java.util.List;

/**
 * Action for filtering.
 * If ActionType is AND, OR, NOT, then actions must not be null or empty, else property and value must not be null or empty
 */
@Value
public class Action {

    public enum ActionType {
        AND, OR, NOT, EQUAL, MORE, LESS, MORE_OR_EQUAL, LESS_OR_EQUAL, NOT_EQUAL
    }

    /**
     * Constructor for AND, OR, NOT operators
     *
     * @param actionType actionType
     * @param actions actions
     */
    public Action(ActionType actionType, List<Action> actions) {
        this.actionType = actionType;
        this.actions = actions;
        property = null;
        value = null;
    }

    /**
     * Constructor for = and etc. operators
     *
     * @param actionType actionType
     * @param property name of property in entity
     * @param value value
     */
    public Action(ActionType actionType, String property, Object value) {
        this.actionType = actionType;
        this.property = property;
        this.value = value;
        actions = null;
    }

    ActionType actionType;

    List<Action> actions;

    String property;

    Object value;
}
