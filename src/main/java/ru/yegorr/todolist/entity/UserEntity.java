package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

/**
 * Сущность пользователя
 */
@Entity
@Data
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;
}
