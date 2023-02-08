package com.example.demo.parser;

import com.example.demo.Constants;
import com.example.demo.classes.TravelData;


import java.io.IOException;
import java.sql.*;

public class LocalDatabaseUpdater {

    public static void travelDataUpdate(String[] input) {
        int k = input.length;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_base",
                    Constants.DB_USER, Constants.DB_PASSWORD);
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                PreparedStatement statement = connection.prepareStatement("INSERT INTO travel_data (`from`, `to`," +
                        " transportation_type, euro_price, time_in_minutes)" +
                        " VALUES (" + Integer.parseInt(arr[0]) + "," +
                        Integer.parseInt(arr[1]) + "," +
                        Integer.parseInt(arr[2]) + "," +
                        Float.parseFloat(arr[3]) + "," +
                        Integer.parseInt(arr[4]) + ")");
                statement.execute();
            }
            connection.close();
        } catch (SQLException E) {
            System.out.println("Problem with the connection with database");
        }
    }

    public static void travelDataAltUpdate(String[] input) {
        int k = input.length;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_base",
                    Constants.DB_USER, Constants.DB_PASSWORD);
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                TravelData data = new TravelData(Integer.parseInt(arr[0].trim()),
                        Integer.parseInt(arr[1].trim()),
                        Integer.parseInt(arr[2].trim()),
                        Integer.parseInt(arr[3].trim()),
                        (float) Integer.parseInt(arr[4].trim()),
                        Integer.parseInt(arr[5].trim()));
                String query = "INSERT INTO travel_data (id,`from`, `to`," +
                        " transportation_type, euro_price, time_in_minutes)" +
                        " VALUES (" + data.getId() + "," + data.getFrom() + "," +
                        data.getTo() + "," +
                        data.getTransportation_type() + "," +
                        data.getEuro_price() + "," +
                        data.getTime_in_minutes() + ")";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
        } catch (SQLException E) {
            System.out.println("Problem with the connection with database");
        }
    }

    public static void routesUpdate(String[] input) {
        int k = input.length;
        try {
            Connection connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String f = "";
                for (int j = 4; j < arr.length; j++) {
                    if (j == arr.length - 1) {
                        f = f + arr[j];
                    } else
                        f = f + arr[j] + ",";
                }
                f = "'" + f + "'";
                PreparedStatement statement = connection.prepareStatement("INSERT INTO routes (`from`, `to`, " +
                        "euro_price, travel_data)" +
                        " VALUES (" /*+ Integer.parseInt(arr[0]) + ","*/ +
                        Integer.parseInt(arr[1]) + "," +
                        Integer.parseInt(arr[2]) + "," +
                        Float.parseFloat(arr[3]) + "," +
                        f + ")");
                System.out.println(statement);
                statement.execute();
            }
            connection.close();
        } catch (SQLException E) {
            System.out.println("Problem with the connection with database");
        }
    }

    public static void main(String[] args) throws IOException {
        //travelDataUpdate(CSVtoJson.CSVoString(Constants.PATH_ALT + "new_routes.csv"));
        //routesUpdate(CSVtoJson.CSVoString(Constants.PATH_ALT + "new_routes.csv"));
        //travelDataAltUpdate(CSVtoJson.CSVoString("K:/Programming/Graphs(from_Roman)" +
        //        "/CheapTripScraping1/java/start_pack/csv/travel_data.csv"));
//        locationLocalUpdate(CSVtoJson.CSVoString("K:/Programming/Graphs(from_Roman)" +
//                "/CheapTripScraping1/java/start_pack/csv/locations.csv"));
        shortLocationsAdder();
    }

    public static void locationLocalUpdate(String[] input) {
        int k = input.length;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_base",
                    Constants.DB_USER, Constants.DB_PASSWORD);
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO locations_short (id, name,latitude,longitude)" +
                        " VALUES (" + Integer.parseInt(arr[0]) + "," +
                        "'" + arr[1] + "'," +
                        Double.parseDouble(arr[2]) + "," +
                        Double.parseDouble(arr[3]) + ")";
                System.out.println(query);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
        } catch (SQLException E) {
            System.out.println("Problem with the connection with database");
        }
    }

    public static void shortLocationsAdder() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_base",
                    Constants.DB_USER, Constants.DB_PASSWORD);

            String query1 = "CREATE TABLE IF NOT EXISTS locations_new AS SELECT locations_short.id,\n" +
                    "                                                   locations_short.name,\n" +
                    "                                                   locations.country_id,\n" +
                    "                                                   locations_short.latitude,\n" +
                    "                                                   locations_short.longitude,\n" +
                    "                                                   locations.name_ru\n" +
                    "FROM locations_short JOIN locations ON locations.name = locations_short.name;";
            PreparedStatement statement = connection.prepareStatement(query1);
            statement.execute();

            String query2 = "DROP TABLE locations;";
            String query3 = "DROP TABLE locations_short;";
            PreparedStatement statement1 = connection.prepareStatement(query2);
            statement1.execute();
            PreparedStatement statement2 = connection.prepareStatement(query3);
            statement2.execute();

        } catch (SQLException E) {
            System.out.println("Problem with the connection with database");
        }

    }

}
