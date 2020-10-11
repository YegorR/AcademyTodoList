package ru.yegorr.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.*;

/**
 * Controller for tasks requests
 */
@RestController
public class TaskController {

    /**
     * Creates new task
     *
     * @param createTaskRequest new task data
     * @return TaskResponse
     */
    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
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
    public ResponseEntity<?> changeTask(@RequestBody ChangeTaskRequest changeTaskRequest, @PathVariable("id") long taskId) {
        return null;
    }

    /**
     * Do the task done
     *
     * @param taskId task id
     * @return no body
     */
    @PostMapping("/mark-done/{id}")
    public ResponseEntity<?> markDone(@PathVariable("id") long taskId) {
        return null;
    }

    /**
     * Delete the task
     *
     * @param taskId task id
     * @return no body
     */
    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") long taskId) {
        return null;
    }
}
