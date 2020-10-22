package ru.yegorr.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Task list entity
 */
@Data
@Entity
@Table(name = "list")
public class TaskListEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "creation_date")
    private LocalDate creationDate;
    // TODO(Шайдуко): я бы исполозовал Date, чтоб было время. т.к. просто дата слишком больой промежуток

    @Column(name = "update_date")
    private LocalDate updateDate;
    // TODO(Шайдуко): тоже что и выше

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "taskList")
    private List<TaskEntity> tasks;
}
