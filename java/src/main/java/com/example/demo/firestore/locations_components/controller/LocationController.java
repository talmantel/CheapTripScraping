package com.example.demo.firestore.locations_components.controller;

import com.example.demo.classes.Location;
import com.example.demo.firestore.locations_components.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class LocationController {

    public LocationService locationService;

    public LocationController (LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/createLocation")
    public String createLocation(@RequestBody Location location) throws InterruptedException, ExecutionException {
        return locationService.createLocation(location);
    }

    @GetMapping("/getLocation")
    public Location getLocation(@RequestParam String name) throws InterruptedException, ExecutionException {
        return locationService.getLocation(name);
    }

    @PutMapping("/updateLocation")
    public String updateLocation(@RequestParam Location location) throws InterruptedException, ExecutionException {
        return locationService.updateLocation(location);
    }

    @DeleteMapping("/deleteLocation")
    public String deleteLocation(@RequestParam String name) throws InterruptedException, ExecutionException {
        return locationService.deleteLocation(name);
    }

//    @GetMapping("/test")
//    public ResponseEntity<String> testGetEndpoint() {
//        return ResponseEntity.ok("The test endpoint is working");
//    }


    @PostMapping("/createAllLocations")
    public void createAllLocations () throws IOException, ExecutionException, InterruptedException {
        locationService.createAllLocations();
    }
}
