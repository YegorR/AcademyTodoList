package ru.yegorr.todolist.service;

import ru.yegorr.todolist.dto.request.UserRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.exception.NotFoundException;

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
    UserResponse createUser(UserRequest userRequest);

    /**
     * Изменить пользователя
     *
     * @param userRequest данные о пользователя
     * @param userId id пользователя
     * @return UserResponse
     * @throws NotFoundException пользователь не найден
     */
    UserResponse changeUser(UserRequest userRequest, UUID userId) throws NotFoundException;

    /**
     * Получить пользователя
     *
     * @param userId id пользователя
     * @return UserResponse
     * @throws NotFoundException пользователь не найден
     */
    UserResponse getUser(UUID userId) throws NotFoundException;

    /**
     * Получить всех пользователей
     *
     * @return UserResponse
     */
    UsersResponse getAllUsers();

    /**
     * Удалить пользователя
     *
     * @param userId id пользователя
     * @throws NotFoundException пользователь не найден
     */
    void deleteUser(UUID userId) throws NotFoundException;
}
