package ru.yegorr.todolist.exception;

/**
 * Исключение для ситуаций, когда проваливается проверка на уникальность
 */
public class UniqueCheckFallsException extends ApplicationException {

    /**
     * Конструктор
     *
     * @param message название поля, которое не уникально
     */
    public UniqueCheckFallsException(String message) {
        super(message);
    }
}
