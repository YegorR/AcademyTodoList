package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Task list entity
 */
@Data
@Entity
@Table(name = "list")
public class TaskListEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "taskList")
    private List<TaskEntity> tasks;
}
