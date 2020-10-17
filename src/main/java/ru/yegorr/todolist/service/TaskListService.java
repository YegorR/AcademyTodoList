package ru.yegorr.todolist.service;

import ru.yegorr.todolist.dto.request.ListRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.exception.*;

/**
 * Service for task lists
 */
public interface TaskListService {

    /**
     * Gets task lists
     *
     * @param limit  max count of lists, if more 100 than 10
     * @param sort   how lists must be sorted, nullable
     * @param filter how lists must be filtered, nullable
     * @return all lists satisfying limit, sort and filter
     */
    ListsResponse getLists(Integer limit, String sort, String filter) throws ValidationFailsException;

    /**
     * Gets list with tasks
     *
     * @param listId list id
     * @return list with tasks
     */
    FullTaskListResponse getList(long listId) throws NotFoundException;

    /**
     * Creates new list
     *
     * @param listRequest new list data
     * @return Created list data
     */
    TaskListResponse createList(ListRequest listRequest);

    /**
     * Changes the list
     *
     * @param listRequest list data for changing
     * @param listId      list id
     * @return changed list data
     */
    TaskListResponse changeList(ListRequest listRequest, long listId) throws NotFoundException;

    /**
     * Deletes the list
     * @param listId list id
     */
    void deleteList(long listId) throws NotFoundException;
}
