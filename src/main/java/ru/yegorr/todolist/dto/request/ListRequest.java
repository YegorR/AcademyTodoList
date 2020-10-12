package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Request body for create or change list
 */
@Data
@ApiModel(value = "ListRequest", description = "List data for creating or changing")
public class ListRequest {
    private String name;
}
