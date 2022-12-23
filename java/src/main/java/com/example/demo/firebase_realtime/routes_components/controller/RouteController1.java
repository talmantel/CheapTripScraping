package com.example.demo.firebase_realtime.routes_components.controller;

import com.example.demo.classes.Route;
import com.example.demo.firebase_realtime.routes_components.service.RouteService1;
import org.springframework.web.bind.annotation.*;

@RestController
public class RouteController1 {
    private final RouteService1 service;

    public RouteController1(RouteService1 service) {
        this.service = service;
    }

    @PostMapping("/routes")
    public void postLocation(@RequestBody Route route) throws Exception{
        try {
            service.postRoute(route);
        }
        catch (Exception e) {
            throw new Exception("problem with postRoute()");
        }
    }

    @GetMapping("/routes")
    public void getRoute(@RequestParam int id) throws Exception{
        try{
            service.getRoute(id);
        }
        catch (Exception e) {
            throw new Exception("An error occurred with getting Route");
        }
    }

    @PostMapping("/routes/all")
    public void postAllRoutes() throws Exception{
        try{
            service.postAllRoutes();
        }
        catch (Exception e) {
            throw new Exception("An error occurred with postAllRoutes");
        }
    }
}
