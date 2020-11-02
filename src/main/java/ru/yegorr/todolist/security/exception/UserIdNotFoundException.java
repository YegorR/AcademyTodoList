package ru.yegorr.todolist.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Исключение, когда id пользователя в токене не найден
 */
public class UserIdNotFoundException extends AuthenticationException {

    /**
     * Конструктор
     *
     * @param msg сообщение
     * @param t причина
     */
    public UserIdNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Конструктор
     *
     * @param msg сообщение
     */
    public UserIdNotFoundException(String msg) {
        super(msg);
    }
}
