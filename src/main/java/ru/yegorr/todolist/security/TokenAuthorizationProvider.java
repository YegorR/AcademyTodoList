package ru.yegorr.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.Component;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.ValidationFailsException;
import ru.yegorr.todolist.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Компонент, проводит основую аутентификацию по фильтру
 */
@Component
public class TokenAuthorizationProvider implements AuthenticationProvider {

    private final JwtManager jwtManager;

    private final UserRepository userRepository;

    /**
     * Конструктор
     *
     * @param jwtManager     jwtManager
     * @param userRepository userRepository
     */
    @Autowired
    public TokenAuthorizationProvider(JwtManager jwtManager, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.jwtManager = jwtManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String)authentication.getCredentials();
        if (token == null) {
            return authentication;
        }
        JwtInfo jwtInfo;
        try {
            jwtInfo = jwtManager.readToken(token);
        } catch (ValidationFailsException e) {
            return authentication;
        }
        UUID userId = jwtInfo.getUserID();
        LocalDateTime expTime = jwtInfo.getExpiredTime();

        if (expTime.isBefore(LocalDateTime.now())) {
            return authentication;
        }

        TokenAuthentication tokenAuthentication = new TokenAuthentication(token);
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return tokenAuthentication;
        }
        UserEntity user = userOptional.get();
        Set<GrantedAuthority> roles = user.getRoles().
                stream().
                map(RoleEntity::getRole).
                map(Role::name).
                map(roleName -> (GrantedAuthority)() -> roleName).
                collect(Collectors.toSet());

        tokenAuthentication.setAuthorities(roles);
        tokenAuthentication.setUserId(userId);
        tokenAuthentication.setAuthenticated(true);

        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(TokenAuthentication.class);
    }
}
