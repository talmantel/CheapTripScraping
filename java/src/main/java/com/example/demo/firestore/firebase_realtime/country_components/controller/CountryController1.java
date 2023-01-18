package com.example.demo.firestore.firebase_realtime.country_components.controller;

import com.example.demo.classes.Country;
import com.example.demo.firestore.firebase_realtime.country_components.service.CountryService1;
import org.springframework.web.bind.annotation.*;

@RestController
public class CountryController1 {

    private final CountryService1 service;

    public CountryController1(CountryService1 service) {
        this.service = service;
    }

    @PostMapping("/countries")
    public void postCountry(@RequestBody Country country) throws Exception {
        try {
            service.postCountry(country);
        } catch (Exception e) {
            throw new Exception("problem with postCountry()");
        }
    }

    @GetMapping("/countries")
    public void getCountry(@RequestParam String country_name) throws Exception {
        try {
            service.getCountry(country_name);
        } catch (Exception e) {
            throw new Exception("An error occurred with getting Country");
        }
    }

    @PostMapping("/countries/all")
    public void postAllCountries() throws Exception {
        try {
            service.postAllCountries();
        } catch (Exception e) {
            throw new Exception("An error occurred with postAllCountries");
        }
    }
}
