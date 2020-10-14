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

    @NotBlank(message = "{name.notblank}")
    @Length(max = 4096, message = "{name.length}")
    private String name;

    @Length(max = 4096, message = "{description.length")
    private String description;

    @Min(value = 0, message = "{priority.min}")
    @Max(value = 4, message = "{priority.max}")
    private byte priority;
}
