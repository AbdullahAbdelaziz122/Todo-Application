package com.todo.TodoApplication.controller;

import com.todo.TodoApplication.exceptions.UserNotFoundException;
import com.todo.TodoApplication.models.ApplicationUser;
import com.todo.TodoApplication.repository.UserRepository;
import com.todo.TodoApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    // Create
    @PostMapping("/users/register")
    public ResponseEntity<String> registerUser(@RequestBody ApplicationUser applicationUser) {
            return userService.registerUser(applicationUser);
    }


    // read
    @GetMapping("/users/search")
    public List<ApplicationUser> getAllUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username
    ) {
       return userService.getAllUsers(id, username);
    }

    // update
    @PutMapping("/users/update")
    public ResponseEntity<String> updateUserDetails(
        @RequestParam(required = true) Long userId,
        @RequestBody ApplicationUser newUser){
        try {
            ApplicationUser updatedUser = userService.updateUser(userId, newUser);
            return ResponseEntity.ok("User details updated successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating user details: " + e.getMessage());
        }

    }

    // delete

    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long id){
        boolean isDeleted = userService.deleteUser(id);
        if(isDeleted){
            return ResponseEntity.ok("User Has Been Deleted Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found");
        }
    }

}

