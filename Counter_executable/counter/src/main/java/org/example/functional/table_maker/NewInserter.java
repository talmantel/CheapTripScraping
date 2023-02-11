package org.example.functional.table_maker;

import org.example.CounterMenuTest;
import org.example.visual.Console;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewInserter {
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);

    // Adding table 'countries' from CSV
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

    // Adding table 'currencies' from CSV
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

    // Adding new table 'locations' (6 columns) after getting (4 columns) from CSV
    public static void insertNewLocations (String [] input, Connection connection) {
        int k = input.length;
        try {

            String queryDrop = "DROP TABLE locations_old";
            PreparedStatement statement0 = connection.prepareStatement(queryDrop);
            statement0.execute();

            TablesInitializer.locationTableMakerAlt(connection);

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

    // Adding new 'locations' table (4 columns)
    public static void creatingNewLocations (String [] input, Connection connection) {
        int k = input.length;
        try {
            String initQuery = "CREATE TABLE locations_new (id INT, " +
                    "name VARCHAR(50), " +
                    "latitude FLOAT, " +
                    "longitude FLOAT, " +
                    "PRIMARY KEY(id));";
            System.out.println(initQuery);
            PreparedStatement statement0 = connection.prepareStatement(initQuery);
            statement0.execute();

            OldInserter.insertDefaultLocationsNew(connection);

            for (int i = 0; i < k; i++) {
                String[] arr = input[i].split(",");
                String query = "INSERT INTO locations_new (id, name, latitude, longitude) VALUES (" +
                        Integer.parseInt(arr[0]) + ",'"
                        + arr[1] + "',"
                        + Float.parseFloat(arr[2]) +  ","
                        + Float.parseFloat(arr[3]) + ");";
                System.out.println(query);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }

            TablesInitializer.locationTableMakerAlt(connection);

            String query1 = "INSERT INTO locations SELECT locations_new.id,\n" +
                    "                                                   locations_new.name,\n" +
                    "                                                   locations_old.country_id,\n" +
                    "                                                   locations_new.latitude,\n" +
                    "                                                   locations_new.longitude,\n" +
                    "                                                   locations_old.name_ru\n" +
                    "FROM locations_new JOIN locations_old ON locations_new.id = locations_old.id;";
            PreparedStatement statement1 = connection.prepareStatement(query1);
            System.out.println(query1);
            statement1.execute();
            String query2 = "DROP TABLE locations_new;";
            System.out.println(query2);
            String query3 = "DROP TABLE locations_old;";
            System.out.println(query3);
            PreparedStatement statement2 = connection.prepareStatement(query2);
            statement2.execute();
            PreparedStatement statement3 = connection.prepareStatement(query3);
            statement3.execute();
            connection.close();
            stringMaker("Table 'locations' successfully loaded");
        } catch (SQLException E) {
            stringMaker("creatingNewLocations: Problem with the connection with database");
        }
    }

    // Adding new table 'transportation_type' from CSV
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

    // Adding new table 'travel_data' from CSV ('id' column is in the file)
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

    // Adding new table 'travel_data' from CSV ('id' column is not in the file)
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

    // from CSV parser
    public static String[] CSVoString(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = "";
        String add = "";
        while (add != null) {
            String str = "";
            add = reader.readLine();
            if (add == null) {
                str = "";
            } else {
                str = "(" + add + "),";
            }
            line = line + str;
        }

        String[] lines = line.split("\\),\\(");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("[(')]", "");
            lines[i] = lines[i].replaceAll("null", "");
        }
        return lines;
    }

    public static void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
