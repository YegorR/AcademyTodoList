package ru.yegorr.todolist.exception;

/**
 * Исключение для неудачной аутентификации
 */
public class NotAuthorizeException extends ApplicationException {

    /**
     * Конструктор
     *
     * @param message сообщение
     */
    public NotAuthorizeException(String message) {
        super(message);
    }
}
