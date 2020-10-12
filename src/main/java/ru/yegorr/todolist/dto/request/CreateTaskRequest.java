package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Create task request
 */
@Data
@ApiModel(value = "CreateTask", description = "Task data for creation")
public class CreateTaskRequest {

    private long listId;

    private String name;

    private String description;

    private byte priority;
}
