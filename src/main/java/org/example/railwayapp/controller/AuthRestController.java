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

// @RestController объединяет @Controller и @ResponseBody
// @ResponseBody означает, что возвращаемые значения методов будут сериализованы в тело ответа (обычно JSON)
@RestController
@RequestMapping("/api/auth") // Базовый путь для всех API аутентификации
public class AuthRestController {

    @Autowired
    private UserService userService;

    // Эндпоинт для логина через API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> apiLogin(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();

        // 1. Найти пользователя по имени
        Optional<User> userOptional = userService.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 2. ПРОВЕРЯЕМ ПАРОЛЬ ПРЯМЫМ СРАВНЕНИЕМ СТРОК
            // ВНИМАНИЕ: В реальном приложении здесь должна быть безопасная проверка хэша пароля!
            if (loginRequest.getPassword().equals(user.getPassword())) {
                // 3. Если пароль верный, возвращаем успешный ответ с ролью
                response.setSuccess(true);
                response.setMessage("Login successful");
                response.setRole(user.getRole()); // Отправляем роль пользователя
                return ResponseEntity.ok(response); // Статус 200 OK
            }
        }

        // Если пользователь не найден или пароль неверный
        response.setSuccess(false);
        response.setMessage("Invalid username or password");
        response.setRole(null);
        // Для API логина часто возвращают 401 Unauthorized или 400 Bad Request
        // ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response) - более правильный статус
        return ResponseEntity.status(HttpStatus.OK).body(response); // Возвращаем 200 OK, но с success=false для примера
    }

    // Эндпоинт для регистрации через API
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> apiRegister(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = new RegisterResponse();
        try {
            // Проверяем, что данные для регистрации есть
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
            return ResponseEntity.ok(response); // Статус 200 OK
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Registration failed: " + e.getMessage());
            // Если регистрация не удалась (например, пользователь существует), возвращаем 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}