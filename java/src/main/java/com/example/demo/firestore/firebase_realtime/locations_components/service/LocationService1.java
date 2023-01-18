package com.example.demo.firestore.firebase_realtime.locations_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.Location;
import com.example.demo.parser.CSVtoJson;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocationService1 {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    public void postLocation(Location location) {
        DatabaseReference locationsRef = ref.child("locations");

        Map<String, Object> locations = new HashMap<>();
        String strId = location.getId() + "";
        locations.put(strId,
                new Location(location.getId(),
                        location.getName(),
                        location.getCountry_id(),
                        location.getLatitude(),
                        location.getLongitude(),
                        location.getName_ru()));

        locationsRef.updateChildrenAsync(locations);
        System.out.println("Location with name " + location.getName() + " added");
    }

    public void getLocation(String name) {
        ref.child("locations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Location location = snapshot.getValue(Location.class);
                    if (location.getName().equals(name)) {
                        System.out.println(location);
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("That location does not exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in transfer " + error);
            }
        });
    }

    public void postAllLocations() throws IOException {
        Gson gson = new Gson();
        String filename = Constants.LOCATION_END_PATH;
        JsonObject[] objects = CSVtoJson.locationsToJson(CSVtoJson.CSVoString(Constants.COMMON_PATH + filename));
        for (int i = 0; i < objects.length; i++) {
            Location location = gson.fromJson(objects[i], Location.class);
            postLocation(location);
        }
    }
}
