package ru.yegorr.todolist.exception;

/**
 * Исключение "доступ к ресурсу запрещён"
 */
public class ForbiddenException extends ApplicationException {

    /**
     * Конструктор
     */
    public ForbiddenException() {
        super("Access is forbidden");
    }
}
