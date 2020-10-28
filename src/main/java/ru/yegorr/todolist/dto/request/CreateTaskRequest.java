package ru.yegorr.todolist.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yegorr.todolist.entity.Priority;
import ru.yegorr.todolist.serializer.LocalDateDeserializer;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Запрос на создание задание
 */
@Data
@ApiModel(value = "Создание задания", description = "Данные для создания задания")
public class CreateTaskRequest implements Serializable {
    @NotBlank(message = "{name.notblank}")
    @Length(max = 4096, message = "{name.length}")
    private String name;

    @Length(max = 4096, message = "{description.length")
    private String description;

    @NotNull(message = "{priority.notnull}")
    private Priority priority;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate destinationDate;
}
