package org.example.functional.parser;

import com.google.gson.JsonObject;
import org.example.CounterMenuTest;
import org.example.visual.Console;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;

public class ParserDBtoCSV {

    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    public static void csvExtractRoutes (Connection connection, String path) {
        try {
            String query1 = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.execute();
            ResultSet locationsResultSet = statement.getResultSet();
            while (locationsResultSet.next()) {
                int locationId = locationsResultSet.getInt("id");
                String query2 = "SELECT * FROM routes WHERE `from` = " + locationId;
                PreparedStatement statement1 = connection.prepareStatement(query2);
                statement1.execute();
                ResultSet fromResultSet = statement1.getResultSet();
                ArrayList <String> list = new ArrayList<>();
                while (fromResultSet.next()) {
                    int id = fromResultSet.getInt("id");
                    int from = fromResultSet.getInt("from");
                    int to = fromResultSet.getInt("to");
                    float euro_price = fromResultSet.getFloat("euro_price");
                    String travel_data = fromResultSet.getString("travel_data");
                    String result = id + "," + from + "," + to + "," + euro_price + "," + travel_data;
                    list.add(result);
                }
                String [] strings = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    strings[i] = list.get(i);
                }
                csvToFile(strings,locationId, path);
            }
        } catch (SQLException e) {
            stream.println("problemWithCSVExtractRoutes");
        }
    }

    public static void csvExtractFixedRoutes (Connection connection, String path) {
        try {
            String query1 = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.execute();
            ResultSet locationsResultSet = statement.getResultSet();
            while (locationsResultSet.next()) {
                int locationId = locationsResultSet.getInt("id");
                String query2 = "SELECT * FROM fixed_routes WHERE `from` = " + locationId;
                PreparedStatement statement1 = connection.prepareStatement(query2);
                statement1.execute();
                ResultSet fromResultSet = statement1.getResultSet();
                ArrayList <String> list = new ArrayList<>();
                while (fromResultSet.next()) {
                    int id = fromResultSet.getInt("id");
                    int from = fromResultSet.getInt("from");
                    int to = fromResultSet.getInt("to");
                    float euro_price = fromResultSet.getFloat("euro_price");
                    String travel_data = fromResultSet.getString("travel_data");
                    String result = id + "," + from + "," + to + "," + euro_price + "," + travel_data;
                    list.add(result);
                }
                String [] strings = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    strings[i] = list.get(i);
                }
                csvToFile(strings,locationId, path);
            }
        } catch (SQLException e) {
            stream.println("problemWithCSVExtractFixedRoutes");
        }
    }

    public static void csvExtractFlyingRoutes (Connection connection, String path) {
        try {
            String query1 = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.execute();
            ResultSet locationsResultSet = statement.getResultSet();
            while (locationsResultSet.next()) {
                int locationId = locationsResultSet.getInt("id");
                String query2 = "SELECT * FROM flying_routes WHERE `from` = " + locationId;
                PreparedStatement statement1 = connection.prepareStatement(query2);
                statement1.execute();
                ResultSet fromResultSet = statement1.getResultSet();
                ArrayList <String> list = new ArrayList<>();
                while (fromResultSet.next()) {
                    int id = fromResultSet.getInt("id");
                    int from = fromResultSet.getInt("from");
                    int to = fromResultSet.getInt("to");
                    float euro_price = fromResultSet.getFloat("euro_price");
                    String travel_data = fromResultSet.getString("travel_data");
                    String result = id + "," + from + "," + to + "," + euro_price + "," + travel_data;
                    list.add(result);
                }
                String [] strings = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    strings[i] = list.get(i);
                }
                csvToFile(strings,locationId, path);
            }
        } catch (SQLException e) {
            stream.println("problemWithCSVExtractFlyingRoutes");
        }
    }

    public static void csvToFile(String [] routes, int locationId, String path) {
        int k = routes.length;
        String result = "";
        for (int i = 0; i < k; i++) {
            if (i < k-1) {
                result = result + routes[i] + "\n";
            } else {
                result = result + routes[i];
            }
        }
        String filename = path + "/from_location_id_" + locationId + ".csv";
        try (FileWriter file = new FileWriter(filename)) {
            file.write(result);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}