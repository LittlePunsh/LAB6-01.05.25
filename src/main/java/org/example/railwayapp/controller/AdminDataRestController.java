package org.example.railwayapp.controller;

import org.example.railwayapp.dto.AdminTripDto;
import org.example.railwayapp.service.RailwayDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/railwaydata") // Базовый путь для API админских данных
public class AdminDataRestController {

    @Autowired
    private RailwayDataService railwayDataService;

    // Эндпоинт для получения всех рейсов с билетами для админа через API
    @GetMapping
    public ResponseEntity<List<AdminTripDto>> getAllTripsWithTickets() {
        // ВНИМАНИЕ: В реальном API здесь ДОЛЖНА БЫТЬ строгая проверка на роль "admin"!
        // Текущий код просто возвращает данные без проверки.
        List<AdminTripDto> tripsAndTickets = railwayDataService.getAllTripsWithTicketsForAdmin();
        return ResponseEntity.ok(tripsAndTickets); // Возвращаем список DTO с статусом 200 OK
    }
}