package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;

/**
 * Task list response for create or change task list
 */
@Data
@ApiModel(value = "BriefList", description = "Brief list information")
public class TaskListResponse {
    private long id;

    private String name;

    private LocalDate creationDate;

    private LocalDate updateDate;

}
