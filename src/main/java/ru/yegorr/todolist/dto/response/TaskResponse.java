package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.entity.Priority;
import ru.yegorr.todolist.serializer.LocalDateTimeSerializer;

import java.time.*;
import java.util.UUID;

/**
 * Response for task requests
 */
@Data
@ApiModel(value = "Task", description = "Task information")
public class TaskResponse {

    private UUID id;

    private UUID listId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    private String name;

    private String description;

    private Priority priority;

    private boolean done;
}
