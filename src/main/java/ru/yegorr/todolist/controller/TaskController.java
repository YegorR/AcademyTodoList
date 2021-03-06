package ru.yegorr.todolist.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.TaskResponse;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.service.TaskService;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Контроллер для запросов заданий
 */
@RestController
@Api(tags = "Задания")
public class TaskController {

    private final TaskService taskService;

    /**
     * Конструктор
     *
     * @param taskService сервис заданий
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Создаёт новое задание
     *
     * @param createTaskRequest данные для нового задания
     * @param listId id списка
     * @param userId id пользователя
     * @return TaskResponse
     */
    @PostMapping("/lists/{listId}/todos")
    @ApiOperation("Создать новоё задание")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Задание создано"),
            @ApiResponse(code = 404, message = "Список не найден")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @RequestBody @ApiParam("Данные для нового задания") @Valid CreateTaskRequest createTaskRequest,
            @PathVariable("listId") @ApiParam("id списка") UUID listId,
            @RequestParam("userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return taskService.createTask(createTaskRequest, listId, userId);
    }

    /**
     * Возвращает задание
     *
     * @param taskId id задания
     * @param listId id списка
     * @param userId id пользователя
     * @return задание
     */
    @GetMapping("/lists/{listId}/todos/{id}")
    @ApiOperation("Получить задание")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Задание возвращено"),
            @ApiResponse(code = 404, message = "Задание или список не найдены")
    })
    public TaskResponse getTask(
            @PathVariable("id") @ApiParam("id задания") UUID taskId,
            @PathVariable("listId") @ApiParam("id списка") UUID listId,
            @RequestParam("userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return taskService.getTask(taskId, listId, userId);
    }

    /**
     * Изменяет задание
     *
     * @param changeTaskRequest данные для изменения задания
     * @param taskId            id задания
     * @param listId id списка
     * @param userId id пользователя
     * @return TaskResponse
     */
    @PutMapping("/lists/{listId}/todos/{id}")
    @ApiOperation("Изменить задание")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Задание изменено"),
            @ApiResponse(code = 404, message = "Задание или список не найдены")
    })
    public TaskResponse changeTask(
            @RequestBody @ApiParam("Данные для изменения задания") @Valid ChangeTaskRequest changeTaskRequest,
            @PathVariable("id") @ApiParam("id задания") UUID taskId,
            @PathVariable("listId") @ApiParam("id возможно нового списка") UUID listId,
            @RequestParam("userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return taskService.changeTask(changeTaskRequest, taskId, listId, userId);
    }

    /**
     * Отмечает задание как выполненное
     *
     * @param taskId id задания
     * @param listId id списка
     * @param userId id пользователя
     */
    @PostMapping("/lists/{listId}/todos/{id}/markDone")
    @ApiOperation("Отметить задание выполненным")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Задание отмечено выполненным"),
            @ApiResponse(code = 404, message = "Задание не найдено")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markDone(
            @PathVariable("id") @ApiParam("id задания") UUID taskId,
            @PathVariable("listId") @ApiParam("id списка") UUID listId,
            @RequestParam("userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        taskService.markDone(taskId, listId, userId);
    }

    /**
     * Удаляет задание
     *
     * @param taskId id задания
     * @param listId id списка
     * @param userId id пользователя
     */
    @DeleteMapping("/lists/{listId}/todos/{id}")
    @ApiOperation("Удалить задание")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Задание удалено"),
            @ApiResponse(code = 404, message = "Задание не найдено")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable("id") @ApiParam("id задания") UUID taskId,
            @PathVariable("listId") @ApiParam("id списка") UUID listId,
            @RequestParam("userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        taskService.deleteTask(taskId, listId, userId);
    }
}
