package ru.yegorr.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.AuthenticateResponse;
import ru.yegorr.todolist.exception.ApplicationException;
import ru.yegorr.todolist.service.AuthService;

import javax.validation.Valid;

/**
 * Контроллер для аутентификации
 */
@RestController("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Конструктор
     *
     * @param authService authService
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Получить токены
     *
     * @param authenticateRequest логин и пароль
     * @return токены
     */
    @PostMapping("/login")
    public AuthenticateResponse authenticate(@RequestBody @Valid AuthenticateRequest authenticateRequest) throws ApplicationException {
        return authService.doAuthentication(authenticateRequest);
    }

    /**
     * Обновить токены
     *
     * @param refreshRequest refresh token
     * @return токены
     */
    @PostMapping("/refresh")
    public AuthenticateResponse refresh(@RequestBody @Valid RefreshRequest refreshRequest) throws ApplicationException {
        return authService.refresh(refreshRequest);
    }

    /**
     * Удаляет refresh token
     */
    @PostMapping("/logout")
    public void logout() throws ApplicationException {
        authService.logout();
    }
}
