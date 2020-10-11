package ru.yegorr.todolist.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import ru.yegorr.todolist.dto.request.ListRequest;

/**
 * Controller for task lists request
 */
@RestController
public class TaskListController {

    /**
     * Returns all lists
     *
     * @param limit  max count of lists in result
     * @param sort   how result must be sorted, see details in api.md
     * @param filter filter of results, see details in api.md
     * @return ListsResponse
     */
    @GetMapping("/lists")
    public EntityResponse<?> getLists(
            @RequestParam(value = "limit", required = false) byte limit, @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        return null;
    }

    /**
     * Return list with tasks
     *
     * @param listId list id
     * @return FullTaskListResponse
     */
    @GetMapping("/list/{id}")
    public EntityResponse<?> getList(@PathVariable("id") long listId) {
        return null;
    }

    /**
     * Creates new list
     *
     * @param taskList name of new list
     * @return TaskListResponse
     */
    @PostMapping("/list")
    public EntityResponse<?> createList(@RequestBody ListRequest taskList) {
        return null;
    }

    /**
     * Change the list
     *
     * @param taskList name of new list
     * @param listId   list id
     * @return TaskListResponse
     */
    @PutMapping("/list/{id}")
    public EntityResponse<?> changeList(@RequestBody ListRequest taskList, @PathVariable("id") long listId) {
        return null;
    }

    /**
     * Delete the list
     *
     * @param listId list id
     * @return no body
     */
    @DeleteMapping("/list/{id}")
    public EntityResponse<?> deleteList(@PathVariable("id") long listId) {
        return null;
    }
}
