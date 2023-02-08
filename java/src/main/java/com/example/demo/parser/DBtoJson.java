package com.example.demo.parser;

import com.example.demo.classes.Country;
import com.example.demo.classes.Location;
import com.google.api.client.json.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.checkerframework.checker.index.qual.PolyUpperBound;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBtoJson {


    public static String url = "jdbc:mysql://localhost:3306/data_base";
    public static String user = "root";
    public static String password = "49918003";
    public static String PATH_FIXED_COMMON = "K:/Programming/Graphs(from_Roman)" +
            "/CheapTripScraping1/java/empty_res/new_json/fixed_routes/common";
    public static String PATH_FIXED_PARTLY = "K:/Programming/Graphs(from_Roman)" +
            "/CheapTripScraping1/java/empty_res/new_json/fixed_routes/partly";

    public static String PATH_ROUTES_COMMON = "K:/Programming/Graphs(from_Roman)" +
            "/CheapTripScraping1/java/empty_res/new_json/routes/common";
    public static String PATH_ROUTES_PARTLY = "K:/Programming/Graphs(from_Roman)" +
            "/CheapTripScraping1/java/empty_res/new_json/routes/partly";

    public static String PATH_FLYING_COMMON = "K:/Programming/Graphs(from_Roman)" +
            "/CheapTripScraping1/java/empty_res/new_json/flying_routes/common";
    public static String PATH_FLYING_PARTLY = "K:/Programming/Graphs(from_Roman)" +
            "/CheapTripScraping1/java/empty_res/new_json/flying_routes/partly";

    public static void main(String[] args) {
        String PATH = "K:/Programming/Graphs(from_Roman)/CheapTripScraping1/java/empty_res/new_json";
//        String country_filename = "countries.json";
//        jsonToFile(countriesToJson(), PATH + "/" + country_filename);
//        jsonToFile(routesToJson("routes"), PATH + "/routes.json");
//        jsonToFile(routesToJson("fixed_routes"), PATH + "/fixed_routes.json");
//        jsonToFile(routesToJson("flying_routes"), PATH + "/flying_routes.json");
//        jsonToFile(locationsToJson(), PATH + "/locations.json");
//        jsonToFile(travelDataToJson(), PATH + "/travel_data.json");
//        routesFromLocationsToJson("fixed_routes", PATH_FIXED_PARTLY);
//        routesFromLocationsToJson("routes", PATH_ROUTES_PARTLY);
//        routesFromLocationsToJson("flying_routes", PATH_FLYING_PARTLY);
        jsonToFile(transportationTypeToJson(), PATH + "/transportation_types.json");
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

    public static ArrayList<JsonObject> routesToJson(String typeOfRoutesDB) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM " + typeOfRoutesDB;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet routesSet = statement.getResultSet();
            while (routesSet.next()) {
                int id = routesSet.getInt("id");
                int from = routesSet.getInt("from");
                int to = routesSet.getInt("to");
                float euro_price = routesSet.getFloat("euro_price");
                int trip_duration = routesSet.getInt("trip_duration");
                String travel_data = routesSet.getString("travel_data");
                String[] arr = travel_data.split(",");
                JsonArray routes = new JsonArray();
                for (int i = 0; i < arr.length; i++) {
                    routes.add(Integer.parseInt(arr[i]));
                }
                JsonObject object = new JsonObject();
                object.addProperty("id", id);
                object.addProperty("from", from);
                object.addProperty("to", to);
                object.addProperty("euro_price", euro_price);
                object.addProperty("trip_duration", trip_duration);
                object.add("travel_data", routes);
                //object.addProperty("travel_data", travel_data);
                list.add(object);
            }
        } catch (SQLException e) {
            System.out.println("Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> locationsToJson() {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet locationsSet = statement.getResultSet();
            while (locationsSet.next()) {
                int id = locationsSet.getInt("id");
                String name = locationsSet.getString("name");
                double latitude = locationsSet.getDouble("latitude");
                double longitude = locationsSet.getDouble("longitude");

                JsonObject object = new JsonObject();
                object.addProperty("id", id);
                object.addProperty("name", name);
                object.addProperty("latitude", latitude);
                object.addProperty("longitude", longitude);
                list.add(object);
            }
        } catch (SQLException e) {
            System.out.println("Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> travelDataToJson() {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM travel_data";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet travelDataSet = statement.getResultSet();
            while (travelDataSet.next()) {
                int id = travelDataSet.getInt("id");
                int from = travelDataSet.getInt("from");
                int to = travelDataSet.getInt("to");
                int transportation_type = travelDataSet.getInt("transportation_type");
                float euro_price = travelDataSet.getFloat("euro_price");
                int time_in_minutes = travelDataSet.getInt("time_in_minutes");

                JsonObject object = new JsonObject();
                object.addProperty("id", id);
                object.addProperty("from", from);
                object.addProperty("to", to);
                object.addProperty("transportation_type", transportation_type);
                object.addProperty("euro_price", euro_price);
                object.addProperty("time_in_minutes", time_in_minutes);
                list.add(object);
            }
        } catch (SQLException e) {
            System.out.println("Problem with the connection");
        }
        return list;
    }

    public static void routesFromLocationsToJson(String tableType, String path) {
        List<Integer> lostLocations = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query1 = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.execute();
            ResultSet locationsResultSet = statement.getResultSet();
            while (locationsResultSet.next()) {
                int locationId = locationsResultSet.getInt("id");
                String query2 = "SELECT * FROM " + tableType + " WHERE `from` = " + locationId;
                PreparedStatement statement1 = connection.prepareStatement(query2);
                statement1.execute();
                ResultSet fromResultSet = statement1.getResultSet();
                ArrayList<JsonObject> list = new ArrayList<>();
                while (fromResultSet.next()) {
                    int id = fromResultSet.getInt("id");
                    int from = fromResultSet.getInt("from");
                    int to = fromResultSet.getInt("to");
                    float euro_price = fromResultSet.getFloat("euro_price");
                    int trip_duration = fromResultSet.getInt("trip_duration");
                    String travel_data = fromResultSet.getString("travel_data");
                    String[] arr = travel_data.split(",");
                    JsonArray routes = new JsonArray();
                    for (int i = 0; i < arr.length; i++) {
                        routes.add(Integer.parseInt(arr[i]));
                    }
                    JsonObject object = new JsonObject();
                    object.addProperty("to", to);
                    object.addProperty("euro_price", euro_price);
                    object.addProperty("trip_duration", trip_duration);
                    object.add("travel_data", routes);
                    list.add(object);
                }
                if (!list.isEmpty()) {
                    jsonToFile(list, path + "/from_" + locationId + ".json");
                } else {
                    lostLocations.add(locationId);
                }
            }
        } catch (SQLException e) {
            System.out.println("routesFromLocationsToJson: problem");
        }
        System.out.println(lostLocations);
    }

    public static ArrayList<JsonObject> transportationTypeToJson() {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query1 = "SELECT * FROM transportation_types";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.execute();
            ResultSet set = statement.getResultSet();
            while (set.next()) {
                int id = set.getInt("id");
                String name = set.getString("name");
                JsonObject object = new JsonObject();
                object.addProperty("id", id);
                object.addProperty("name", name);
                list.add(object);
            }
        } catch (SQLException e) {
            System.out.println("transportationTypeToJson: problem");
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
