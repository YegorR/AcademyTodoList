package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.entity.Priority;
import ru.yegorr.todolist.serializer.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.*;
import java.util.*;

/**
 * Ответ на запрос получения списка
 */
@Data
@ApiModel(value = "Список(ответ)", description = "Полная информация о списке и заданиях в нём")
public class FullTaskListResponse implements Serializable {

    private UUID id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    private int closedTasksCount;

    private int openedTasksCount;

    private long totalTasksCount;

    private boolean closed;

    private short color;

    private Priority priority;

    private UUID userId;

    private List<TaskResponse> tasks;
}
