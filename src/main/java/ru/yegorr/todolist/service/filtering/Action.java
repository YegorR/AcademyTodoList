package ru.yegorr.todolist.service.filtering;

import lombok.Value;

import java.util.List;

/**
 * Действие для фильтрации
 * Если ActionType - это AND, OR, NOT, то actions должен быть null или пустым, иначе property и value должны быть не пустыми и не null
 */
@Value
public class Action {

    public enum ActionType {
        AND, OR, NOT, EQUAL, MORE, LESS, MORE_OR_EQUAL, LESS_OR_EQUAL, NOT_EQUAL, LIKE
    }

    /**
     * Конструктор для операторов AND, OR, NOT
     *
     * @param actionType тип действия
     * @param actions вложенные действия
     */
    public Action(ActionType actionType, List<Action> actions) {
        this.actionType = actionType;
        this.actions = actions;
        property = null;
        value = null;
    }

    /**
     * Конструктор для "простых" операторов
     *
     * @param actionType тип действия
     * @param property название свойства в сущности
     * @param value значение
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
