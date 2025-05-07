package org.example.railwayapp.controller;

import jakarta.servlet.http.HttpSession;
import org.example.railwayapp.dto.AdminTripDto;
import org.example.railwayapp.model.users.User;
import org.example.railwayapp.service.RailwayDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RailwayDataService railwayDataService;

    @GetMapping
    public String adminPage(Model model, HttpSession session) {
        // Проверяем аутентификацию и роль
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
            // Если не админ или не залогинен, перенаправляем на логин или другую страницу
            return "redirect:/login?error=accessDenied";
        }

        try {
            List<AdminTripDto> tripsAndTickets = railwayDataService.getAllTripsWithTicketsForAdmin();
            model.addAttribute("tripsData", tripsAndTickets);
            model.addAttribute("loggedInUser", loggedInUser);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка загрузки данных: " + e.getMessage());
        }
        return "admin";
    }
}