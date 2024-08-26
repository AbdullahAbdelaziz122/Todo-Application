package com.todo.TodoApplication.repository;

import com.todo.TodoApplication.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTitleContainingAndStatus(String title, String status);

    List<Task> findByTitleContaining(String title);

    List<Task> findByStatus(String status);

    List<Task> findByUser_Id(Long userId);

    List<Task> findByUser_IdAndTitleContaining(Long userId, String title);

    List<Task> findByUser_IdAndStatus(Long userId, String status);

    List<Task> findByUser_IdAndTitleContainingAndStatus(Long userId, String title, String status);
}

