package ru.yegorr.todolist.dto.request;

import lombok.Data;

/**
 * Change task request
 */
@Data
public class ChangeTaskRequest {

    private long listId;

    private String name;

    private String description;

    private byte priority;

    private boolean done;
}
