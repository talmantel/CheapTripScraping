package com.example.demo.firestore.routes_components.controller;

import com.example.demo.classes.Route;
import com.example.demo.firestore.routes_components.service.RoutesService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class RoutesController {

    public RoutesService service;

    public RoutesController (RoutesService service) {
        this.service = service;
    }

    @PostMapping("/createRoute")
    public String createRoute(@RequestBody Route route) throws InterruptedException, ExecutionException {
        return service.createRoute(route);
    }

    @GetMapping("/getRoute")
    public Route getRoute(@RequestParam int id) throws InterruptedException, ExecutionException {
        return service.getRoute(id);
    }

    @PutMapping("/updateRoute")
    public String updateRoute(@RequestParam Route route) throws InterruptedException, ExecutionException {
        return service.updateRoute(route);
    }

    @DeleteMapping("/deleteRoute")
    public String deleteRoute(@RequestParam int id) throws InterruptedException, ExecutionException {
        return service.deleteRoute(id);
    }

    @PostMapping("/createAllRoutes")
    public void createAllRoutes () throws Exception {
        service.createAllRoutes();
    }
}