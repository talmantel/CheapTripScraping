package com.example.demo.firebase_realtime.travel_data_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.TravelData;
import com.example.demo.parser.CSVtoJson;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TravelDataService1 {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    public void postTravelData(TravelData data) {
        DatabaseReference locationsRef = ref.child("travel_data");

        Map<String, Object> datas = new HashMap<>();
        String strId = data.getId()+"";
        datas.put(strId,
                new TravelData(data.getId(),
                        data.getFrom(),
                        data.getTo(),
                        data.getTransportation_type(),
                        data.getEuro_price(),
                        data.getTime_in_minutes()));

        locationsRef.updateChildrenAsync(datas);
        System.out.println("Travel data with id " + data.getId() + " added");
    }

    public void getTravelData(int id) {
        ref.child("travel_data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TravelData data = snapshot.getValue(TravelData.class);
                    if (data.getId() == id){
                        System.out.println(data);
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("That travel data does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in transfer " + error);
            }
        });
    }

    public void postAllTravelData() throws IOException {
        Gson gson = new Gson();
        String filename = Constants.TRAVEL_DATA_END_PATH;
        JsonObject[] objects = CSVtoJson.travelDataToJson(CSVtoJson.CSVoString(Constants.COMMON_PATH + filename));
        for (int i = 0; i < objects.length; i++) {
            TravelData data = gson.fromJson(objects[i], TravelData.class);
            postTravelData(data);
        }
    }
}
