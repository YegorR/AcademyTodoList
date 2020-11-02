package ru.yegorr.todolist.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Исключение, когда токен не прошёл валидацию
 */
public class BadTokenException extends AuthenticationException {

    /**
     * Конструктор
     *
     * @param msg сообщение
     * @param t причина
     */
    public BadTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Конструктор
     *
     * @param msg сообщение
     */
    public BadTokenException(String msg) {
        super(msg);
    }
}
