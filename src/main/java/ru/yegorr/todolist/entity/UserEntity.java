package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

/**
 * Сущность пользователя
 */
@Entity
@Data
@Table(name = "user_account")
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

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<RoleEntity> roles;
}
