package com.example.demo.firestore.routes_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.Route;
import com.example.demo.parser.CSVtoJson;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
public class RoutesService {

    final static String ROUTES_FILENAME_TEST = "test_routes";
    final static String ROUTES_FILENAME = "cheap_trip_routes";
    final static String ROUTES_WITHOUT_FIRE_SHARE_FILENAME = "cheap_trip_routes_without_ride_share";
    final static String FIXED_ROUTES_FILENAME = "cheap_trip_fixed_routes";
    final static String FIXED_ROUTES_WITHOUT_RIDE_SHARE_FILENAME = "cheap_trip_fixed_routes_without_ride_share";
    final static String FLYING_ROUTES_FILENAME = "cheap_trip_flying_routes";

    final static String COMMON_PATH = Constants.COMMON_PATH;
    final static int AMOUNT_OF_STRINGS = 20;

    final static String CSV_ROUTES_TEST = "test2.csv";
    final static String CSV_ROUTES_FILENAME = "cheap_trip_routes.csv";
    final static String CSV_ROUTES_WITHOUT_FIRE_SHARE_FILENAME = "cheap_trip_routes_without_ride_share.csv";
    final static String CSV_FIXED_ROUTES_FILENAME = "cheap_trip_fixed_routes.csv";
    final static String CSV_FIXED_ROUTES_WITHOUT_RIDE_SHARE_FILENAME = "cheap_trip_fixed_routes_without_ride_share.csv";
    final static String CSV_FLYING_ROUTES_FILENAME = "cheap_trip_flying_routes.csv";

    public String createRoute(Route route) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection(ROUTES_FILENAME_TEST).document(route.getName()).set(route);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Route getRoute(int id) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference reference = firestore.collection(ROUTES_FILENAME_TEST).document(id+"");
        ApiFuture<DocumentSnapshot> future = reference.get();
        DocumentSnapshot snapshot = future.get();

        Route route;
        if (snapshot.exists()) {
            route = snapshot.toObject(Route.class);
            return route;
        }
        return null;
    }

    public String updateRoute(Route route) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection(ROUTES_FILENAME_TEST).document(route.getName()).set(route);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteRoute(int id){
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> delete = firestore.collection(ROUTES_FILENAME).document(id+"").delete();
        return "Successfully deleted " + id;
    }

    public void createAllRoutes () throws Exception {
        JsonObject[] routes = CSVtoJson.routesToJsonAlt(CSVtoJson.CSVoString(COMMON_PATH + CSV_ROUTES_TEST));
        int all = routes.length;
        for (int i = 0; i < all; i++) {
            JsonObject obj = routes[i];

            int id = obj.get("id").getAsInt();
            int from = obj.get("from").getAsInt();
            int to = obj.get("to").getAsInt();
            float euro_price = obj.get("euro_price").getAsFloat();

            JsonArray array = obj.getAsJsonArray("travel_data");
            ArrayList<Integer> travel_data = new ArrayList<>();
            for (int j = 0; j < array.size(); j++) {
                travel_data.add(array.get(j).getAsInt());
            }
//            int [] travel_data = new int[array.size()];
//            for (int j = 0; j < array.size(); j++) {
//                travel_data[j] = array.get(j).getAsInt();
//            }
            Route route = new Route(id,from, to, euro_price, travel_data);
            try {
                createRoute(route);
            } catch (Exception e) {
                throw new Exception("Не передалось");
            }

        }
    }
}
