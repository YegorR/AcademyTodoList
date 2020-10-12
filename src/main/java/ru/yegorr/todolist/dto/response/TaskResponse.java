package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;

/**
 * Response for task requests
 */
@Data
@ApiModel(value = "Task", description = "Task information")
public class TaskResponse {

    private long id;

    private long listId;

    private LocalDate creationDate;

    private LocalDate updateDate;

    private String name;

    private String description;

    private byte priority;

    private boolean done;
}
