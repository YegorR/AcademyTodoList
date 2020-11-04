package ru.yegorr.todolist.service;

import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.exception.*;

import java.util.UUID;

/**
 * Сервис для работы с пользователями
 */
public interface UserService {

    /**
     * Создать пользователя
     *
     * @param userRequest данные о пользователе
     * @return UserResponse
     */
    UserResponse createUser(UserRequest userRequest) throws ApplicationException;

    /**
     * Изменить пользователя
     *
     * @param userRequest данные о пользователя
     * @param userId      id пользователя
     * @return UserResponse
     */
    UserResponse changeUser(UserRequest userRequest, UUID userId) throws ApplicationException;

    /**
     * Получить пользователя
     *
     * @param userId id пользователя
     * @return UserResponse
     */
    UserResponse getUser(UUID userId) throws ApplicationException;

    /**
     * Получить всех пользователей
     *
     * @return UserResponse
     */
    UsersResponse getAllUsers() throws ApplicationException;

    /**
     * Удалить пользователя
     *
     * @param userId id пользователя
     */
    void deleteUser(UUID userId) throws ApplicationException;
}
