package ru.yegorr.todolist.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Response for get list request
 */
@Data
public class FullTaskListResponse {

    private long id;

    private String name;

    private LocalDate creationDate;

    private LocalDate updateDate;

    private int closedTasksCount;

    private int openedTasksCount;

    private List<TaskListResponse> tasks;
}
