package ru.yegorr.todolist.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Исключение, когда время токена истекло
 */
public class ExpiredTokenException extends AuthenticationException {

    /**
     * Конструктор
     *
     * @param msg сообщение
     * @param t причина
     */
    public ExpiredTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Конструктор
     *
     * @param msg сообщение
     */
    public ExpiredTokenException(String msg) {
        super(msg);
    }
}
