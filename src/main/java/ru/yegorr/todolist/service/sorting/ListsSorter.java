package ru.yegorr.todolist.service.sorting;

import ru.yegorr.todolist.exception.ValidationFailsException;

import java.util.*;
import java.util.regex.*;

import static org.springframework.data.domain.Sort.Order;

/**
 * Делает Sort.Orders с помощью строки-запроса сортировки
 */
public class ListsSorter {

    private final Set<String> props;

    private final Map<String, String> queryPropsMapping;

    /**
     * Конструктор
     *
     * @param props свойства
     * @param queryPropsMapping маппинг "свойства в строке - свойства в entity"; если совпадают, то добавлять не обязательно
     */
    public ListsSorter(Set<String> props, Map<String, String> queryPropsMapping) {
        this.props = props;
        this.queryPropsMapping = queryPropsMapping;
    }

    /**
     * Составляет Sort.Orders с помощью строки сортировки
     *
     * @param sortQuery строка сортировки
     * @return Sort.Orders
     * @throws ValidationFailsException строка сортировка составлена неверно
     */
    public List<Order> handleSortQuery(String sortQuery) throws ValidationFailsException {
        if (sortQuery == null || sortQuery.trim().isEmpty()) {
            return null;
        }

        sortQuery = sortQuery.trim().toLowerCase().replaceAll("\\s+", "");
        for (String prop: queryPropsMapping.keySet()) {
            sortQuery = sortQuery.replaceAll(prop, queryPropsMapping.get(prop));
        }
        StringBuilder queryBuilder = new StringBuilder(sortQuery);
        Set<String> usedProperties = new HashSet<>();
        List<Order> orderList = new ArrayList<>();
        final Pattern propertyGroupPattern = getPropertyRegexpGroup(props);
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
                    orderList.add(Order.asc(property));
                } else {
                    orderList.add(Order.desc(property));
                }
                queryBuilder.delete(0, matcher.end(0));
            } else {
                orderList.add(Order.asc(property));
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
