package com.example.demo.firestore.locations_components.service;

import com.example.demo.classes.Location;
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
public class LocationService {

    final static String COMMON_PATH = "K://Programming/Graphs(from_Roman)/demo/src/main/resources/db/txt/";

    public String createLocation(Location location) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection("cheap_trip_locations").document(location.getName()).set(location);

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Location getLocation(String name) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference reference = firestore.collection("cheap_trip_locations").document(name);
        ApiFuture<DocumentSnapshot> future = reference.get();
        DocumentSnapshot snapshot = future.get();

        Location location;
        if (snapshot.exists()) {
            location = snapshot.toObject(Location.class);
            return location;
        }
        return null;
    }

    public String updateLocation(Location location) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection("cheap_trip_locations").document(location.getName()).set(location);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteLocation(String name){
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> delete = firestore.collection("cheap_trip_locations").document(name).delete();
        return "Successfully deleted " + name;
    }

    public void createAllLocations () throws IOException, ExecutionException, InterruptedException {
        JsonObject[] locations = CSVtoJson.locationsToJson(CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_locations.csv"));
        for (int i = 0; i < locations.length; i++) {
            JsonObject obj = locations[i];
            int id = obj.get("id").getAsInt();
            String name = obj.get("name").getAsString();
            int country_id = obj.get("country_id").getAsInt();
            double latitude = obj.get("latitude").getAsDouble();
            double longitude = obj.get("longitude").getAsDouble();
            String name_ru = obj.get("name_ru").getAsString();
            Location location = new Location(id,name, country_id, latitude, longitude, name_ru);
            createLocation(location);
        }
    }
}
