package org.example.functional.table_maker;

import org.example.CounterMenuTest;
import org.example.visual.Console;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewInserter {
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    public static void insertNewCountries (String [] input, Connection connection) {
        int k = input.length;
        try {
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO countries (country_name, country_id, country_name_ru) VALUES (" +
                        "'" + arr[0] + "',"
                        + Integer.parseInt(arr[1]) + ",'"
                        + arr[2] + "');";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
            stringMaker("Table 'countries' successfully loaded");
        } catch (SQLException E) {
            stringMaker("insertNewCountries: Problem with the connection with database");
        }
    }

    public static void insertNewCurrencies (String [] input, Connection connection) {
        int k = input.length;
        try {
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO currencies (id, name, code, symbol, one_euro_rate, rtr_symbol) VALUES (" +
                        Integer.parseInt(arr[0]) + ",'"
                        + arr[1] + "','"
                        + arr[2] + "','"
                        + arr[3] +  "',"
                        + Float.parseFloat(arr[4]) + ",'"
                        + arr[5] + "');";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
            stringMaker("Table 'currencies' successfully loaded");
        } catch (SQLException E) {
            stringMaker("insertNewCurrencies: Problem with the connection with database");
        }
    }

    public static void insertNewLocations (String [] input, Connection connection) {
        int k = input.length;
        try {
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO locations (id, name, country_id, latitude, longitude, name_ru) VALUES (" +
                        Integer.parseInt(arr[0]) + ",'"
                        + arr[1] + "',"
                        + Integer.parseInt(arr[2]) + ","
                        + Float.parseFloat(arr[3]) +  ","
                        + Float.parseFloat(arr[4]) + ",'"
                        + arr[5] + "');";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
            stringMaker("Table 'locations' successfully loaded");
        } catch (SQLException E) {
            stringMaker("insertNewLocations: Problem with the connection with database");
        }
    }

    public static void insertNewTransportationTypes (String [] input, Connection connection) {
        int k = input.length;
        try {
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO transportation_types (id, name) VALUES (" +
                        Integer.parseInt(arr[0]) + ",'"
                        + arr[1] + "',);";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
            stringMaker("Table 'transportation_types' successfully loaded");
        } catch (SQLException E) {
            stringMaker("insertNewTransportationTypes: Problem with the connection with database");
        }
    }

    public static void insertNewTravelDataWithIndexes (String [] input, Connection connection) {
        int k = input.length;
        try {
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO travel_data (id,`from`, `to`, transportation_type, euro_price, " +
                        "time_in_minutes) VALUES (" +
                         Integer.parseInt(arr[0]) + ","
                        + Integer.parseInt(arr[1]) + ","
                        + Integer.parseInt(arr[2]) + ","
                        + Integer.parseInt(arr[3]) +  ","
                        + Float.parseFloat(arr[4]) + ","
                        + Integer.parseInt(arr[5]) + ");";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
            stringMaker("Table 'travel_data' successfully loaded");
        } catch (SQLException E) {
            stringMaker("insertNewTravelDataWithoutIndexes: Problem with the connection with database");
        }
    }

    public static void insertNewTravelDataWithoutIndexes (String [] input, Connection connection) {
        int k = input.length;
        int count = 1;
        try {
            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO travel_data (id,`from`, `to`, transportation_type, euro_price, " +
                        "time_in_minutes) VALUES (" + count +
                        Integer.parseInt(arr[0]) + ","
                        + Integer.parseInt(arr[1]) + ","
                        + Integer.parseInt(arr[2]) +  ","
                        + Float.parseFloat(arr[3]) + ","
                        + Integer.parseInt(arr[4]) + ");";
                count++;
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
            connection.close();
            stringMaker("Table 'travel_data' successfully loaded");
        } catch (SQLException E) {
            stringMaker("insertNewTravelDataWithoutIndexes: Problem with the connection with database");
        }
    }

    public static void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
