package com.example.demo.firestore.travel_data_components.controller;


import com.example.demo.classes.TravelData;
import com.example.demo.firestore.travel_data_components.service.TravelDataService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class TravelDataController {

    public TravelDataService service;

    public TravelDataController (TravelDataService service) {
        this.service = service;
    }

    @PostMapping("/createData")
    public String createData(@RequestBody TravelData data) throws InterruptedException, ExecutionException {
        return service.createData(data);
    }

    @GetMapping("/getData")
    public TravelData getData(@RequestParam int id) throws InterruptedException, ExecutionException {
        return service.getData(id);
    }

    @PutMapping("/updateData")
    public String updateData(@RequestParam TravelData data) throws InterruptedException, ExecutionException {
        return service.updateData(data);
    }

    @DeleteMapping("/deleteData")
    public String deleteData(@RequestParam int id) throws InterruptedException, ExecutionException {
        return service.deleteData(id);
    }

    @PostMapping("/createAllData")
    public void createAllData() throws IOException, ExecutionException, InterruptedException {
        service.createAllData();
    }

}
