package ru.yegorr.todolist.dto.request;

import lombok.Data;

/**
 * Request body for create or change list
 */
@Data
public class ListRequest {
    private String name;
}
