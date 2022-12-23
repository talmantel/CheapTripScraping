package com.example.demo.firebase_realtime.locations_components.controller;

import com.example.demo.classes.Location;
import com.example.demo.firebase_realtime.locations_components.service.LocationService1;
import org.springframework.web.bind.annotation.*;

@RestController
public class LocationController1 {
    private final LocationService1 service;

    public LocationController1(LocationService1 service) {
        this.service = service;
    }

    @PostMapping("/locations")
    public void postLocation(@RequestBody Location location) throws Exception{
        try {
            service.postLocation(location);
        }
        catch (Exception e) {
            throw new Exception("problem with postLocation()");
        }
    }

    @GetMapping("/locations")
    public void getLocation(@RequestParam String name) throws Exception{
        try{
            service.getLocation(name);
        }
        catch (Exception e) {
            throw new Exception("An error occurred with getting Location");
        }
    }

    @PostMapping("/locations/all")
    public void postAllLocations() throws Exception{
        try{
            service.postAllLocations();
        }
        catch (Exception e) {
            throw new Exception("An error occurred with postAllLocations");
        }
    }
}
