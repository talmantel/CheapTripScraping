package com.example.demo.firestore.travel_data_components.service;

import com.example.demo.classes.TravelData;
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
public class TravelDataService {
    final static String TRAVEL_DATA_FILENAME = "cheap_trip_travel_data";
    final static String COMMON_PATH = "K://Programming/Graphs(from_Roman)/demo/src/main/resources/db/txt/";
    final static int AMOUNT_OF_STRINGS = 20;
    final static String CSV_TRAVEL_DATA = "cheap_trip_travel_data.csv";

    public String createData(TravelData data) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection(TRAVEL_DATA_FILENAME).document(data.getName()).set(data);

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public TravelData getData(int id) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference reference = firestore.collection(TRAVEL_DATA_FILENAME).document(id+"");
        ApiFuture<DocumentSnapshot> future = reference.get();
        DocumentSnapshot snapshot = future.get();

        TravelData data;
        if (snapshot.exists()) {
            data = snapshot.toObject(TravelData.class);
            return data;
        }
        return null;
    }

    public String updateData(TravelData data) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection(TRAVEL_DATA_FILENAME).document(data.getName()).set(data);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteData(int id){
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> delete = firestore.collection(TRAVEL_DATA_FILENAME).document(id+"").delete();
        return "Successfully deleted " + id;
    }

    public void createAllData () throws IOException, ExecutionException, InterruptedException {
        JsonObject[] datas = CSVtoJson.travelDataToJson(CSVtoJson.CSVoString(COMMON_PATH + CSV_TRAVEL_DATA));
        int all = datas.length;
        for (int i = 0; i < AMOUNT_OF_STRINGS; i++) {
            JsonObject obj = datas[i];

            int id = obj.get("id").getAsInt();
            int from = obj.get("from").getAsInt();
            int to = obj.get("to").getAsInt();
            int transportation_type = obj.get("transportation_type").getAsInt();
            float euro_price = obj.get("euro_price").getAsFloat();
            int time_in_minutes = obj.get("time_in_minutes").getAsInt();

            TravelData data = new TravelData(id, from, to, transportation_type, euro_price, time_in_minutes);
            createData(data);
        }
    }
}
