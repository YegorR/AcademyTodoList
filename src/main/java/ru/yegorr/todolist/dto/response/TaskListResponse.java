package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.serializer.LocalDateSerializer;

import java.time.LocalDate;

/**
 * Task list response for create or change task list
 */
@Data
@ApiModel(value = "BriefList", description = "Brief list information")
public class TaskListResponse {
    private long id;

    private String name;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate updateDate;

}
