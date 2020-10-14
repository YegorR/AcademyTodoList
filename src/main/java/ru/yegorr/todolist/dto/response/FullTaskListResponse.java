package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.serializer.LocalDateSerializer;

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

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate updateDate;

    private int closedTasksCount;

    private int openedTasksCount;

    private List<TaskResponse> tasks;
}
