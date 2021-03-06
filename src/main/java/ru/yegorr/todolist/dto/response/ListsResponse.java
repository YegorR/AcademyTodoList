package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Ответ на запрос о получении списков
 */
@Data
@ApiModel(value = "Списки", description = "Информация о списках")
public class ListsResponse implements Serializable {
    private int openedListsCount;

    private int closedListCount;

    private long totalListsCount;

    private List<TaskListResponse> lists;
}
