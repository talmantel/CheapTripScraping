package com.example.demo.firestore.firebase_realtime.travel_data_components.controller;

import com.example.demo.classes.TravelData;
import com.example.demo.firestore.firebase_realtime.travel_data_components.service.TravelDataService1;
import org.springframework.web.bind.annotation.*;

@RestController
public class TravelDataController1 {
    private final TravelDataService1 service;

    public TravelDataController1(TravelDataService1 service) {
        this.service = service;
    }

    @PostMapping("/travel_data")
    public void postTravelData(@RequestBody TravelData data) throws Exception {
        try {
            service.postTravelData(data);
        } catch (Exception e) {
            throw new Exception("problem with postTravelData()");
        }
    }

    @GetMapping("/travel_data")
    public void getTravelData(@RequestParam int id) throws Exception {
        try {
            service.getTravelData(id);
        } catch (Exception e) {
            throw new Exception("An error occurred with getting Travel Data");
        }
    }

    @PostMapping("/travel_data/all")
    public void postAllTravelData() throws Exception {
        try {
            service.postAllTravelData();
        } catch (Exception e) {
            throw new Exception("An error occurred with postAllTravelData()");
        }
    }
}
