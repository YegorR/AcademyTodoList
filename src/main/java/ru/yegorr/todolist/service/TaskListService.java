package ru.yegorr.todolist.service;

import ru.yegorr.todolist.dto.request.ListRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.exception.*;

import java.util.UUID;

/**
 * Сервис для спискрв
 */
public interface TaskListService {

    /**
     * Получить списки
     *
     * @param limit  максимальное количество списков; есть больше 100, то 10
     * @param sort   как сортировать, nullable
     * @param filter как фильтровать, nullable
     * @param offset номер первого результата, nullable
     * @return списки
     */
    ListsResponse getLists(Integer limit, String sort, String filter, Integer offset, UUID userId) throws ApplicationException;

    /**
     * Получить список и задания в нём
     *
     * @param listId id списка
     * @param limit  максимальное количество списков; есть больше 100, то 10
     * @param sort   как сортировать, nullable
     * @param filter как фильтровать, nullable
     * @param offset номер первого результата, nullable
     * @return список и задания в неём
     */
    FullTaskListResponse getList(UUID listId, Integer limit, String sort, String filter, Integer offset, UUID userId) throws ApplicationException;

    /**
     * Создать новый список
     *
     * @param listRequest данные для нового списка
     * @return TaskListResponse
     */
    TaskListResponse createList(ListRequest listRequest, UUID userId) throws ApplicationException;

    /**
     * Изменить список
     *
     * @param listRequest данные для изменения списка
     * @param listId      id списка
     * @return TaskListResponse
     */
    TaskListResponse changeList(ListRequest listRequest, UUID listId, UUID userId) throws ApplicationException;

    /**
     * Удалить список
     * @param listId id списка
     */
    void deleteList(UUID listId, UUID userId) throws ApplicationException;

    /**
     * Удалить все списки
     */
    void deleteAllLists();
}
