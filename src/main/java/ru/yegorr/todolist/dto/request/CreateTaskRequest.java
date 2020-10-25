package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yegorr.todolist.entity.Priority;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Запрос на создание задание
 */
@Data
@ApiModel(value = "Создание задания", description = "Данные для создания задания")
public class CreateTaskRequest implements Serializable {

    @NotNull(message = "{id.notnull}")
    private UUID listId;

    @NotBlank(message = "{name.notblank}")
    @Length(max = 4096, message = "{name.length}")
    private String name;

    @Length(max = 4096, message = "{description.length")
    private String description;

    @NotNull(message = "{priority.notnull}")
    private Priority priority;
}
