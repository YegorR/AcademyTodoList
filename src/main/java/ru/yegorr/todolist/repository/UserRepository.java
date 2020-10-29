package ru.yegorr.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yegorr.todolist.entity.UserEntity;

import java.util.UUID;

/**
 * Репозиторий для пользователей
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
