package ru.yegorr.todolist.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Response for get lists request
 */
@Data
@ApiModel(value = "Lists", description = "Lists information")
public class ListsResponse {
    private int openedListsCount;

    private int closedListCount;

    private List<TaskListResponse> lists;
}
