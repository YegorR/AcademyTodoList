package ru.yegorr.todolist.service.filtering;

import org.springframework.data.jpa.domain.Specification;
import ru.yegorr.todolist.exception.ValidationFailsException;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Specification for filtering
 *
 * @param <T> entity type
 */
public class FilterSpecification<T> implements Specification<T> {

    private final Action action;

    /**
     * Constructor
     *
     * @param action action
     */
    public FilterSpecification(Action action) {
        this.action = action;
    }

    private Predicate[] getPredicatesFromActions(List<Action> actions, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return (Predicate[])actions.stream()
                .map(act -> new FilterSpecification<T>(act).toPredicate(root, query, criteriaBuilder))
                .toArray();
    }

    @Override
    public Predicate toPredicate(
            Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder
    ) {
        if (action == null) {
            return null;
        }
        return switch (action.getActionType()) {
            case AND -> criteriaBuilder.and(getPredicatesFromActions(action.getActions(), root, query, criteriaBuilder));
            case OR -> criteriaBuilder.or(getPredicatesFromActions(action.getActions(), root, query, criteriaBuilder));
            case NOT -> criteriaBuilder.not(new FilterSpecification<T>(action.getActions().get(0)).toPredicate(root, query, criteriaBuilder));
            case EQUAL -> criteriaBuilder.equal(root.get(action.getProperty()), action.getValue());
            case NOT_EQUAL -> criteriaBuilder.notEqual(root.get(action.getProperty()), action.getValue());
            case MORE, MORE_OR_EQUAL, LESS, LESS_OR_EQUAL -> {
                Object value = action.getValue();
                if (value instanceof LocalDate) {
                    yield moreOrLessPredicate(root, criteriaBuilder, (LocalDate)value);
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
        };
    }

    private <Y extends Comparable<? super Y>> Predicate moreOrLessPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Y object) {
        return switch (action.getActionType()) {
            case MORE -> criteriaBuilder.greaterThan(root.get(action.getProperty()), object);
            case LESS -> criteriaBuilder.lessThan(root.get(action.getProperty()), object);
            case MORE_OR_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(root.get(action.getProperty()), object);
            case LESS_OR_EQUAL -> criteriaBuilder.lessThanOrEqualTo(root.get(action.getProperty()), object);
            default -> throw new IllegalStateException("Not exception state: " + action.getActionType());
        };
    }
}
