package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Запрос на изменение или создание списка
 */
@Data
@ApiModel(value = "Список", description = "Данные для изменения или создания списка")
public class ListRequest implements Serializable {

    @NotBlank(message = "{name.notblank}")
    @Length(max = 4096, message = "{name.length}")
    private String name;

    @Range(min = 0, max = 255, message = "{color.range}")
    private Short color;
}
