package ru.yegorr.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.NotFoundException;
import ru.yegorr.todolist.repository.*;

import java.time.LocalDate;
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
            throw new NotFoundException(String.format("List %d", createTaskRequest.getListId()));
        }

        TaskEntity task = new TaskEntity();
        task.setName(createTaskRequest.getName());
        task.setDescription(createTaskRequest.getDescription());
        task.setPriority(createTaskRequest.getPriority());

        TaskListEntity taskListEntity = new TaskListEntity();
        taskListEntity.setId(createTaskRequest.getListId());
        task.setTaskList(taskListEntity);

        LocalDate date = LocalDate.now();
        task.setCreationDate(date);
        task.setUpdateDate(date);
        task.setDone(false);

        task = taskRepository.save(task);
        return generateTaskResponseFromEntity(task);
    }

    @Override
    public TaskResponse changeTask(ChangeTaskRequest changeTaskRequest, long taskId) throws NotFoundException {
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new NotFoundException(String.format("Task %d", taskId));
        }
        if (!taskListRepository.existsById(changeTaskRequest.getListId())) {
            throw new NotFoundException(String.format("List %d", changeTaskRequest.getListId()));
        }

        TaskEntity task = taskOptional.get();
        task.setName(changeTaskRequest.getName());
        task.setDescription(changeTaskRequest.getDescription());
        task.setDone(changeTaskRequest.isDone());

        long oldListId = task.getTaskList().getId();
        if (oldListId != changeTaskRequest.getListId()) {
            TaskListEntity newTaskList = taskListRepository.findById(changeTaskRequest.getListId()).orElseThrow(
                    () -> new NotFoundException(String.format("List %d", changeTaskRequest.getListId())));
            TaskListEntity oldTaskList = task.getTaskList();
            newTaskList.getTasks().add(task);
            oldTaskList.getTasks().remove(task);
            task.setTaskList(newTaskList);
            taskListRepository.saveAll(Arrays.asList(newTaskList, oldTaskList));
        }

        task = taskRepository.save(task);
        return generateTaskResponseFromEntity(task);
    }

    @Override
    public void deleteTask(long taskId) throws NotFoundException {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException(String.format("Task %d", taskId));
        }

        taskRepository.deleteById(taskId);
    }

    private static TaskResponse generateTaskResponseFromEntity(TaskEntity entity) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(entity.getId());
        taskResponse.setName(entity.getName());
        taskResponse.setDescription(entity.getDescription());
        taskResponse.setCreationDate(entity.getCreationDate());
        taskResponse.setUpdateDate(entity.getUpdateDate());
        taskResponse.setPriority(entity.getPriority());
        taskResponse.setDone(entity.isDone());
        taskResponse.setListId(entity.getTaskList().getId());
        return taskResponse;
    }
}
