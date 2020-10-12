package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Task entity
 */
@Data
@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "done")
    private boolean done;

    @Column(name = "priority")
    private byte priority;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskListEntity taskList;
}
