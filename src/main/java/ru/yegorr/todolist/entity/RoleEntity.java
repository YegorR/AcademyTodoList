package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

/**
 * Сущность роли
 */
@Entity
@Data
@Table(name = "role")
public class RoleEntity {
    @Id
    @Column(name = "id")
    private UUID uuid;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private Role role;
}
