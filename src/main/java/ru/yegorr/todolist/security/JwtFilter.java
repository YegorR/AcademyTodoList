package ru.yegorr.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import ru.yegorr.todolist.security.exception.BadTokenException;

import javax.servlet.http.*;

/**
 * Фильтр, проверяющий наличие JWT
 */
@Component
public class JwtFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * Конструктор
     */
    public JwtFilter() {
        super("/**");
    }

    private static final String AUTHORIZATION = "Authorization";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String token = readTokenFromHeader(request);
        if (token == null || token.trim().isEmpty()) {
            throw new BadTokenException("No token");
        }
        return new TokenAuthentication(token);
    }

    private static String readTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
