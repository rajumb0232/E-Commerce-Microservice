package com.example.user.controller;

import com.example.user.dto.request.RegistrationRequest;
import com.example.user.dto.request.UserRequest;
import com.example.user.dto.response.UserResponse;
import com.example.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegistrationRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Yes I'm Up - User Service");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id) {
        UserResponse response = userService.findUserById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
