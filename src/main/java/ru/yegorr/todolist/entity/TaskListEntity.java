package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.*;
import java.util.*;

/**
 * Сущность списка заданий
 */
@Data
@Entity
@Table(name = "list")
public class TaskListEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private short color;

    @Column(name = "priority")
    @Enumerated(value = EnumType.ORDINAL)
    private Priority priority;

    @OneToMany(mappedBy = "taskList")
    private List<TaskEntity> tasks;
}
