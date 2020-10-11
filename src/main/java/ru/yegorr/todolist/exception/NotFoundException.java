package ru.yegorr.todolist.exception;

/**
 * Exception for "not found" situations
 */
public class NotFoundException extends ApplicationException {

    /**
     * @param message type or name of not found object
     */
    public NotFoundException(String message) {
        super(message);
    }
}
