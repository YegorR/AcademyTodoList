package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.*;
import java.util.UUID;

/**
 * Сущность задания
 */
@Data
@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "done")
    private boolean done;

    @Column(name = "priority")
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskListEntity taskList;
}
