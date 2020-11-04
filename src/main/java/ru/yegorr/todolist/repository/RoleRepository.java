package ru.yegorr.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.*;

import java.util.*;

/**
 * Репозиторий для ролей
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    /**
     * Ищет roleEntity по Role
     *
     * @param role роль
     * @return RoleEntity
     */
    Optional<RoleEntity> getByRole(Role role);
}
