package ru.yegorr.todolist.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.ListRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.service.TaskListService;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;

/**
 * Контроллер для запросов списков
 */
@RestController
@Api(tags = {"Списки"})
@Validated
public class TaskListController {

    private final TaskListService taskListService;

    /**
     * Конструктор
     *
     * @param taskListService сервис списков
     */
    @Autowired
    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    /**
     * Возвращает списки
     *
     * @param limit  максимальное количество списков в результате
     * @param sort   как результат должен быть отсортирован
     * @param filter как результат должен быть отфильтрован
     * @param offset смещение, с которого возвращать списки
     * @param userId id пользователя
     * @return ListsResponse списки
     */
    @GetMapping("/lists")
    @ApiOperation("Получить списки")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Списки возвращены")
    })
    public ListsResponse getLists(
            @RequestParam(value = "limit", required = false) @ApiParam(example = "10", value = "Максимальное количество списков")
            @PositiveOrZero(message = "{limit.positive_or_zero}") Integer limit,
            @RequestParam(value = "sort", required = false)
            @ApiParam(example = "creation_time,update_time:desc", value = "Сортировка") String sort,
            @RequestParam(value = "filter", required = false) @ApiParam(example = "like name 'Important'", value = "Фильтр") String filter,
            @RequestParam(value = "offset", required = false) @ApiParam(example = "0", value = "Смещение")
            @PositiveOrZero(message = "{offset.positive_or_zero}") Integer offset,
            @RequestParam(value = "userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return taskListService.getLists(limit, sort, filter, offset, userId);
    }

    /**
     * Возвращает список с заданиями
     *
     * @param listId id списка
     * @param sort   как результат должен быть отсортирован
     * @param limit  максимальное количество заданий в результате
     * @param offset смещение, с которого должны возвращаться задания
     * @param filter как результат должен быть отфильтрован
     * @param userId id пользователя
     * @return список с заданиями
     */
    @GetMapping("/lists/{id}")
    @ApiOperation("Получить лист")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Список возвращен"),
            @ApiResponse(code = 404, message = "Список не найден")
    })
    public FullTaskListResponse getList(
            @PathVariable("id") @ApiParam("id списка") UUID listId,
            @RequestParam(required = false) @ApiParam(example = "creation_time,update_time:desc", value = "Сортировка") String sort,
            @RequestParam(value = "limit", required = false) @ApiParam(example = "10", value = "Максимальное количество")
            @Positive(message = "{limit.positive}") Integer limit,
            @RequestParam(value = "offset", required = false) @ApiParam(example = "0", value = "Смещение")
            @PositiveOrZero(message = "{offset.positive_or_zero}") Integer offset,
            @RequestParam(value = "filter", required = false) @ApiParam(example = "like name 'дело'", value = "Фильтр") String filter,
            @RequestParam(value = "userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return taskListService.getList(listId, limit, sort, filter, offset, userId);
    }

    /**
     * Создаёт новый список
     *
     * @param taskList название для нового списка
     * @param userId id пользователя
     * @return TaskListResponse
     */
    @PostMapping("/lists")
    @ApiOperation(value = "Создать новый список")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Список создан")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListResponse createList(
            @RequestBody @ApiParam("Название нового списка") @Valid ListRequest taskList,
            @RequestParam(value = "userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return taskListService.createList(taskList, userId);
    }

    /**
     * Изменяет список
     *
     * @param taskList данные списка для изменения
     * @param listId   id списка
     * @param userId id пользователя
     * @return TaskListResponse
     */
    @PutMapping("/lists/{id}")
    @ApiOperation("Изменить список")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Список изменён"),
            @ApiResponse(code = 404, message = "Список не найден")
    })
    public TaskListResponse changeList(
            @RequestBody @ApiParam("Данные списка для изменения") @Valid ListRequest taskList, @PathVariable("id") @ApiParam("id листа") UUID listId,
            @RequestParam(value = "userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return taskListService.changeList(taskList, listId, userId);
    }

    /**
     * Удаляет список
     *
     * @param listId id списка
     * @param userId id пользователя
     */
    @DeleteMapping("/lists/{id}")
    @ApiOperation("Удалить список")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Список и его задания удалены"),
            @ApiResponse(code = 404, message = "Список не найден")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteList(
            @PathVariable("id") @ApiParam("id списка") UUID listId,
            @RequestParam(value = "userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        taskListService.deleteList(listId, userId);
    }

    /**
     * Удалаяет все списки и задания
     */
    @DeleteMapping("/lists")
    @ApiOperation("Удалить все списки (ОПАСНО - ИСПОЛЬЗУЙТЕ ОСТОРОЖНО)")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Все списки и задания удалены")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllLists() {
        taskListService.deleteAllLists();
    }
}
