package ru.yegorr.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.TaskListEntity;

/**
 * Repository for task lists
 */
@Repository
public interface TaskListRepository extends JpaRepository<TaskListEntity, Long> {

}
