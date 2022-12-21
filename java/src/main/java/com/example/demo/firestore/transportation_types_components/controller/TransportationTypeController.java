package com.example.demo.firestore.transportation_types_components.controller;


import com.example.demo.classes.TransportationType;
import com.example.demo.firestore.transportation_types_components.service.TransportationTypeService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class TransportationTypeController {

    public TransportationTypeService service;

    public TransportationTypeController (TransportationTypeService service) {
        this.service = service;
    }

    @PostMapping("/createTransportationType")
    public String createTransportationType(@RequestBody TransportationType type) throws InterruptedException, ExecutionException {
        return service.createTransportationType(type);
    }

    @GetMapping("/getTransportationType")
    public TransportationType getLocation(@RequestParam String name) throws InterruptedException, ExecutionException {
        return service.getTransportationType(name);
    }

    @PutMapping("/updateTransportationType")
    public String updateCurrency(@RequestParam TransportationType type) throws InterruptedException, ExecutionException {
        return service.updateTransportationType(type);
    }

    @DeleteMapping("/deleteTransportationType")
    public String deleteTransportationType(@RequestParam String name) throws InterruptedException, ExecutionException {
        return service.deleteTransportationType(name);
    }

//    @GetMapping("/test")
//    public ResponseEntity<String> testGetEndpoint() {
//        return ResponseEntity.ok("The test endpoint is working");
//    }


    @PostMapping("/createAllTransportationTypes")
    public void createAllTransportationTypes () throws IOException, ExecutionException, InterruptedException {
        service.createAllTransportationTypes();
    }
}
