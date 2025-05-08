package org.example.railwayapp.controller;

import org.example.railwayapp.model.railway.Trip;
import org.example.railwayapp.service.RailwayDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trips") // Базовый путь для API рейсов
public class TripRestController {

    @Autowired
    private RailwayDataService railwayDataService;

    // Эндпоинт для получения всех рейсов через API
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = railwayDataService.getAllTripsOrdered();
        return ResponseEntity.ok(trips);
    }
}
