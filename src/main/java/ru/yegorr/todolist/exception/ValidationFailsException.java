package ru.yegorr.todolist.exception;

/**
 * Another one exception for validation failing - if our application (not validation library) finds validation error
 * Ещё одно исключение для провала валидации - если приложение (не библиотека валидации) найдёт ошибку валидации
 */
public class ValidationFailsException extends ApplicationException {

    /**
     * Конструктор
     * @param message message сообщение - почему валидация провалилась
     */
    public ValidationFailsException(String message) {
        super(message);
    }
}
