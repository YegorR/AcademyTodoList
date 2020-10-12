package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Change task request
 */
@Data
@ApiModel(value = "ChangeTask", description = "Task data for changing")
public class ChangeTaskRequest {

    private long listId;

    private String name;

    private String description;

    private byte priority;

    private boolean done;
}
