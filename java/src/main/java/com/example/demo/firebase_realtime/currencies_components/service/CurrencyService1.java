package com.example.demo.firebase_realtime.currencies_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.Currency;
import com.example.demo.parser.CSVtoJson;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService1 {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    public void postCurrency(Currency currency) {
        DatabaseReference currencyRef = ref.child("currencies");

        Map<String, Object> currencies = new HashMap<>();
        String strId = currency.getId()+"";
        currencies.put(strId,
                new Currency(currency.getId(),
                        currency.getName(),
                        currency.getCode(),
                        currency.getCode(),
                        currency.getOne_euro_rate(),
                        currency.getRtr_symbol()));

        currencyRef.updateChildrenAsync(currencies);
        System.out.println("Currency with name " + currency.getName() + " added");
    }

    public void getCurrency(String name) {
        ref.child("currencies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Currency currency = snapshot.getValue(Currency.class);
                    if (currency.getName().equals(name)){
                        System.out.println(currency);
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("That currency does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in transfer " + error);
            }
        });
    }

    public void postAllCurrencies() throws IOException {
        Gson gson = new Gson();
        String filename = Constants.CURRENCY_END_PATH;
        JsonObject[] objects = CSVtoJson.currenciesToJson(CSVtoJson.CSVoString(Constants.COMMON_PATH + filename));
        for (int i = 0; i < objects.length; i++) {
            Currency currency = gson.fromJson(objects[i], Currency.class);
            postCurrency(currency);
        }
    }
}
