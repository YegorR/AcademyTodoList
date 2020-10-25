package ru.yegorr.todolist.service.filtering;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.*;
import java.util.*;

/**
 * Specification для фильтрации
 *
 * @param <T> тип сущности
 */
public class FilterSpecification<T> implements Specification<T> {

    private final Action action;

    private final UUID foreignKey;

    private final String foreignParent;

    /**
     * Конструктор
     *
     * @param action действие фильтрации
     */
    public FilterSpecification(Action action) {
        this(action, null, null);
    }

    /**
     * Конструктор, когда нужно искать также по внешнему ключу
     *
     * @param action действие фильтрации
     * @param foreignKey внешний ключ
     * @param foreignParent свойство внешнего ключа
     */
    public FilterSpecification(Action action, UUID foreignKey, String foreignParent) {
        this.action = action;
        this.foreignKey = foreignKey;
        this.foreignParent = foreignParent;
    }

    private Predicate[] getPredicatesFromActions(List<Action> actions, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return actions.stream()
                .map(act -> new FilterSpecification<T>(act).toPredicate(root, query, criteriaBuilder))
                .toArray(Predicate[]::new);
    }

    @Override
    public Predicate toPredicate(
            Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder
    ) {
        Predicate result = getForeignPredicate(root, criteriaBuilder);
        if (action == null) {
            return result;
        }
        return criteriaBuilder.and(
                result,
                switch (action.getActionType()) {
                    case AND -> criteriaBuilder.and(getPredicatesFromActions(action.getActions(), root, query, criteriaBuilder));
                    case OR -> criteriaBuilder.or(getPredicatesFromActions(action.getActions(), root, query, criteriaBuilder));
                    case NOT -> criteriaBuilder.not(new FilterSpecification<T>(action.getActions().get(0)).toPredicate(root, query, criteriaBuilder));
                    case EQUAL -> criteriaBuilder.equal(root.get(action.getProperty()), action.getValue());
                    case NOT_EQUAL -> criteriaBuilder.notEqual(root.get(action.getProperty()), action.getValue());
                    case MORE, MORE_OR_EQUAL, LESS, LESS_OR_EQUAL -> {
                        Object value = action.getValue();
                        if (value instanceof LocalDateTime) {
                            yield moreOrLessPredicate(root, criteriaBuilder, (LocalDateTime)value);
                        } else if (value instanceof Integer) {
                            yield moreOrLessPredicate(root, criteriaBuilder, (Integer)value);
                        } else if (value instanceof Boolean) {
                            yield moreOrLessPredicate(root, criteriaBuilder, (Boolean)value);
                        } else if (value instanceof String) {
                            yield moreOrLessPredicate(root, criteriaBuilder, (String)value);
                        } else {
                            throw new IllegalStateException("Unexpected value type in filter");
                        }
                    }
                    case LIKE -> {
                        Object value = action.getValue();
                        if (!(value instanceof String)) {
                            throw new IllegalStateException(action.getProperty() + " must be string");
                        }
                        String str = (String)value;
                        yield criteriaBuilder.like(criteriaBuilder.upper(root.get(action.getProperty())), String.format("%%%s%%", str.toUpperCase()));
                    }
                }
        );
    }

    private <Y extends Comparable<? super Y>> Predicate moreOrLessPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Y object) {
        return switch (action.getActionType()) {
            case MORE -> criteriaBuilder.greaterThan(root.get(action.getProperty()), object);
            case LESS -> criteriaBuilder.lessThan(root.get(action.getProperty()), object);
            case MORE_OR_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(root.get(action.getProperty()), object);
            case LESS_OR_EQUAL -> criteriaBuilder.lessThanOrEqualTo(root.get(action.getProperty()), object);
            default -> throw new IllegalStateException("Not excepted state: " + action.getActionType());
        };
    }

    private Predicate getForeignPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (foreignKey != null && foreignParent != null) {
            return criteriaBuilder.equal(root.join(foreignParent).get("id"), foreignKey);
        } else {
            return criteriaBuilder.and();
        }
    }
}
