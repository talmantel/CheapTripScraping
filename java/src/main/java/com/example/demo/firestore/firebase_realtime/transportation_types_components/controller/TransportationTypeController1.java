package com.example.demo.firestore.firebase_realtime.transportation_types_components.controller;

import com.example.demo.classes.TransportationType;
import com.example.demo.firestore.firebase_realtime.transportation_types_components.service.TransportationTypeService1;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransportationTypeController1 {
    private final TransportationTypeService1 service;

    public TransportationTypeController1(TransportationTypeService1 service) {
        this.service = service;
    }

    @PostMapping("/types")
    public void postTransportationType(@RequestBody TransportationType type) throws Exception {
        try {
            service.postTransportationType(type);
        } catch (Exception e) {
            throw new Exception("problem with postTransportationType()");
        }
    }

    @GetMapping("/types")
    public void getTransportationType(@RequestParam int id) throws Exception {
        try {
            service.getTransportationType(id);
        } catch (Exception e) {
            throw new Exception("An error occurred with getting Transportation type");
        }
    }

    @PostMapping("/types/all")
    public void postAllTransportationTypes() throws Exception {
        try {
            service.postAllTransportationTypes();
        } catch (Exception e) {
            throw new Exception("An error occurred with postAllTransportationTypes()");
        }
    }
}
