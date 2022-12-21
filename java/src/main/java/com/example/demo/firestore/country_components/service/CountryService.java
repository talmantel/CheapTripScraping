package com.example.demo.firestore.country_components.service;

import com.example.demo.classes.Country;
import com.example.demo.parser.CSVtoJson;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class CountryService {

    final static String COMMON_PATH = "K://Programming/Graphs(from_Roman)/demo/src/main/resources/db/txt/";

    public String createCountry(Country country) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection("cheap_trip_countries").document(country.getCountry_name()).set(country);

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Country getCountry(String country_name) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference reference = firestore.collection("cheap_trip_countries").document(country_name);
        ApiFuture<DocumentSnapshot> future = reference.get();
        DocumentSnapshot snapshot = future.get();

        Country country;
        if (snapshot.exists()) {
            country = snapshot.toObject(Country.class);
            return country;
        }
        return null;
    }

    public String updateCountry(Country country) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection("cheap_trip_countries").document(country.getCountry_name()).set(country);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteCountry(String country_name){
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> delete = firestore.collection("cheap_trip_countries").document(country_name).delete();
        return "Successfully deleted " + country_name;
    }

    public void createAllCountries () throws IOException, ExecutionException, InterruptedException {
        JsonObject[] countries = CSVtoJson.countriesToJson(CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_countries.csv"));
        for (int i = 0; i < countries.length; i++) {
            JsonObject obj = countries[i];
            String country_name = obj.get("country_name").getAsString();
            int country_id = obj.get("country_id").getAsInt();
            String country_name_ru = obj.get("country_name_ru").getAsString();
            Country country = new Country(country_name, country_id, country_name_ru);
            createCountry(country);
        }
    }
}
