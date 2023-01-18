package com.example.demo.parser;

import com.example.demo.classes.Country;
import com.example.demo.classes.Location;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBtoJson {


    public static String url = "jdbc:mysql://localhost:3306/test_graphs";
    public static String user = "root";
    public static String password = "49918003";

    public static void main(String[] args) {
        String PATH = "K:/Programming/Graphs(from_Roman)/CheapTripScraping1/java/src/main/resources/ttt";
        String country_filename = "countries.json";
        jsonToFile(countriesToJson(), PATH + "/" + country_filename);
    }

    public static ArrayList<JsonObject> countriesToJson() {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM countries";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet countriesSet = statement.getResultSet();
            while (countriesSet.next()) {
                String country_name = countriesSet.getString("country_name");
                int country_id = countriesSet.getInt("country_id");
                String country_name_ru = countriesSet.getString("country_name_ru");

                JsonObject object = new JsonObject();
                object.addProperty("country_name", country_name);
                object.addProperty("country_id", country_id);
                object.addProperty("country_name_ru", country_name_ru);
                list.add(object);
            }
        } catch (SQLException e) {
            System.out.println("Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> currenciesToJson() {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM currencies";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet currenciesSet = statement.getResultSet();
            while (currenciesSet.next()) {
                int id = currenciesSet.getInt("id");
                String name = currenciesSet.getString("name");
                String code = currenciesSet.getString("code");
                String symbol = currenciesSet.getString("symbol");
                float one_euro_rate = currenciesSet.getFloat("one_euro_rate");
                String rtr_symbol = currenciesSet.getString("rtr_symbol");

                JsonObject object = new JsonObject();
                object.addProperty("id", id);
                object.addProperty("name", name);
                object.addProperty("code", code);
                object.addProperty("symbol", symbol);
                object.addProperty("one_euro_rate", one_euro_rate);
                object.addProperty("rtr_symbol", rtr_symbol);

                list.add(object);
            }
        } catch (SQLException e) {
            System.out.println("Problem with the connection");
        }
        return list;
    }

    public static void jsonToFile(ArrayList<JsonObject> list, String filename) {
        int k = list.size();
        JsonObject general = new JsonObject();
        for (int i = 0; i < k; i++) {
            general.add(String.valueOf(i + 1), list.get(i));
        }

        try (FileWriter file = new FileWriter(filename)) {
            System.out.println(general.toString());
            file.write(general.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
