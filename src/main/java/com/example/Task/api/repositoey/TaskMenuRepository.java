package com.example.Task.api.repositoey;

import com.example.Task.api.model.TaskMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskMenuRepository extends JpaRepository   <TaskMenu, Long> {
    Optional<TaskMenu> findById(Long id); // This is already provided by JpaRepository
}
