package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Response for get list request
 */
@Data
@ApiModel(value = "List", description = "Full information about list and tasks in it")
public class FullTaskListResponse {

    private long id;

    private String name;

    private LocalDate creationDate;

    private LocalDate updateDate;

    private int closedTasksCount;

    private int openedTasksCount;

    private List<TaskResponse> tasks;
}
