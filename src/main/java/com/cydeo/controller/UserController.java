package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ResponseWrapper> getUsers() {

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Users successfully retrieved")
                .data(userService.listAllUsers()).build());
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> getUserByUsername(@PathVariable("username") String username) {

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User successfully retrieved")
                .data(userService.findByUserName(username)).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user) {

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("User successfully created")
                .build());
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) {

        userService.update(user);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User successfully updated")
                .build());
    }

    @DeleteMapping("{username}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) {

        userService.delete(username);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User successfully deleted")
                .build());
    }

}