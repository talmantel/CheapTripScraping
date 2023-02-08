package com.example.demo.firestore.currencies_components.service;

import com.example.demo.Constants;
import com.example.demo.classes.Currency;
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
public class CurrencyService {

    final static String COMMON_PATH = Constants.COMMON_PATH;

    public String createCurrency(Currency currency) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =
                firestore.collection("cheap_trip_currencies").document(currency.getName()).set(currency);

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Currency getCurrency(String name) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference reference = firestore.collection("cheap_trip_currencies").document(name);
        ApiFuture<DocumentSnapshot> future = reference.get();
        DocumentSnapshot snapshot = future.get();

        Currency currency;
        if (snapshot.exists()) {
            currency = snapshot.toObject(Currency.class);
            return currency;
        }
        return null;
    }

    public String updateCurrency(Currency currency) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =
                firestore.collection("cheap_trip_currencies").document(currency.getName()).set(currency);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteCurrency(String name) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> delete = firestore.collection("cheap_trip_currencies").document(name).delete();
        return "Successfully deleted " + name;
    }

    public void createAllCurrencies() throws IOException, ExecutionException, InterruptedException {
        JsonObject[] currencies = CSVtoJson.currenciesToJson(CSVtoJson.CSVoString(COMMON_PATH +
                "cheap_trip_currencies.csv"));
        for (int i = 0; i < currencies.length; i++) {
            JsonObject obj = currencies[i];
            int id = obj.get("id").getAsInt();
            String name = obj.get("name").getAsString();
            String code = obj.get("code").getAsString();
            String symbol = obj.get("symbol").getAsString();
            float one_euro_rate = obj.get("one_euro_rate").getAsFloat();
            String rtr_symbol = obj.get("rtr_symbol").getAsString();
            Currency currency = new Currency(id, name, code, symbol, one_euro_rate, rtr_symbol);
            createCurrency(currency);
        }
    }
}
