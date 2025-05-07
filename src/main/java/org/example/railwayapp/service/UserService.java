package org.example.railwayapp.service;

import org.example.railwayapp.model.users.User;
import org.example.railwayapp.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    // Метод для поиска пользователя по имени
    @Transactional("usersTransactionManager")
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional("usersTransactionManager")
    public void registerUser(String username, String rawPassword, String email) throws Exception {
        if (userRepository.existsByUsername(username)) {
            throw new Exception("Username already exists: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email already exists: " + email);
        }

        String storedPassword = rawPassword;

        // Создаем нового пользователя (роль по умолчанию - "user")
        User newUser = new User(username, storedPassword, email, "user"); // Передаем открытый пароль

        // Сохраняем пользователя в БД
        userRepository.save(newUser);
    }
}