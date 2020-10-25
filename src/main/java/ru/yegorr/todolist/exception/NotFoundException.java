package ru.yegorr.todolist.exception;

/**
 * Исключение для ситуаций "не найдено"
 */
public class NotFoundException extends ApplicationException {

    /**
     * @param message тип или название ненайденного объекта
     */
    public NotFoundException(String message) {
        super(message);
    }
}
