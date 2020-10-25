package ru.yegorr.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.NotFoundException;
import ru.yegorr.todolist.repository.*;

import java.time.*;
import java.util.*;

/**
 * Implementation of TaskService
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskListRepository taskListRepository;

    /**
     * Constructor
     *
     * @param taskRepository     taskRepository
     * @param taskListRepository taskListRepository
     */
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest createTaskRequest) throws NotFoundException {
        if (!taskListRepository.existsById(createTaskRequest.getListId())) {
            throw new NotFoundException(String.format("List %s", createTaskRequest.getListId()));
        }

        TaskEntity task = new TaskEntity();
        task.setId(UUID.randomUUID());
        task.setName(createTaskRequest.getName());
        task.setDescription(createTaskRequest.getDescription());
        task.setPriority(createTaskRequest.getPriority());

        TaskListEntity taskListEntity = new TaskListEntity();
        taskListEntity.setId(createTaskRequest.getListId());
        // TODO(Шайдуко): тут лучше поднимать из репозитория, чем создавать новый объект,
        //  хотя оно конечно работает и так
        task.setTaskList(taskListEntity);

        LocalDateTime time = LocalDateTime.now();
        task.setCreationTime(time);
        task.setUpdateTime(time);
        task.setDone(false);

        task = taskRepository.save(task);
        return generateTaskResponseFromEntity(task);
    }

    @Override
    public TaskResponse changeTask(ChangeTaskRequest changeTaskRequest, UUID taskId) throws NotFoundException {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new NotFoundException(String.format("Task %s", taskId));
        }
        if (!taskListRepository.existsById(changeTaskRequest.getListId())) {
            throw new NotFoundException(String.format("List %s", changeTaskRequest.getListId()));
        }

        TaskEntity task = taskOptional.get();
        task.setName(changeTaskRequest.getName());
        task.setDescription(changeTaskRequest.getDescription());
        task.setDone(changeTaskRequest.isDone());
        task.setUpdateTime(LocalDateTime.now());
        task.setPriority(changeTaskRequest.getPriority());

        UUID oldListId = task.getTaskList().getId();
        if (!oldListId.equals(changeTaskRequest.getListId())) {
            TaskListEntity newTaskList = taskListRepository.findById(changeTaskRequest.getListId()).orElseThrow(
                    () -> new NotFoundException(String.format("List %s", changeTaskRequest.getListId())));
            TaskListEntity oldTaskList = task.getTaskList();
            newTaskList.getTasks().add(task);
            oldTaskList.getTasks().remove(task);
            task.setTaskList(newTaskList);
        }

        return generateTaskResponseFromEntity(task);
    }

    @Override
    public void deleteTask(UUID taskId) throws NotFoundException {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException(String.format("Task %s", taskId));
        }

        taskRepository.deleteById(taskId);
    }

    private static TaskResponse generateTaskResponseFromEntity(TaskEntity entity) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(entity.getId());
        taskResponse.setName(entity.getName());
        taskResponse.setDescription(entity.getDescription());
        taskResponse.setCreationTime(entity.getCreationTime());
        taskResponse.setUpdateTime(entity.getUpdateTime());
        taskResponse.setPriority(entity.getPriority());
        taskResponse.setDone(entity.isDone());
        taskResponse.setListId(entity.getTaskList().getId());
        return taskResponse;
    }

    @Override
    public void markDone(UUID taskId) throws NotFoundException {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException(String.format("Task %s", taskId)));
        task.setDone(true);
    }
}
