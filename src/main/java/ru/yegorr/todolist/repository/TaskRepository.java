package ru.yegorr.todolist.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.TaskEntity;

/**
 * Repository for tasks
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

}
