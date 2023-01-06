package com.example.demo.parser;

import com.example.demo.Constants;

import java.io.IOException;
import java.sql.*;

public class LocalDatabaseUpdater {

    public static void routesUpdate(String[] input) {
        int k = input.length;
        try {
            Connection connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD);
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                PreparedStatement statement = connection.prepareStatement("INSERT INTO travel_data_new (`from`, `to`, transportation_type, euro_price, time_in_minutes)" +
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

    public static void main(String[] args) throws IOException {
        routesUpdate(CSVtoJson.CSVoString(Constants.PATH_ALT + "all_direct_routes.csv"));
    }
}
