package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.*;
import java.util.UUID;

/**
 * Task entity
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
    private byte priority;
    // TODO(Шайдуко): приоритет лучше сделать не числовым, а перечислением
    // соответственно это повлечет правку в DTO

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskListEntity taskList;
}
