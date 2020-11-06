package ru.yegorr.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Конфигурация аутентификации и авторизации
 */
@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private TokenAuthorizationProvider authorizationProvider;

    private JwtFilter jwtFilter;

    private static final String USER_ROLE = "USER", ADMIN_ROLE = "ADMIN";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class).httpBasic().disable().csrf().disable().
                authorizeRequests().
                antMatchers("/auth/logout").hasRole(USER_ROLE).
                antMatchers("/auth/**").permitAll().
                antMatchers(
                        HttpMethod.GET,
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html**",
                        "/webjars/**",
                        "favicon.ico",
                        "/swagger-ui/**"
                ).permitAll().
                antMatchers(HttpMethod.POST, "/users").permitAll().
                anyRequest().hasRole(USER_ROLE);
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

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
