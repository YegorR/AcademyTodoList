package ru.yegorr.todolist.security;

import org.springframework.security.core.*;

import java.util.*;

/**
 * Реализация Authentication
 */
public class TokenAuthentication implements Authentication {

    private final String token;

    private boolean authenticated;

    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    private UUID userId;

    /**
     * Конструктор
     *
     * @param token JWT
     */
    public TokenAuthentication(String token) {
        this.token = token;
    }

    /**
     * Сеттер для userId
     *
     * @param userId id пользователя
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Сеттер для прав пользователя
     *
     * @param authorities права пользователя
     */
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * Возвращает JWT
     * @return JWT
     */
    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    /**
     * @return UUID id пользователя
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }
}
