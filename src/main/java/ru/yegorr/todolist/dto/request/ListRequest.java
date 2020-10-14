package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Request body for create or change list
 */
@Data
@ApiModel(value = "ListRequest", description = "List data for creating or changing")
public class ListRequest {
    @NotBlank(message = "{name.notblank}")
    @Length(max = 4096, message = "{name.length}")
    private String name;
}
