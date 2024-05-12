package com.stellijanos.jobfinder.controllers;

import com.stellijanos.jobfinder.models.User;
import com.stellijanos.jobfinder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAll() {
        return new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
       return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
