package ru.yegorr.todolist.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;
import ru.yegorr.todolist.exception.NotFoundException;
import ru.yegorr.todolist.service.TaskService;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Controller for tasks requests
 */
@RestController
@Api(tags = "Tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Controller
     *
     * @param taskService taskService
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates new task
     *
     * @param createTaskRequest new task data
     * @return TaskResponse
     */
    @PostMapping("/task")
    @ApiOperation("Create new task")
    @ApiResponses({
            @ApiResponse(code = 201, message = "The task is created"),
            @ApiResponse(code = 404, message = "The list is not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@RequestBody @ApiParam("New task data") @Valid CreateTaskRequest createTaskRequest) throws NotFoundException {
        return taskService.createTask(createTaskRequest);
    }

    /**
     * Changes the task
     *
     * @param changeTaskRequest task data dor changing
     * @param taskId            task id
     * @return TaskResponse
     */
    @PutMapping("/task/{id}")
    @ApiOperation("Change the task")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The task is changed"),
            @ApiResponse(code = 404, message = "The task or the list is not found")
    })
    public TaskResponse changeTask(
            @RequestBody @ApiParam("Task data for changing") @Valid ChangeTaskRequest changeTaskRequest, @PathVariable("id") @ApiParam("Task id") UUID taskId
    ) throws NotFoundException {
        return taskService.changeTask(changeTaskRequest, taskId);
    }

    /**
     * Mark the task done
     *
     * @param taskId task id
     */
    @PostMapping("/mark-done/{id}") // TODO(Шайдуко): хоть так в задании и написано, но лучше использовать camelCase
    @ApiOperation("Mark the task done")
    @ApiResponses({
            @ApiResponse(code = 204, message = "The task is marked"),
            @ApiResponse(code = 404, message = "The task is not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markDone(@PathVariable("id") @ApiParam("Task id") UUID taskId) throws NotFoundException {
        taskService.markDone(taskId);
    }

    /**
     * Delete the task
     *
     * @param taskId task id
     */
    @DeleteMapping("/task/{id}")
    @ApiOperation("Delete the task")
    @ApiResponses({
            @ApiResponse(code = 204, message = "The task is removed"),
            @ApiResponse(code = 404, message = "The task is not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") @ApiParam("Task id") UUID taskId) throws NotFoundException {
        taskService.deleteTask(taskId);
    }
}
