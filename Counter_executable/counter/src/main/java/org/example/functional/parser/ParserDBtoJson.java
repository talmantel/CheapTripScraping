package org.example.functional.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.CounterMenuTest;
import org.example.visual.Console;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;

public class ParserDBtoJson {

    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    public static ArrayList<JsonObject> countriesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
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
            stream.println("countriesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> currenciesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
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
            stream.println("currenciesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> locationsToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet locationsSet = statement.getResultSet();
            while (locationsSet.next()) {
                int id = locationsSet.getInt("id");
                String name = locationsSet.getString("name");
                int country_id = locationsSet.getInt("country_id");
                double latitude = locationsSet.getDouble("latitude");
                double longitude = locationsSet.getDouble("longitude");
                String name_ru = locationsSet.getString("name_ru");

                JsonObject object = new JsonObject();
                object.addProperty("id", id);
                object.addProperty("name", name);
                object.addProperty("country_id", country_id);
                object.addProperty("latitude",latitude);
                object.addProperty("longitude", longitude);
                object.addProperty("name_ru", name_ru);
                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("locationsToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> travelDataToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
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
                object.addProperty("id",id);
                object.addProperty("from", from);
                object.addProperty("to", to);
                object.addProperty("transportation_type", transportation_type);
                object.addProperty("euro_price", euro_price);
                object.addProperty("time_in_minutes", time_in_minutes);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("travelDataJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> transportationTypesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM transportation_types";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet transportationTypesSet = statement.getResultSet();
            while (transportationTypesSet.next()) {
                int id = transportationTypesSet.getInt("id");
                String name = transportationTypesSet.getString("name");

                JsonObject object = new JsonObject();
                object.addProperty("id",id);
                object.addProperty("name", name);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("transportationTypesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> routesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet routesSet = statement.getResultSet();
            while (routesSet.next()) {
                int id = routesSet.getInt("id");
                int from = routesSet.getInt("from");
                int to = routesSet.getInt("to");
                float euro_price = routesSet.getFloat("euro_price");
                String travel_data = routesSet.getString("travel_data");
                JsonArray array = new JsonArray();
                String [] travel = travel_data.split(",");
                for (int i = 0; i < travel.length; i++) {
                    array.add(travel[i]);
                }

                JsonObject object = new JsonObject();
                object.addProperty("id",id);
                object.addProperty("from",from);
                object.addProperty("to", to);
                object.addProperty("euro_price", euro_price);
                object.add("travel_data", array);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("routesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> fixedRoutesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM fixed_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet fixesRoutesSet = statement.getResultSet();
            while (fixesRoutesSet.next()) {
                int id = fixesRoutesSet.getInt("id");
                int from = fixesRoutesSet.getInt("from");
                int to = fixesRoutesSet.getInt("to");
                float euro_price = fixesRoutesSet.getFloat("euro_price");
                String travel_data = fixesRoutesSet.getString("travel_data");
                JsonArray array = new JsonArray();
                String [] travel = travel_data.split(",");
                for (int i = 0; i < travel.length; i++) {
                    array.add(travel[i]);
                }

                JsonObject object = new JsonObject();
                object.addProperty("id",id);
                object.addProperty("from",from);
                object.addProperty("to", to);
                object.addProperty("euro_price", euro_price);
                object.add("travel_data", array);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("fixedRoutesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> flyingRoutesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM flying_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet flyingRoutesSet = statement.getResultSet();
            while (flyingRoutesSet.next()) {
                int id = flyingRoutesSet.getInt("id");
                int from = flyingRoutesSet.getInt("from");
                int to = flyingRoutesSet.getInt("to");
                float euro_price = flyingRoutesSet.getFloat("euro_price");
                String travel_data = flyingRoutesSet.getString("travel_data");

                JsonArray array = new JsonArray();
                String [] travel = travel_data.split(",");
                for (int i = 0; i < travel.length; i++) {
                    array.add(travel[i]);
                }
                JsonObject object = new JsonObject();
                object.addProperty("id",id);
                object.addProperty("from",from);
                object.addProperty("to", to);
                object.addProperty("euro_price", euro_price);
                object.add("travel_data", array);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("flyingRoutesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> travelDataCounterRoutesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM travel_data_counter_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet travelDataCountSet = statement.getResultSet();
            while (travelDataCountSet.next()) {
                int id = travelDataCountSet.getInt("id");
                int from = travelDataCountSet.getInt("from");
                int to = travelDataCountSet.getInt("to");
                int transportation_type = travelDataCountSet.getInt("transportation_type");
                float euro_price = travelDataCountSet.getFloat("euro_price");
                int time_in_minutes = travelDataCountSet.getInt("time_in_minutes");
                int count = (int) travelDataCountSet.getDouble("count");

                JsonObject object = new JsonObject();
                object.addProperty("id",id);
                object.addProperty("from", from);
                object.addProperty("to", to);
                object.addProperty("transportation_type", transportation_type);
                object.addProperty("euro_price", euro_price);
                object.addProperty("time_in_minutes", time_in_minutes);
                object.addProperty("count", count);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("travelDataCounterRoutesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> travelDataCounterFixedRoutesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM travel_data_counter_fixed_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet travelDataCountFixedSet = statement.getResultSet();
            while (travelDataCountFixedSet.next()) {
                int id = travelDataCountFixedSet.getInt("id");
                int from = travelDataCountFixedSet.getInt("from");
                int to = travelDataCountFixedSet.getInt("to");
                int transportation_type = travelDataCountFixedSet.getInt("transportation_type");
                float euro_price = travelDataCountFixedSet.getFloat("euro_price");
                int time_in_minutes = travelDataCountFixedSet.getInt("time_in_minutes");
                int count = (int) travelDataCountFixedSet.getDouble("count");

                JsonObject object = new JsonObject();
                object.addProperty("id",id);
                object.addProperty("from", from);
                object.addProperty("to", to);
                object.addProperty("transportation_type", transportation_type);
                object.addProperty("euro_price", euro_price);
                object.addProperty("time_in_minutes", time_in_minutes);
                object.addProperty("count", count);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("travelDataCounterFixedRoutesToJson: Problem with the connection");
        }
        return list;
    }

    public static ArrayList<JsonObject> travelDataCounterFlyingRoutesToJson(Connection connection) {
        ArrayList<JsonObject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM travel_data_counter_flying_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet travelDataCountFlyingSet = statement.getResultSet();
            while (travelDataCountFlyingSet.next()) {
                int id = travelDataCountFlyingSet.getInt("id");
                int from = travelDataCountFlyingSet.getInt("from");
                int to = travelDataCountFlyingSet.getInt("to");
                int transportation_type = travelDataCountFlyingSet.getInt("transportation_type");
                float euro_price = travelDataCountFlyingSet.getFloat("euro_price");
                int time_in_minutes = travelDataCountFlyingSet.getInt("time_in_minutes");
                int count = (int) travelDataCountFlyingSet.getDouble("count");

                JsonObject object = new JsonObject();
                object.addProperty("id",id);
                object.addProperty("from", from);
                object.addProperty("to", to);
                object.addProperty("transportation_type", transportation_type);
                object.addProperty("euro_price", euro_price);
                object.addProperty("time_in_minutes", time_in_minutes);
                object.addProperty("count", count);

                list.add(object);
            }
        } catch (SQLException e) {
            stream.println("travelDataCounterFlyingRoutesToJson: Problem with the connection");
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
            file.write(general.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
