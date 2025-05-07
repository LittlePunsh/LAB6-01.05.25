package org.example.railwayapp.controller;

import jakarta.servlet.http.HttpSession;
import org.example.railwayapp.model.users.User;
import org.example.railwayapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model,
                            HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/default";
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Неверные имя пользователя или пароль.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Вы успешно вышли из системы.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        // 1. Найти пользователя по имени
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 2. ПРОВЕРЯЕМ ПАРОЛЬ ПРЯМЫМ СРАВНЕНИЕМ СТРОК
            if (password.equals(user.getPassword())) {
                // 3. Если пароль верный, сохранить пользователя в сессии
                session.setAttribute("loggedInUser", user);

                // 4. Перенаправить на /default для определения страницы по роли
                return "redirect:/default";
            }
        }

        // Если пользователь не найден или пароль неверный
        redirectAttributes.addFlashAttribute("errorMessage", "Неверные имя пользователя или пароль.");
        return "redirect:/login?error=true";
    }


    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/default";
        }
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(@RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam String mail,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(username, password, mail);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно! Теперь вы можете войти.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка регистрации: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}