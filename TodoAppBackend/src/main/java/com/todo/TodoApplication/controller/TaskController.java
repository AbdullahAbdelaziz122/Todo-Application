package com.todo.TodoApplication.controller;

import com.todo.TodoApplication.models.ApplicationUser;
import com.todo.TodoApplication.models.Task;
import com.todo.TodoApplication.repository.TaskRepository;
import com.todo.TodoApplication.repository.UserRepository;
import com.todo.TodoApplication.service.TaskService;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskService taskService;

    // create
    @PostMapping("/tasks/add/{user-id}")
    public ResponseEntity<String> addTask(@RequestBody Task task, @PathVariable("user-id") Long userId) {
       boolean isAdded = taskService.addTask(task, userId);
       try {
           if(isAdded) {

               return ResponseEntity.status(HttpStatus.CREATED).body("Task Has Been Successfully Created");
               } else {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("User ID: " + userId + " Is Not Found");
               }

       }catch (Exception ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("An error occurred while updating user details: " + ex.getMessage());
       }
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
        return taskService.getTasks(userId, title, status);
    }


    //delete
    @DeleteMapping("/tasks/delete")
    public ResponseEntity<String> deleteTaskById(@RequestParam Long id) {
        boolean isDeleted = taskService.deleteTaskById(id);
        if(isDeleted){
            return ResponseEntity.ok("Task Has Been Deleted Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task with ID " + id + " not found");
        }
    }

    // update
    @PutMapping("/tasks/update")
    public ResponseEntity<String> updateTaskById(@RequestBody Task newTask, @RequestParam Long id) {

        try{
            boolean isUpdated = taskService.updateTaskById(newTask, id);
            if(isUpdated){
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Task Has Been Updated Successfully");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("There Is No Task With ID: "+ id);
            }
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An Error Occurred Updating Task Details: "+ ex.getMessage());
        }
    }

}
