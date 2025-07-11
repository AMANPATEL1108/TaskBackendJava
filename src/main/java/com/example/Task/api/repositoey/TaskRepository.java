package com.example.Task.api.repositoey;

import com.example.Task.api.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskMenuId(Long taskMenuId);
    void deleteAllByTaskMenuId(Long taskMenuId); // Optional method if you want direct delete

}

