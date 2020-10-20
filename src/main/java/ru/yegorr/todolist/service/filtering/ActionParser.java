package ru.yegorr.todolist.service.filtering;

import ru.yegorr.todolist.exception.ValidationFailsException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.yegorr.todolist.service.filtering.Action.ActionType.*;

/**
 * Reads condition from filter query
 */
public class ActionParser {

    public enum PropertyType {
        STRING, INTEGER, DATE, BOOLEAN
    }

    private final Map<String, PropertyType> propType;

    private final Map<String, String> properties;

    /**
     * Constructor
     *
     * @param propType   mapping property in entity - type
     * @param properties mapping property in query - property in entity
     */
    public ActionParser(Map<String, PropertyType> propType, Map<String, String> properties) {
        this.propType = propType;
        this.properties = properties;
    }

    /**
     * Parses filter query and get action
     *
     * @param filter filter query
     * @return Action
     * @throws ValidationFailsException if query is wrong
     */
    public Action parse(String filter) throws ValidationFailsException {
        if (filter == null || filter.trim().isEmpty()) {
            return null;
        }

        filter = filter.trim().toLowerCase();
        for (String prop : properties.keySet()) {
            filter = filter.replaceAll(prop, properties.get(prop));
        }

        Scanner scanner = new Scanner(filter);
        try {
            Action action =  readAction(scanner);
            if (scanner.hasNext()) {
                throw new ValidationFailsException("Wrong filter");
            }
            return action;
        } catch (NoSuchElementException ex) {
            throw new ValidationFailsException("Wrong filter");
        }
    }

    private Action readAction(Scanner scanner) throws ValidationFailsException {
        String operation = scanner.next();
        Action.ActionType actionType = switch (operation) {
            case "=" -> EQUAL;
            case ">" -> MORE;
            case "<" -> LESS;
            case ">=" -> MORE_OR_EQUAL;
            case "<=" -> LESS_OR_EQUAL;
            case "&" -> AND;
            case "|" -> OR;
            case "!" -> NOT;
            default -> throw new ValidationFailsException("Wrong filter");
        };

        switch (actionType) {
            case AND, OR -> {
                Action action1 = readAction(scanner);
                Action action2 = readAction(scanner);
                return new Action(actionType, List.of(action1, action2));
            }
            case NOT -> {
                Action action = readAction(scanner);
                return new Action(actionType, List.of(action));
            }
            case EQUAL, MORE, LESS, MORE_OR_EQUAL, LESS_OR_EQUAL -> {
                return readFinalAction(scanner, actionType);
            }

            default -> throw new IllegalStateException("Unexpected value: " + actionType);
        }
    }

    private Action readFinalAction(Scanner scanner, Action.ActionType actionType) throws ValidationFailsException {
        String prop = scanner.next();
        if (!propType.containsKey(prop)) {
            throw new ValidationFailsException("Wrong filter");
        }
        Object value = readValue(scanner, propType.get(prop));
        return new Action(actionType, prop, value);
    }

    private static Object readValue(Scanner scanner, ActionParser.PropertyType propertyType) throws ValidationFailsException {
        switch (propertyType) {
            case STRING -> {
                if (!scanner.hasNext("'.*")) {
                    throw new ValidationFailsException("Wrong filter");
                }
                String value = scanner.findInLine("'.*?[^\\\\]'");
                if (value == null) {
                    throw new ValidationFailsException("Wrong filter");
                }
                return value.substring(1, value.length() - 1).replace("\\'", "'");
            }
            case INTEGER -> {
                return scanner.nextInt();
            }
            case DATE -> {
                String dateString = scanner.next();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return LocalDate.parse(dateString, formatter);
            }
            case BOOLEAN -> {
                return scanner.nextBoolean();
            }
            default -> throw new IllegalStateException("Unexpected value: " + propertyType);
        }
    }
}
