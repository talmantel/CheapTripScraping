package com.example.demo.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParserDBtoCSV {


    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_base",
                    "root", "49918003");
            csvExtractRoutesCommon(connection, "routes", "K:/Programming/Graphs(from_Roman)" +
                    "/CheapTripScraping1/java/empty_res/new_csv/routes/common");
            csvExtractRoutesPartly(connection, "routes", "K:/Programming/Graphs(from_Roman)" +
                    "/CheapTripScraping1/java/empty_res/new_csv/routes/partly");
        } catch (SQLException e) {
            System.out.println("ParserDBtoCSV: problem");
        }
    }

    public static void csvExtractRoutesPartly(Connection connection, String table, String path) {
        List<Integer> lostLocations = new ArrayList<>();
        try {
            String query1 = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.execute();
            ResultSet locationsResultSet = statement.getResultSet();
            while (locationsResultSet.next()) {
                int locationId = locationsResultSet.getInt("id");
                String query2 = "SELECT * FROM " + table + " WHERE `from` = " + locationId;
                PreparedStatement statement1 = connection.prepareStatement(query2);
                statement1.execute();
                ResultSet fromResultSet = statement1.getResultSet();
                ArrayList<String> list = new ArrayList<>();
                while (fromResultSet.next()) {
                    int id = fromResultSet.getInt("id");
                    int from = fromResultSet.getInt("from");
                    int to = fromResultSet.getInt("to");
                    int duration = fromResultSet.getInt("trip_duration");
                    float euro_price = fromResultSet.getFloat("euro_price");
                    String travel_data = fromResultSet.getString("travel_data");
                    String result = to + "," + euro_price + "," + duration + "," + travel_data;
                    list.add(result);
                }
                String[] strings = new String[list.size()];
                list.toArray(strings);
                for (int i = 0; i < list.size(); i++) {
                    strings[i] = list.get(i);
                }
                if (strings.length == 0) {
                    lostLocations.add(locationId);
                } else {
                    csvToFilePartly(strings, locationId, path);
                }
            }
        } catch (SQLException e) {
            System.out.println("Problem with csvExtractRoutesPartly: " + table);
        }
        System.out.println(lostLocations);
    }

    public static void csvExtractRoutesCommon(Connection connection, String table, String path) {
        try {
            String query2 = "SELECT * FROM " + table;
            PreparedStatement statement1 = connection.prepareStatement(query2);
            statement1.execute();
            ResultSet fromResultSet = statement1.getResultSet();
            ArrayList<String> list = new ArrayList<>();
            while (fromResultSet.next()) {
                int id = fromResultSet.getInt("id");
                int from = fromResultSet.getInt("from");
                int to = fromResultSet.getInt("to");
                float euro_price = fromResultSet.getFloat("euro_price");
                int duration = fromResultSet.getInt("trip_duration");
                String travel_data = fromResultSet.getString("travel_data");
                String result = id + "," + from + "," + to + "," + euro_price + "," + duration + "," + travel_data;
                list.add(result);
            }
            String[] strings = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                strings[i] = list.get(i);
            }
            csvToFileCommon(strings, path, table);
        } catch (SQLException e) {
            System.out.println("Problem with csvExtractRoutesCommon: " + table);
        }
    }

    public static void csvToFilePartly(String[] routes, int locationId, String path) {
        int k = routes.length;
        String result = "";
        for (int i = 0; i < k; i++) {
            if (i < k - 1) {
                result = result + routes[i] + "\n";
            } else {
                result = result + routes[i];
            }
        }
        if (!result.equals("")) {
            String filename = path + "/from_" + locationId + ".csv";
            try (FileWriter file = new FileWriter(filename)) {
                file.write(result);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void csvToFileCommon(String[] routes, String path, String table) {
        int k = routes.length;
        String result = "";
        for (int i = 0; i < k; i++) {
            if (i < k - 1) {
                result = result + routes[i] + "\n";
            } else {
                result = result + routes[i];
            }
        }
        if (!result.equals("")) {
            String filename = path + "/" + table + "_common.csv";
            try (FileWriter file = new FileWriter(filename)) {
                file.write(result);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}