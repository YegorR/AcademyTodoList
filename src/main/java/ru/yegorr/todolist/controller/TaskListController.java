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

/**
 * Controller for task lists request
 */
@RestController
@Api(tags = {"Lists"})
@Validated
public class TaskListController {

    private final TaskListService taskListService;

    /**
     * Constructor
     *
     * @param taskListService taskListService
     */
    @Autowired
    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    /**
     * Returns all lists
     *
     * @param limit  max count of lists in result
     * @param sort   how result must be sorted, see details in api.md
     * @param filter filter of results, see details in api.md
     * @return ListsResponse
     */
    @GetMapping("/lists")
    @ApiOperation("Get lists")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The lists are returned")
    })
    public ListsResponse getLists(
            @RequestParam(value = "limit", required = false) @ApiParam(example = "10", value = "Max count of lists in result")
            @Positive(message = "{limit.positive}") Integer limit,
            @RequestParam(value = "sort", required = false)
            @ApiParam(example = "creation_date,update_date:desc", value = "How result must be sorted")
                    String sort,
            @RequestParam(value = "filter", required = false) @ApiParam(example = "name='Important'", value = "Filter of results") String filter,
            @RequestParam(value = "offset", required = false) @ApiParam(example = "0", value = "Offset") @PositiveOrZero(message = "{offset.positive_or_zero}")
                    Integer offset
    ) throws ValidationFailsException {
        return taskListService.getLists(limit, sort, filter, offset);
    }

    /**
     * Return list with tasks
     *
     * @param listId list id
     * @return FullTaskListResponse
     */
    @GetMapping("/list/{id}")
    @ApiOperation("Get list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The list is returned"),
            @ApiResponse(code = 404, message = "The list is not found")
    })
    public FullTaskListResponse getList(
            @PathVariable("id") @ApiParam("List id") long listId,
            @RequestParam(required = false) @ApiParam(example = "creation_date,update_date:desc", value = "How result must be sorted") String sort,
            @RequestParam(value = "limit", required = false) @ApiParam(example = "10", value = "Max count of lists in result")
            @Positive(message = "{limit.positive}") Integer limit,
            @RequestParam(value = "offset", required = false) @ApiParam(example = "0", value = "Offset") @PositiveOrZero(message = "{offset.positive_or_zero}")
                    Integer offset,
            @RequestParam(value = "filter", required = false) @ApiParam(example = "like name 'дело'", value = "Filter") String filter
    ) throws NotFoundException, ValidationFailsException {
        return taskListService.getList(listId, limit, sort, filter, offset);
    }

    /**
     * Creates new list
     *
     * @param taskList name of new list
     * @return TaskListResponse
     */
    @PostMapping("/list")
    @ApiOperation(value = "Creates new list")
    @ApiResponses({
            @ApiResponse(code = 201, message = "The list is created")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListResponse createList(@RequestBody @ApiParam("New list data") @Valid ListRequest taskList) {
        return taskListService.createList(taskList);
    }

    /**
     * Change the list
     *
     * @param taskList list data for changing
     * @param listId   list id
     * @return TaskListResponse
     */
    @PutMapping("/list/{id}")
    @ApiOperation("Change the list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The list is changed"),
            @ApiResponse(code = 404, message = "The list is not found")
    })
    public TaskListResponse changeList(
            @RequestBody @ApiParam("List data for changing") @Valid ListRequest taskList, @PathVariable("id") @ApiParam("List id") long listId
    ) throws NotFoundException {
        return taskListService.changeList(taskList, listId);
    }

    /**
     * Delete the list
     *
     * @param listId list id
     */
    @DeleteMapping("/list/{id}")
    @ApiOperation("Delete the list")
    @ApiResponses({
            @ApiResponse(code = 204, message = "The list and its tasks are removed"),
            @ApiResponse(code = 404, message = "The list is not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteList(@PathVariable("id") @ApiParam("List id") long listId) throws NotFoundException {
        taskListService.deleteList(listId);
    }
}
