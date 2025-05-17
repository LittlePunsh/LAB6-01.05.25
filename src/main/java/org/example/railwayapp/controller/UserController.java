package org.example.railwayapp.controller;

import jakarta.servlet.http.HttpSession;
import org.example.railwayapp.model.railway.Trip;
import org.example.railwayapp.model.users.User;
import org.example.railwayapp.service.RailwayDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RailwayDataService railwayDataService;

    @GetMapping
    public String userPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"user".equals(loggedInUser.getRole())) {
            return "redirect:/login?error=accessDenied";
        }

        try {
            List<Trip> trips = railwayDataService.getAllTripsOrdered();
            model.addAttribute("trips", trips);
            model.addAttribute("loggedInUser", loggedInUser);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка загрузки данных: " + e.getMessage());
        }
        return "user";
    }
}
