package com.todo.TodoApplication.controller;

import com.todo.TodoApplication.models.ApplicationUser;
import com.todo.TodoApplication.models.Task;
import com.todo.TodoApplication.repository.TaskRepository;
import com.todo.TodoApplication.repository.UserRepository;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // create
    @PostMapping("/tasks/add/{user-id}")
    public Task addTask(@RequestBody Task task, @PathVariable("user-id") Long userId) {
        // Fetch the user from the database
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Associate the user with the task
        task.setUser(user);

        // Save the task and return it
        return taskRepository.save(task);
    }


    // read
    @GetMapping("/tasks")
    public Task getTasksById(@RequestParam Long id){
        return taskRepository.findById(id).orElse(new Task());
    }

    @GetMapping("/tasks/{user-id}")
    public List<Task> getAllTasks(
            @PathVariable("user-id") Long id
    ){
        return taskRepository.findByUser_Id(id);
    }

    @GetMapping("/tasks/search/{user-id}")
    public List<Task> getTasks(
            @PathVariable("user-id") Long userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status) {


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


    //delete
    @DeleteMapping("/tasks/delete")
    public void deleteTaskById(@RequestParam Long id) {
        taskRepository.deleteById(id);
    }

    // update
    @PutMapping("/tasks/update")
    public Task updateTaskById(@RequestBody Task newTask, @RequestParam Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        // Update the fields
        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setStatus(newTask.getStatus());

        // Save the updated task
        return taskRepository.save(task);
    }


}
