package ru.yegorr.todolist.controller;

import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;

/**
 * Controller for tasks requests
 */
@RestController
@Api(tags = "Tasks")
public class TaskController {

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
    public TaskResponse createTask(@RequestBody @ApiParam("New task data") CreateTaskRequest createTaskRequest) {
        return null;
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
            @RequestBody @ApiParam("Task data for changing") ChangeTaskRequest changeTaskRequest, @PathVariable("id") @ApiParam("Task id") long taskId
    ) {
        return null;
    }

    /**
     * Mark the task done
     *
     * @param taskId task id
     */
    @PostMapping("/mark-done/{id}")
    @ApiOperation("Mark the task done")
    @ApiResponses({
            @ApiResponse(code = 204, message = "The task is marked"),
            @ApiResponse(code = 404, message = "The task is not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markDone(@PathVariable("id") @ApiParam("Task id") long taskId) {

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
    public void deleteTask(@PathVariable("id") @ApiParam("Task id") long taskId) {

    }
}
