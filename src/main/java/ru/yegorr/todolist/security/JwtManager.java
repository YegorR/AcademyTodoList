package ru.yegorr.todolist.security;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yegorr.todolist.exception.ValidationFailsException;

import java.time.*;
import java.util.*;

/**
 * Конпонент для извлечения и занесения информации в JWT
 */
@Component
public class JwtManager {

    private static final String USER_ID = "userId";

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    /**
     * Конструктор
     *
     * @param secret секрет для подписи, берётся из системной переменной JWT_SECRET
     */
    public JwtManager(@Value(value = "${JWT_SECRET}") String secret) {
        algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).build();
    }

    /**
     * Создаёт JWT
     *
     * @param userId id пользователя
     * @param expiredTime время, когда истекает токен
     * @return токен
     */
    public String generateToken(UUID userId, LocalDateTime expiredTime) {
        return JWT.create().
                withClaim(USER_ID, userId.toString()).
                withExpiresAt(Date.from(expiredTime.atZone(ZoneId.systemDefault()).toInstant())).
                sign(algorithm);
    }

    /**
     * Извлекает информацию из JWT
     *
     * @param token токен
     * @return JwtInfo
     * @throws ValidationFailsException если токен "плохой"
     */
    public JwtInfo readToken(String token) throws ValidationFailsException {
        try {
            if (token == null) {
                throw new ValidationFailsException("Token is null");
            }
            DecodedJWT decodedJWT = verifier.verify(token);

            UUID userId = UUID.fromString(decodedJWT.getClaim(USER_ID).asString());
            LocalDateTime expTime = LocalDateTime.ofInstant(decodedJWT.getExpiresAt().toInstant(), ZoneId.systemDefault());
            return new JwtInfo(userId, expTime);
        } catch (JWTVerificationException | IllegalArgumentException ex) {
            throw new ValidationFailsException("Wrong JWT");
        }
    }
}
