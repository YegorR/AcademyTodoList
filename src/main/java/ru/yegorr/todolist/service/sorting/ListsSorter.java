package ru.yegorr.todolist.service.sorting;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Component;
import ru.yegorr.todolist.exception.ValidationFailsException;

import java.util.*;

@Component
@Slf4j
public class ListsSorter {

    private enum Property {
        UPDATEDATE("updateDate"), CREATIONDATE("creationDate"), NAME("name");

        private final String propertyName;

        Property(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public static Property getByPropertyName(String propertyName) {
            for (Property prop : values()) {
                if (prop.propertyName.equals(propertyName)) {
                    return prop;
                }
            }
            return null;
        }
    }

    private static final String PROPERTY_REGEXP_GROUP;

    static {
        StringBuilder group = new StringBuilder("^(");
        for (Property prop : Property.values()) {
            group.append("|").append(prop.propertyName);
        }
        group.deleteCharAt(2);
        group.append(")");
        PROPERTY_REGEXP_GROUP = group.toString();
    }

    /**
     * Parses sortQuery to get orders
     *
     * @param sortQuery sort query from client
     * @return orders
     * @throws ValidationFailsException if validation fails
     */
    public List<Order> handleSortQuery(String sortQuery) throws ValidationFailsException {
        if (sortQuery == null || sortQuery.trim().isEmpty()) {
            log.debug("SortQuery is empty");
            return List.of(Order.desc(Property.UPDATEDATE.getPropertyName()));
        }

        sortQuery = sortQuery.trim().toLowerCase().replaceAll("\\s+", "");
        Scanner scanner = new Scanner(sortQuery);
        Set<Property> usedProperties = EnumSet.noneOf(Property.class);
        List<Order> orderList = new ArrayList<>();
        while (true) {
            if (scanner.hasNext(PROPERTY_REGEXP_GROUP)) {
                throw new ValidationFailsException("Wrong sorting query");
            }
            String property = scanner.next(PROPERTY_REGEXP_GROUP);
            log.debug("Sorting property is {}", property);
            if (usedProperties.contains(Property.getByPropertyName(property))) {
                throw new ValidationFailsException(String.format("Doubling field '%s' in sorting", property));
            }
            usedProperties.add(Property.getByPropertyName(property));

            if (scanner.hasNext(":(asc|desc)")) {
                scanner.next(":");
                String order = scanner.next("(asc|desc)");
                if ("asc".equals(order)) {
                    log.debug("Property orders by asc");
                    orderList.add(Order.asc(property));
                } else {
                    log.debug("Property orders by desc");
                    orderList.add(Order.desc(property));
                }
            } else {
                log.debug("Property orders by asc");
                orderList.add(Order.asc(property));
            }

            if (!scanner.hasNext()) {
                return orderList;
            }

            if (!scanner.hasNext("\\|")) {
                throw new ValidationFailsException("Wrong sorting query");
            }
            scanner.next("\\|");
        }
    }
}
