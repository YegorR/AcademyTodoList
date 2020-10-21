package ru.yegorr.todolist.service;

import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;
import ru.yegorr.todolist.exception.NotFoundException;

import java.util.UUID;

/**
 * Service for tasks
 */
public interface TaskService {

    /**
     * Creates new task
     *
     * @param createTaskRequest new task data
     * @return created task data
     */
    TaskResponse createTask(CreateTaskRequest createTaskRequest) throws NotFoundException;

    /**
     * Changes the tasks
     *
     * @param changeTaskRequest the task data for changing
     * @param taskId            task id
     * @return changed task data
     */
    TaskResponse changeTask(ChangeTaskRequest changeTaskRequest, UUID taskId) throws NotFoundException;

    /**
     * Deletes the task
     *
     * @param taskId task id
     */
    void deleteTask(UUID taskId) throws NotFoundException;

    /**
     * Marks the task as done
     * @param taskId task id
     */
    void markDone(UUID taskId) throws NotFoundException;
}
