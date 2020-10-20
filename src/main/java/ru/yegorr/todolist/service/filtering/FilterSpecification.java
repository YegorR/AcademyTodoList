package ru.yegorr.todolist.service.filtering;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
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
            case MORE -> criteriaBuilder.greaterThan(root.get(action.getProperty()), action.getValue().toString());
            case LESS -> criteriaBuilder.lessThan(root.get(action.getProperty()), action.getValue().toString());
            case MORE_OR_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(root.get(action.getProperty()), action.getValue().toString());
            case LESS_OR_EQUAL -> criteriaBuilder.lessThanOrEqualTo(root.get(action.getProperty()), action.getValue().toString());
            case NOT_EQUAL -> criteriaBuilder.notEqual(root.get(action.getProperty()), action.getValue());
        };
    }
}
