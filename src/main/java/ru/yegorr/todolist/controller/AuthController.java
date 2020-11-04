package ru.yegorr.todolist.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.AuthenticateResponse;
import ru.yegorr.todolist.exception.ApplicationException;
import ru.yegorr.todolist.service.AuthService;

import javax.validation.Valid;

/**
 * Контроллер для аутентификации
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "Аутентификация")
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
    @ApiOperation("Пройти аутентификацию")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Аутентификация прошла успешно"),
                    @ApiResponse(code = 401, message = "Аутентификация провалилась")
            }
    )
    public AuthenticateResponse authenticate(@RequestBody @Valid @ApiParam("Логин/пароль") AuthenticateRequest authenticateRequest)
            throws ApplicationException {
        return authService.doAuthentication(authenticateRequest);
    }

    /**
     * Обновить токены
     *
     * @param refreshRequest refresh token
     * @return токены
     */
    @PostMapping("/refresh")
    @ApiOperation("Обновить токены")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Токены обновлены"),
                    @ApiResponse(code = 401, message = "Аутентификация провалилась")
            }
    )
    public AuthenticateResponse refresh(@RequestBody @Valid @ApiParam("Refresh token") RefreshRequest refreshRequest) throws ApplicationException {
        return authService.refresh(refreshRequest);
    }

    /**
     * Удаляет refresh token
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Сбросить refresh token")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Refresh token сбросен")
    })
    public void logout() throws ApplicationException {
        authService.logout();
    }
}
