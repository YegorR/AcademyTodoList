package ru.yegorr.todolist.service.sorting;

import org.springframework.stereotype.Component;
import ru.yegorr.todolist.exception.ValidationFailsException;

import java.util.*;
import java.util.regex.*;

import static org.springframework.data.domain.Sort.Order;

/**
 * Do Sort.Orders with sorting query
 */
@Component
public class ListsSorter {

    /**
     * Do Sort.Orders for sortQuery
     *
     * @param sortQuery sort Query
     * @param props map, where key - property in query, value - property in entity
     * @return List Sort.Order; null - if there is no sortQuery
     * @throws ValidationFailsException if validation fails
     */
    public List<Order> handleSortQuery(String sortQuery, Map<String, String> props) throws ValidationFailsException {
        if (sortQuery == null || sortQuery.trim().isEmpty()) {
            return null;
        }

        sortQuery = sortQuery.trim().toLowerCase().replaceAll("\\s+", "");
        StringBuilder queryBuilder = new StringBuilder(sortQuery);
        Set<String> usedProperties = new HashSet<>();
        List<Order> orderList = new ArrayList<>();
        final Pattern propertyGroupPattern = getPropertyRegexpGroup(props.keySet());
        final Pattern ascDescGroupPattern = Pattern.compile(":(asc|desc)");

        while (true) {
            Matcher matcher = propertyGroupPattern.matcher(queryBuilder.toString());
            if (!matcher.find()) {
                throw new ValidationFailsException("Wrong sorting query");
            }
            String property = matcher.group(0);
            if (usedProperties.contains(property)) {
                throw new ValidationFailsException(String.format("Doubling field '%s' in sorting", property));
            }
            usedProperties.add(property);
            queryBuilder.delete(0, matcher.end(0));

            matcher = ascDescGroupPattern.matcher(queryBuilder.toString());
            if (matcher.find()) {
                if (":asc".equals(matcher.group(0))) {
                    orderList.add(Order.asc(props.get(property)));
                } else {
                    orderList.add(Order.desc(props.get(property)));
                }
                queryBuilder.delete(0, matcher.end(0));
            } else {
                orderList.add(Order.asc(props.get(property)));
            }

            if (queryBuilder.toString().isEmpty()) {
                return orderList;
            }

            if (queryBuilder.indexOf(",") != 0) {
                throw new ValidationFailsException("Wrong sorting query");
            }
            queryBuilder.deleteCharAt(0);
        }
    }

    private static Pattern getPropertyRegexpGroup(Iterable<String> props) {
        StringBuilder group = new StringBuilder("^(");
        for (String prop : props) {
            group.append("|").append(prop);
        }
        group.deleteCharAt(2);
        group.append(")");
        return Pattern.compile(group.toString());
    }
}
