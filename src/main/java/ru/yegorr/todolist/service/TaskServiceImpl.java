package ru.yegorr.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.repository.*;

import java.time.*;
import java.util.*;

/**
 * Реализация TaskService
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskListRepository taskListRepository;

    /**
     * Конструктор
     *
     * @param taskRepository     репозиторий заданий
     * @param taskListRepository репозиторий списков
     */
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest createTaskRequest, UUID listId, UUID userId) throws ApplicationException {
        TaskListEntity taskListEntity = checkRightAndGetList(listId, userId);

        TaskEntity task = new TaskEntity();
        task.setId(UUID.randomUUID());
        task.setName(createTaskRequest.getName());
        task.setDescription(createTaskRequest.getDescription());
        task.setPriority(createTaskRequest.getPriority());
        task.setTaskList(taskListEntity);
        task.setDestinationDate(createTaskRequest.getDestinationDate());

        LocalDateTime time = LocalDateTime.now();
        task.setCreationTime(time);
        task.setUpdateTime(time);
        task.setDone(false);

        task = taskRepository.save(task);
        return generateTaskResponseFromEntity(task, userId);
    }

    @Override
    public TaskResponse changeTask(ChangeTaskRequest changeTaskRequest, UUID taskId, UUID listId, UUID userId) throws ApplicationException {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new NotFoundException(String.format("Task %s", taskId));
        }
        TaskListEntity newTaskList = checkRightAndGetList(listId, userId);
        TaskEntity task = taskOptional.get();
        TaskListEntity oldTaskList = checkRightAndGetList(task.getTaskList().getId(), userId);

        task.setName(changeTaskRequest.getName());
        task.setDescription(changeTaskRequest.getDescription());
        task.setDone(changeTaskRequest.isDone());
        task.setUpdateTime(LocalDateTime.now());
        task.setPriority(changeTaskRequest.getPriority());
        task.setDestinationDate(changeTaskRequest.getDestinationDate());

        if (!oldTaskList.equals(newTaskList)) {
            newTaskList.getTasks().add(task);
            oldTaskList.getTasks().remove(task);
            task.setTaskList(newTaskList);
        }

        return generateTaskResponseFromEntity(task, userId);
    }

    @Override
    public void deleteTask(UUID taskId, UUID listId, UUID userId) throws ApplicationException {
        TaskEntity task = getAndCheckTask(taskId, listId, userId);
        taskRepository.delete(task);
    }

    private static TaskResponse generateTaskResponseFromEntity(TaskEntity entity, UUID userId) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(entity.getId());
        taskResponse.setName(entity.getName());
        taskResponse.setDescription(entity.getDescription());
        taskResponse.setCreationTime(entity.getCreationTime());
        taskResponse.setUpdateTime(entity.getUpdateTime());
        taskResponse.setPriority(entity.getPriority());
        taskResponse.setDone(entity.isDone());
        taskResponse.setListId(entity.getTaskList().getId());
        taskResponse.setDestinationDate(entity.getDestinationDate());
        taskResponse.setUserId(userId);
        return taskResponse;
    }

    @Override
    public void markDone(UUID taskId, UUID listId, UUID userId) throws ApplicationException {
        TaskEntity task = getAndCheckTask(taskId, listId, userId);
        task.setDone(true);
    }

    @Override
    public TaskResponse getTask(UUID taskId, UUID listId, UUID userId) throws ApplicationException {
        TaskEntity task = getAndCheckTask(taskId, listId, userId);
        return generateTaskResponseFromEntity(task, userId);
    }

    private TaskEntity getAndCheckTask(UUID taskId, UUID listId, UUID userId) throws ApplicationException {
        TaskListEntity taskListEntity = checkRightAndGetList(listId, userId);
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException(String.format("Task %s", taskId)));
        if (!taskListEntity.getTasks().contains(task)) {
            throw new NotFoundException(String.format("Task %s in list %s", taskId, listId));
        }
        return task;
    }

    private TaskListEntity checkRightAndGetList(UUID listId, UUID userId) throws ApplicationException {
        TaskListEntity taskListEntity = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %s", listId)));
        if (!taskListEntity.getUser().getId().equals(userId)) {
            throw new ForbiddenException();
        }
        return taskListEntity;
    }
}
