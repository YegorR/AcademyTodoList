package ru.yegorr.todolist.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import ru.yegorr.todolist.serializer.LocalDateTimeSerializer;

import java.time.*;
import java.util.UUID;

/**
 * Task list response for create or change task list
 */
@Data
@ApiModel(value = "BriefList", description = "Brief list information")
public class TaskListResponse {
    private UUID id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    // TODO(Шайдуко): тут я бы ещё ввел одно поле, которое бы показыывало стату списка, чтоб
    //  фронт прямо в списке мог показать список закрыт или нет

}
