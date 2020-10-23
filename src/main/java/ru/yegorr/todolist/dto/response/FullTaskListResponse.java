package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.serializer.LocalDateTimeSerializer;

import java.time.*;
import java.util.*;

/**
 * Response for get list request
 */
@Data
@ApiModel(value = "List", description = "Full information about list and tasks in it")
public class FullTaskListResponse {

    private UUID id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    private int closedTasksCount;

    private int openedTasksCount;

    private long totalTasksCount;

    private List<TaskResponse> tasks;
}
