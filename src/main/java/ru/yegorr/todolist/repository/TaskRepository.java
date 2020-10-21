package ru.yegorr.todolist.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.TaskEntity;

import java.util.UUID;

/**
 * Repository for tasks
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID>, JpaSpecificationExecutor<TaskEntity> {
    long countAllByTaskList_Id(UUID taskListId);
}
