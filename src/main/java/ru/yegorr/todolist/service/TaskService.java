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
     * Создать новое задание
     *
     * @param createTaskRequest данные для нового задания
     * @return TaskResponse
     */
    TaskResponse createTask(CreateTaskRequest createTaskRequest, UUID listId) throws NotFoundException;

    /**
     * Изменить задание
     *
     * @param changeTaskRequest данные для изменения задания
     * @param taskId            id задания
     * @return TaskResponse
     */
    TaskResponse changeTask(ChangeTaskRequest changeTaskRequest, UUID taskId, UUID listId) throws NotFoundException;

    /**
     * Удалить задание
     *
     * @param taskId id задания
     */
    void deleteTask(UUID taskId) throws NotFoundException;

    /**
     * Отметить задание как выполненное
     * @param taskId id задания
     */
    void markDone(UUID taskId, UUID listId) throws NotFoundException;
}
