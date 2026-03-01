package com.diary.controller;
import com.diary.dto.LoginRequest;
import com.diary.dto.UserResponseDto;
import com.diary.entity.User;
import com.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        boolean isValid = userService.login(request.getEmail(), request.getPassword());
        if(isValid){
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> userRegister(@RequestBody User user) {
        User savedUser = userService.userRegister(user);
        UserResponseDto response = new UserResponseDto();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setUsername(savedUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        User updated = userService.updateUser(id, updateUser);
        UserResponseDto response = new UserResponseDto();
        response.setId(updated.getId());
        response.setEmail(updated.getEmail());
        response.setUsername(updated.getUsername());
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        userService.deleteUser(id, email);
    }
}
