package ru.yegorr.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.UserEntity;

import java.util.*;

/**
 * Репозиторий для пользователей
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Проверяет, уникальный ли email
     *
     * @param email электронный адрес
     * @return true если не уникальный, иначе false
     */
    boolean existsByEmail(String email);

    /**
     * Проверяет, уникальный ли никнейм
     *
     * @param nickname никнейм
     * @return true если не уникальный, иначе false
     */
    boolean existsByNickname(String nickname);

    /**
     * Находит пользователя по email
     *
     * @param email электронный адрес
     * @return пользователь
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Находит пользователя по refresh token
     *
     * @param refreshToken refresh token
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> findByRefreshToken(UUID refreshToken);
}
