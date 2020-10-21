package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.serializer.LocalDateSerializer;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response for task requests
 */
@Data
@ApiModel(value = "Task", description = "Task information")
public class TaskResponse {

    private UUID id;

    private UUID listId;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate updateDate;

    private String name;

    private String description;

    private byte priority;

    private boolean done;
}
