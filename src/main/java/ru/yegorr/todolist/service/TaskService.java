package ru.yegorr.todolist.service;

import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;

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
    TaskResponse createTask(CreateTaskRequest createTaskRequest);

    /**
     * Changes the tasks
     *
     * @param changeTaskRequest the task data for changing
     * @param taskId            task id
     * @return changed task data
     */
    TaskResponse changeTask(ChangeTaskRequest changeTaskRequest, long taskId);

    /**
     * Deletes the task
     *
     * @param taskId task id
     */
    void deleteTask(long taskId);
}
