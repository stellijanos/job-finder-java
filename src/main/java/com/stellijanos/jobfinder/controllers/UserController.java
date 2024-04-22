package com.stellijanos.jobfinder.controllers;

import com.stellijanos.jobfinder.models.User;
import com.stellijanos.jobfinder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
