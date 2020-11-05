package ru.yegorr.todolist.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.*;
import ru.yegorr.todolist.dto.response.AuthenticateResponse;
import ru.yegorr.todolist.entity.UserEntity;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.repository.UserRepository;
import ru.yegorr.todolist.security.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Реализация AuthService
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private JwtManager jwtManager;

    private Encoder encoder;

    private long accessTokenSecondPeriod, refreshTokenSecondPeriod;

    /**
     * Конструктор
     *
     * @param userRepository userRepository
     */
    @Autowired
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticateResponse doAuthentication(AuthenticateRequest authenticateRequest) throws ApplicationException {
        UserEntity user = userRepository.findByEmailAndPassword(authenticateRequest.getEmail(), encoder.encryptPassword(authenticateRequest.getPassword())).
                orElseThrow(() -> new NotAuthorizeException("User or password is wrong"));
        return generateTokens(user);
    }

    @Override
    public AuthenticateResponse refresh(RefreshRequest refreshRequest) throws ApplicationException {
        final String REFRESH_IS_WRONG = "Refresh token is wrong or expired";

        try {
            UUID userRefreshToken = UUID.fromString(refreshRequest.getRefreshToken());
            UserEntity user = userRepository.findByRefreshToken(userRefreshToken).orElseThrow(() -> new NotAuthorizeException(REFRESH_IS_WRONG));
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expTime = user.getRefreshExp();
            if (now.isAfter(expTime)) {
                user.setRefreshExp(null);
                user.setRefreshToken(null);
                throw new NotAuthorizeException(REFRESH_IS_WRONG);
            }
            return generateTokens(user);
        } catch (IllegalArgumentException ex) {
            throw new NotAuthorizeException(REFRESH_IS_WRONG);
        }
    }

    private AuthenticateResponse generateTokens(UserEntity user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime accessExpTime = now.plusSeconds(accessTokenSecondPeriod);
        LocalDateTime refreshExpTime = now.plusSeconds(refreshTokenSecondPeriod);

        String accessToken = jwtManager.generateToken(user.getId(), accessExpTime);
        UUID refreshToken = UUID.randomUUID();

        user.setRefreshToken(refreshToken);
        user.setRefreshExp(refreshExpTime);

        AuthenticateResponse authenticateResponse = new AuthenticateResponse();
        authenticateResponse.setAccessToken(accessToken);
        authenticateResponse.setRefreshToken(refreshToken.toString());
        return authenticateResponse;
    }

    @Override
    public void logout() throws ApplicationException {
        UUID userId = UserSecurityInformation.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotAuthorizeException("User is not found"));
        user.setRefreshToken(null);
        user.setRefreshExp(null);
    }

    /**
     * @param jwtManager jwtManager
     */
    @Autowired
    public void setJwtManager(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    /**
     * @param accessTokenSecondPeriod время действия access token в секундах
     */
    @Value("${access.token.period}")
    public void setAccessTokenSecondPeriod(long accessTokenSecondPeriod) {
        this.accessTokenSecondPeriod = accessTokenSecondPeriod;
    }

    /**
     * @param refreshTokenSecondPeriod время действия refresh token в секундах
     */
    @Value("${refresh.token.period}")
    public void setRefreshTokenSecondPeriod(long refreshTokenSecondPeriod) {
        this.refreshTokenSecondPeriod = refreshTokenSecondPeriod;
    }

    /**
     * @param encoder encoder
     */
    @Autowired
    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }
}
