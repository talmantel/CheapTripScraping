package com.example.demo.firestore.firebase_realtime.currencies_components.controller;

import com.example.demo.classes.Currency;
import com.example.demo.firestore.firebase_realtime.currencies_components.service.CurrencyService1;
import org.springframework.web.bind.annotation.*;

@RestController
public class CurrencyController1 {

    private final CurrencyService1 service;

    public CurrencyController1(CurrencyService1 service) {
        this.service = service;
    }

    @PostMapping("/currencies")
    public void postCurrency(@RequestBody Currency currency) throws Exception {
        try {
            service.postCurrency(currency);
        } catch (Exception e) {
            throw new Exception("problem with postCurrency()");
        }
    }

    @GetMapping("/currencies")
    public void getCurrency(@RequestParam String name) throws Exception {
        try {
            service.getCurrency(name);
        } catch (Exception e) {
            throw new Exception("An error occurred with getCurrency()");
        }
    }

    @PostMapping("/currencies/all")
    public void postAllCurrencies() throws Exception {
        try {
            service.postAllCurrencies();
        } catch (Exception e) {
            throw new Exception("An error occurred with postAllCountries");
        }
    }
}
