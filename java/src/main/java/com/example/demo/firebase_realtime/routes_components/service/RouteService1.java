package com.example.demo.firebase_realtime.routes_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.Route;
import com.example.demo.parser.CSVtoJson;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RouteService1 {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    public void postRoute(Route route) {
        DatabaseReference locationsRef = ref.child("routes");

        Map<String, Object> routes = new HashMap<>();
        String strId = route.getId()+"";
        routes.put(strId,
                new Route(route.getId(),
                        route.getFrom(),
                        route.getTo(),
                        route.getEuro_price(),
                        route.getTravel_data()));

        locationsRef.updateChildrenAsync(routes);
        System.out.println("Route with id " + route.getId() + " added");
    }

    public void getRoute(int id) {
        ref.child("routes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Route route = snapshot.getValue(Route.class);
                    if (route.getId() == id){
                        System.out.println(route);
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("That route does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error in transfer " + error);
            }
        });
    }

    public void postAllRoutes() throws IOException {
        Gson gson = new Gson();
        String filename = Constants.ROUTES_END_PATH;
        JsonObject[] objects = CSVtoJson.routesToJsonAlt(CSVtoJson.CSVoString(Constants.COMMON_PATH + filename));
        for (int i = 0; i < objects.length; i++) {
            Route route = gson.fromJson(objects[i], Route.class);
            postRoute(route);
        }
    }
}
