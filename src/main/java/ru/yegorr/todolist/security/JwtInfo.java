package ru.yegorr.todolist.security;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Информация для занесения в JWT
 */
@Value
public class JwtInfo {
    UUID userID;

    LocalDateTime expiredTime;
}
