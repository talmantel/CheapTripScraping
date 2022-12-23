package com.example.demo.firebase_realtime.country_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.Country;
import com.example.demo.parser.CSVtoJson;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CountryService1 {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    public void postCountry(Country country) {
        DatabaseReference usersRef = ref.child("countries");

        Map<String, Object> countries = new HashMap<>();
        countries.put(country.getCountry_name(),
                new Country(country.getCountry_name(),
                        country.getCountry_id(),
                        country.getCountry_name_ru()));

        usersRef.updateChildrenAsync(countries);
        System.out.println("Country with country_name " + country.getCountry_name() + " added");
    }

    public void getCountry(String country_name) {
        ref.child("countries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Country country = snapshot.getValue(Country.class);
                    if (country.getCountry_name().equals(country_name)){
                        System.out.println(country);
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("That country does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in transfer " + error);
            }
        });
    }

    public void postAllCountries() throws IOException {
        Gson gson = new Gson();
        String filename = Constants.COUNTRY_END_PATH;
        JsonObject[] objects = CSVtoJson.countriesToJson(CSVtoJson.CSVoString(Constants.COMMON_PATH + filename));
        for (int i = 0; i < objects.length; i++) {
            Country country = gson.fromJson(objects[i], Country.class);
            postCountry(country);
        }
    }
}
