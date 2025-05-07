package org.example.railwayapp.controller;

import jakarta.servlet.http.HttpSession;
import org.example.railwayapp.model.users.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/default")
    public String defaultAfterLogin(HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Если пользователь не найден в сессии (не залогинен), перенаправляем на страницу входа
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Определяем роль и перенаправляем
        String role = loggedInUser.getRole();

        if ("admin".equals(role)) {
            return "redirect:/admin";
        } else if ("user".equals(role)) {
            return "redirect:/user";
        } else {
            // Если роль не определена или неизвестна
            session.invalidate();
            return "redirect:/login?error=role";
        }
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        // Проверяем, залогинен ли пользователь. Если да, на /default, иначе на /login
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/default";
        }
        return "redirect:/login";
    }
}