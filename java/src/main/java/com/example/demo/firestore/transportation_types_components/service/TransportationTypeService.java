package com.example.demo.firestore.transportation_types_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.TransportationType;
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
public class TransportationTypeService {

    final static String COMMON_PATH = Constants.COMMON_PATH;

    public String createTransportationType(TransportationType type) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =
                firestore.collection("cheap_trip_transportation_types").document(type.getName()).set(type);

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public TransportationType getTransportationType(String name) throws ExecutionException, InterruptedException,
            NullPointerException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference reference = firestore.collection("cheap_trip_transportation_types").document(name);
        ApiFuture<DocumentSnapshot> future = reference.get();
        DocumentSnapshot snapshot = future.get();

        TransportationType type;
        if (snapshot.exists()) {
            type = snapshot.toObject(TransportationType.class);
            return type;
        }
        return null;
    }

    public String updateTransportationType(TransportationType type) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =
                firestore.collection("cheap_trip_transportation_types").document(type.getName()).set(type);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteTransportationType(String name) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> delete = firestore.collection("cheap_trip_transportation_types").document(name).delete();
        return "Successfully deleted " + name;
    }

    public void createAllTransportationTypes() throws IOException, ExecutionException, InterruptedException {
        JsonObject[] transportationTypes = CSVtoJson.transportationTypesToJson(CSVtoJson.CSVoString(COMMON_PATH +
                "cheap_trip_transportation_types.csv"));
        for (int i = 0; i < transportationTypes.length; i++) {
            JsonObject obj = transportationTypes[i];
            int id = obj.get("id").getAsInt();
            String name = obj.get("name").getAsString();
            TransportationType type = new TransportationType(id, name);
            createTransportationType(type);
        }
    }
}
