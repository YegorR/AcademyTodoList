package ru.yegorr.todolist.exception;

/**
 * Normal exception for errors in application
 */
public class ApplicationException extends Exception {

    /**
     * @param message exception message
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * @param message exception message
     * @param cause   exception-cause
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
