package ru.yegorr.todolist.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Response for get lists request
 */
@Data
public class ListsResponse {
    private int openedListsCount;

    private int closedListCount;

    private List<TaskListResponse> lists;
}
