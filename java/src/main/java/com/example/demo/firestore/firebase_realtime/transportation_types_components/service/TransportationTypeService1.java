package com.example.demo.firestore.firebase_realtime.transportation_types_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.TransportationType;
import com.example.demo.parser.CSVtoJson;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransportationTypeService1 {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    public void postTransportationType(TransportationType type) {
        DatabaseReference locationsRef = ref.child("transportation_types");

        Map<String, Object> types = new HashMap<>();
        String strId = type.getId() + "";
        types.put(strId,
                new TransportationType(type.getId(),
                        type.getName()));

        locationsRef.updateChildrenAsync(types);
        System.out.println("Transportation type with id " + type.getId() + " added");
    }

    public void getTransportationType(int id) {
        ref.child("transportation_types").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TransportationType type = snapshot.getValue(TransportationType.class);
                    if (type.getId() == id) {
                        System.out.println(type);
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("That transportation type does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in transfer " + error);
            }
        });
    }

    public void postAllTransportationTypes() throws IOException {
        Gson gson = new Gson();
        String filename = Constants.TRANSPORTATION_TYPES_END_PATH;
        JsonObject[] objects =
                CSVtoJson.transportationTypesToJson(CSVtoJson.CSVoString(Constants.COMMON_PATH + filename));
        for (int i = 0; i < objects.length; i++) {
            TransportationType type = gson.fromJson(objects[i], TransportationType.class);
            postTransportationType(type);
        }
    }
}
