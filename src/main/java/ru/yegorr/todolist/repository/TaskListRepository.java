package ru.yegorr.todolist.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.TaskListEntity;

import java.util.UUID;

/**
 * Репозиторий для списков
 */
@Repository
public interface TaskListRepository extends JpaRepository<TaskListEntity, UUID>, JpaSpecificationExecutor<TaskListEntity> {

}
