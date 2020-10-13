package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * Create task request
 */
@Data
@ApiModel(value = "CreateTask", description = "Task data for creation")
public class CreateTaskRequest {

    private long listId;

    @NotBlank
    @Length(max = 4096)
    private String name;

    @Length(max = 4096)
    private String description;

    @Min(0)
    @Max(4)
    private byte priority;
}
