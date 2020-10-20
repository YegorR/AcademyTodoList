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
     * @param offset number of the first result, nullable
     * @return all lists satisfying limit, offset, sort and filter
     * @throws ValidationFailsException filter or sort wrong
     */
    ListsResponse getLists(Integer limit, String sort, String filter, Integer offset) throws ValidationFailsException;

    /**
     * Get list and its tasks
     *
     * @param listId list id
     * @param limit  max count of lists, if more 100 than 10
     * @param sort   how lists must be sorted, nullable
     * @param filter how lists must be filtered, nullable
     * @param offset number of the first result, nullable
     * @return list and its tasks satisfying limit, offset, sort and filter
     * @throws NotFoundException no list with this id
     * @throws ValidationFailsException filter or sort wrong
     */
    FullTaskListResponse getList(long listId, Integer limit, String sort, String filter, Integer offset) throws NotFoundException, ValidationFailsException;

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
