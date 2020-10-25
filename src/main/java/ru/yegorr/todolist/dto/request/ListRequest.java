package ru.yegorr.todolist.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Запрос на изменение или создание списка
 */
@Data
@ApiModel(value = "Список", description = "Данные для изменения или создания списка")
public class ListRequest {

    @NotBlank(message = "{name.notblank}")
    @Length(max = 4096, message = "{name.length}")
    private String name;
}
