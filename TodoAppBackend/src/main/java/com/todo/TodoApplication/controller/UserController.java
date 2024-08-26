package com.todo.TodoApplication.controller;

import com.todo.TodoApplication.models.ApplicationUser;
import com.todo.TodoApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;


    // Create
    @PostMapping("/users/register")
    public ApplicationUser registerUser(@RequestBody ApplicationUser applicationUser) {

            userRepository.save(applicationUser);

            return applicationUser;


    }

    // read
    @GetMapping("/users/search")
    public List<ApplicationUser> getAllUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username
    ) {
        // Check if both id and username are provided
        if (id != null && username != null) {
            // Find by username and id
            return userRepository.findByUsernameAndId(username, id)
                    .map(Collections::singletonList) // Wrap the single result in a list
                    .orElse(Collections.emptyList()); // Return an empty list if not found
        } else if (username != null) {
            return userRepository.findByUsername(username)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else if (id != null) {
            return userRepository.findById(id)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }
        else {
            // Return all users if no specific query parameters are provided
            return userRepository.findAll();
        }
    }

    // update
    @PutMapping("/users/update")
    public ApplicationUser updateUserDetails(
        @RequestParam(required = true) Long userId,
        @RequestBody ApplicationUser newUser){

        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        return userRepository.save(user);
    }

    // delete

    @DeleteMapping("/users/delete")
    public void deleteUser(@RequestParam
                           Long id){
        userRepository.deleteById(id);
    }

}
