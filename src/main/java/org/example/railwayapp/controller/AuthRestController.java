package org.example.railwayapp.controller;

import org.example.railwayapp.dto.LoginRequest;
import org.example.railwayapp.dto.LoginResponse;
import org.example.railwayapp.dto.RegisterRequest;
import org.example.railwayapp.dto.RegisterResponse;
import org.example.railwayapp.model.users.User;
import org.example.railwayapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth") 
public class AuthRestController {

    @Autowired
    private UserService userService;

    // Эндпоинт для логина через API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> apiLogin(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();
        Optional<User> userOptional = userService.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (loginRequest.getPassword().equals(user.getPassword())) {
                response.setSuccess(true);
                response.setMessage("Login successful");
                response.setRole(user.getRole()); 
                return ResponseEntity.ok(response); 
            }
        }
        response.setSuccess(false);
        response.setMessage("Invalid username or password");
        response.setRole(null);
        return ResponseEntity.status(HttpStatus.OK).body(response); 
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> apiRegister(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = new RegisterResponse();
        try {
            if (registerRequest.getUsername() == null || registerRequest.getUsername().isEmpty() ||
                    registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty() ||
                    registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Username, password, and email are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            userService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
            response.setSuccess(true);
            response.setMessage("Registration successful");
            return ResponseEntity.ok(response); 
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
