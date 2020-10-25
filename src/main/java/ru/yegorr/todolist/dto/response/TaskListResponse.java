package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.serializer.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.*;
import java.util.UUID;

/**
 * Ответ на запрос о создании и изменении списка
 */
@Data
@ApiModel(value = "Краткий список", description = "Краткая информация о списках")
public class TaskListResponse implements Serializable {
    private UUID id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    private boolean closed;

}
