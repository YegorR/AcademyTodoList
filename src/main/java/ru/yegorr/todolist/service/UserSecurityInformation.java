package ru.yegorr.todolist.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.yegorr.todolist.entity.Role;
import ru.yegorr.todolist.exception.ForbiddenException;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Вспомогательный класс для быстрого получения id пользователя, работающего в системе
 */
public class UserSecurityInformation {

    private UserSecurityInformation() {
    }

    /**
     * Получает id пользователя, работающего в системе
     *
     * @return id пользователя
     */
    public static UUID getUserId() {
        return (UUID)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Проверяет, является ли пользователь админом
     *
     * @return true, если админ, иначе false
     */
    public static boolean isAdmin() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .contains(Role.ROLE_ADMIN.name());
    }

    /**
     * Проверяет, пользователь админ или определённый пользователь
     *
     * @param userId id пользователь
     * @throws ForbiddenException если не админ и не определённый пользователь
     */
    public static void checkAdminOrThisUser(UUID userId) throws ForbiddenException {
        if (!(isAdmin() || getUserId().equals(userId))) {
            throw new ForbiddenException();
        }
    }
}
