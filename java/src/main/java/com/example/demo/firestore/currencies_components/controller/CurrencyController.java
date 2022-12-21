package com.example.demo.firestore.currencies_components.controller;

import com.example.demo.classes.Currency;
import com.example.demo.firestore.currencies_components.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class CurrencyController {

    public CurrencyService currencyService;

    public CurrencyController (CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/createCurrency")
    public String createCountry(@RequestBody Currency currency) throws InterruptedException, ExecutionException {
        return currencyService.createCurrency(currency);
    }

    @GetMapping("/getCurrency")
    public Currency getCountry(@RequestParam String document_id) throws InterruptedException, ExecutionException {
        return currencyService.getCurrency(document_id);
    }

    @PutMapping("/updateCurrency")
    public String updateCurrency(@RequestParam Currency currency) throws InterruptedException, ExecutionException {
        return currencyService.updateCurrency(currency);
    }

    @DeleteMapping("/deleteCurrency")
    public String deleteCurrency(@RequestParam String document_id) throws InterruptedException, ExecutionException {
        return currencyService.deleteCurrency(document_id);
    }

//    @GetMapping("/test")
//    public ResponseEntity<String> testGetEndpoint() {
//        return ResponseEntity.ok("The test endpoint is working");
//    }


    @PostMapping("/createAllCurrencies")
    public void createAllCurrencies () throws IOException, ExecutionException, InterruptedException {
        currencyService.createAllCurrencies();
    }
}
