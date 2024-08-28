package com.todo.TodoApplication.service;

import com.todo.TodoApplication.exceptions.UserNotFoundException;
import com.todo.TodoApplication.models.ApplicationUser;
import com.todo.TodoApplication.repository.TaskRepository;
import com.todo.TodoApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ResponseEntity<String> registerUser(ApplicationUser applicationUser) {

       try {
           ApplicationUser savedUser = userRepository.save(applicationUser);

           if(savedUser.getId() > 0){
               return ResponseEntity.status(HttpStatus.CREATED)
                       .body("User Has Been Successfully Registered");
           }else {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                       .body("User Registration Failed");
           }
       }catch (Exception ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("An exception happened: "+ ex.getMessage());
       }


    }


    public List<ApplicationUser> getAllUsers(
            Long id,
            String username
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


    public ApplicationUser updateUser(Long userId, ApplicationUser newUser) {
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for the Id: "+ userId));

        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }else{return false;}
    }


}
