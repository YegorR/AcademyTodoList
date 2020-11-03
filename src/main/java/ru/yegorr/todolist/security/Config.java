package ru.yegorr.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;

/**
 * Конфигурация аутентификации и авторизации
 */
@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {

    private TokenAuthorizationProvider authorizationProvider;

    private JwtFilter jwtFilter;

    private static final String USER_ROLE = "USER", ADMIN_ROLE = "ADMIN";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(jwtFilter).authorizeRequests().
                antMatchers("/auth/logout").hasRole(USER_ROLE).
                antMatchers("/auth/**").permitAll().
                antMatchers( "/**").hasAnyRole(USER_ROLE, ADMIN_ROLE);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authorizationProvider);
    }

    /**
     * @param authorizationProvider TokenAuthorizationProvider
     */
    @Autowired
    public void setAuthorizationProvider(TokenAuthorizationProvider authorizationProvider) {
        this.authorizationProvider = authorizationProvider;
    }

    /**
     * @param jwtFilter JwtFilter
     */
    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
}
