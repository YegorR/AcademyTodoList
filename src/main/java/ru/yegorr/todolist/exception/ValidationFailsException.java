package ru.yegorr.todolist.exception;

/**
 * Another one exception for validation failing - if our application (not validation library) finds validation error
 */
public class ValidationFailsException extends ApplicationException {

    /**
     * Constructor
     * @param message message
     */
    public ValidationFailsException(String message) {
        super(message);
    }
}
