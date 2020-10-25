package ru.yegorr.todolist.exception;

/**
 * Общее исключение для ошибок в приложении
 */
public class ApplicationException extends Exception {

    /**
     * @param message Сообщение исключения
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * @param message Сообщение исключения
     * @param cause   Причина исключения
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
