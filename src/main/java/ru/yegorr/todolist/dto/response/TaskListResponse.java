package ru.yegorr.todolist.dto.response;

import lombok.Data;

import java.time.LocalDate;

/**
 * Task list response for create or change task list
 */
@Data
public class TaskListResponse {
    private long id;

    private String name;

    private LocalDate creationDate;

    private LocalDate updateDate;

}
