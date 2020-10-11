package ru.yegorr.todolist.dto.request;

import lombok.Data;

/**
 * Create task request
 */
@Data
public class CreateTaskRequest {

    private long listId;

    private String name;

    private String description;

    private byte priority;
}
