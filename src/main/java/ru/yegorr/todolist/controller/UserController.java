package ru.yegorr.todolist.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.exception.ApplicationException;
import ru.yegorr.todolist.service.UserService;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Контроллер для запросов пользователей
 */
@RestController
@Validated
@Api(tags = {"Пользователи"})
public class UserController {

    private final UserService userService;

    /**
     * Конструктор
     *
     * @param userService userService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Создать пользователя
     *
     * @param userRequest данные пользователя
     * @return userResponse
     */
    @PostMapping("/users")
    @ApiOperation("Создаёт пользователя")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Пользователь создан")
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @ApiParam("Данные пользователя") @Valid UserRequest userRequest) throws ApplicationException {
        return userService.createUser(userRequest);
    }

    /**
     * Изменить пользователя
     *
     * @param userRequest данные пользователя
     * @param userId id пользователя
     * @return userResponse
     */
    @PutMapping("/users/{userId}")
    @ApiOperation("Изменяет пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Пользователь изменён")
    })
    public UserResponse changeUser(
            @RequestBody @ApiParam("Данные пользователя") @Valid UserRequest userRequest,
            @PathVariable("userId") @ApiParam("id пользователя") UUID userId
    ) throws ApplicationException {
        return userService.changeUser(userRequest, userId);
    }

    /**
     * Получить пользователя
     *
     * @param userId id пользователя
     * @return userResponse
     */
    @GetMapping("/users/{userId}")
    @ApiOperation("Получает пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Пользователь получен")
    })
    public UserResponse getUser(@PathVariable("userId") @ApiParam("id пользователя") UUID userId) throws ApplicationException {
        return userService.getUser(userId);
    }

    /**
     * Получить всех пользователей
     *
     * @return usersResponse
     */
    @GetMapping("/users")
    @ApiOperation("Получает пользователей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Пользователи получены")
    })
    public UsersResponse getUsers() throws ApplicationException {
        return userService.getAllUsers();
    }

    /**
     * Удалить пользователя
     *
     * @param userId id пользователя
     */
    @DeleteMapping("/users/{userId}")
    @ApiOperation("Удаляет пользователя")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Пользователь удалён")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") @ApiParam("id пользователя") UUID userId) throws ApplicationException {
        userService.deleteUser(userId);
    }
}
