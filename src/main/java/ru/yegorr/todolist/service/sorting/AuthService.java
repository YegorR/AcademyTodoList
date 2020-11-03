package ru.yegorr.todolist.service.sorting;

import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.AuthenticateResponse;
import ru.yegorr.todolist.exception.ApplicationException;

/**
 * Сервис для работы с токенами
 */
public interface AuthService {

    /**
     * Проводит аутентификацию
     *
     * @param authenticateRequest логин пароль
     * @return токены
     */
    AuthenticateResponse doAuthentication(AuthenticateRequest authenticateRequest) throws ApplicationException;

    /**
     * Обновляет токены
     *
     * @param refreshRequest refresh token
     * @return токены
     */
    AuthenticateResponse refresh(RefreshRequest refreshRequest) throws ApplicationException;

    /**
     * Делает refresh token недействительным
     */
    void logout() throws ApplicationException;
}
