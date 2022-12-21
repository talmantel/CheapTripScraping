package com.example.demo.firestore.country_components.controller;

import com.example.demo.classes.Country;
import com.example.demo.firestore.country_components.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class CountryController {

    public CountryService countryService;

    public CountryController (CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/createCountry")
    public String createCountry(@RequestBody Country country) throws InterruptedException, ExecutionException {
        return countryService.createCountry(country);
    }

    @GetMapping("/getCountry")
    public Country getCountry(@RequestParam String document_id) throws InterruptedException, ExecutionException {
        return countryService.getCountry(document_id);
    }

    @PutMapping("/updateCountry")
    public String updateCountry(@RequestParam Country country) throws InterruptedException, ExecutionException {
        return countryService.updateCountry(country);
    }

    @DeleteMapping("/deleteCountry")
    public String deleteCountry(@RequestParam String document_id) throws InterruptedException, ExecutionException {
        return countryService.deleteCountry(document_id);
    }

//    @GetMapping("/test")
//    public ResponseEntity<String> testGetEndpoint() {
//        return ResponseEntity.ok("The test endpoint is working");
//    }


    @PostMapping("/createAllCountries")
    public void createAllCountries () throws IOException, ExecutionException, InterruptedException {
        countryService.createAllCountries();
    }
}

