package ru.yegorr.todolist.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.TaskEntity;

import java.util.List;

/**
 * Repository for tasks
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    /**
     * Finds all tasks by task list id
     *
     * @param id task list id
     * @param pageable Pageable
     * @return task entities
     */
    List<TaskEntity> findAllByTaskList_Id(long id, Pageable pageable);
}
