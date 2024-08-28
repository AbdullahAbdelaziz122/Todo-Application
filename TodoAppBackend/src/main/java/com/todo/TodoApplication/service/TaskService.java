package com.todo.TodoApplication.service;

import com.todo.TodoApplication.models.ApplicationUser;
import com.todo.TodoApplication.models.Task;
import com.todo.TodoApplication.repository.TaskRepository;
import com.todo.TodoApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

//    public Task addTask(Task task, Long userId) {
//
//        ApplicationUser user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        task.setUser(user);
//        return taskRepository.save(task);
//    }

        public boolean addTask(Task task, Long userId) {
        if(userRepository.existsById(userId)){
            ApplicationUser user = userRepository.findById(userId).orElse(new ApplicationUser());
            task.setUser(user);
            taskRepository.save(task);
            return true;
        }else {
            return false;
        }
    }

    public List<Task> getTasks(
            Long userId,
            String title,
            String status) {


        if (title != null && status != null) {
            return taskRepository.findByUser_IdAndTitleContainingAndStatus(userId, title, status);
        } else if (title != null) {
            return taskRepository.findByUser_IdAndTitleContaining(userId, title);
        } else if (status != null) {
            return taskRepository.findByUser_IdAndStatus(userId, status);
        } else {
            return taskRepository.findByUser_Id(userId);
        }

    }

    public boolean deleteTaskById(Long id) {
        if(taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateTaskById(Task newTask, Long id) {
//        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
//
//        // Update the fields
//        task.setTitle(newTask.getTitle());
//        task.setDescription(newTask.getDescription());
//        task.setStatus(newTask.getStatus());
//
//        // Save the updated task
//        return taskRepository.save(task);

        if(taskRepository.existsById(id)){
            Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

            // Update the fields
            task.setTitle(newTask.getTitle());
            task.setDescription(newTask.getDescription());
            task.setStatus(newTask.getStatus());

            taskRepository.save(task);
            return true;
        }else {
            return false;
        }
    }
}
