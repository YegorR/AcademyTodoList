package ru.yegorr.todolist.service;

import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;
import ru.yegorr.todolist.exception.NotFoundException;

import java.util.UUID;

/**
 * Сервис заданий
 */
public interface TaskService {

    /**
     * Получить задание
     *
     * @param taskId id задания
     * @param listId id листа
     * @return задание
     * @throws NotFoundException если задание или лист не найдены
     */
    TaskResponse getTask(UUID taskId, UUID listId) throws NotFoundException;

    /**
     * Создать новое задание
     *
     * @param createTaskRequest данные для нового задания
     * @param listId id листа
     * @return TaskResponse
     */
    TaskResponse createTask(CreateTaskRequest createTaskRequest, UUID listId) throws NotFoundException;

    /**
     * Изменить задание
     *
     * @param changeTaskRequest данные для изменения задания
     * @param taskId            id задания
     * @param listId id листа
     * @return TaskResponse
     */
    TaskResponse changeTask(ChangeTaskRequest changeTaskRequest, UUID taskId, UUID listId) throws NotFoundException;

    /**
     * Удалить задание
     *
     * @param taskId id задания
     * @param listId id листа
     */
    void deleteTask(UUID taskId, UUID listId) throws NotFoundException;

    /**
     * Отметить задание как выполненное
     * @param taskId id задания
     * @param listId id листа
     */
    void markDone(UUID taskId, UUID listId) throws NotFoundException;
}
