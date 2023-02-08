package org.example.functional.table_maker;

import org.example.CounterMenuTest;
import org.example.visual.Console;


import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TablesInitializer {
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    public static void locationTableMaker(Connection connection) {

        try {
            String query = "CREATE TABLE IF NOT EXISTS locations_old(\n" +
                    "     id INT,\n" +
                    "     name VARCHAR(50),\n" +
                    "     country_id INT,\n" +
                    "     latitude DOUBLE,\n" +
                    "     longitude DOUBLE,\n" +
                    "     name_ru VARCHAR(60),\n" +
                    "     PRIMARY KEY(id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'locations' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'locations' creating");
        }
    }

    public static void locationTableMakerAlt(Connection connection) {

        try {
            String query = "CREATE TABLE IF NOT EXISTS locations(\n" +
                    "     id INT,\n" +
                    "     name VARCHAR(50),\n" +
                    "     country_id INT,\n" +
                    "     latitude DOUBLE,\n" +
                    "     longitude DOUBLE,\n" +
                    "     name_ru VARCHAR(60),\n" +
                    "     PRIMARY KEY(id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'locations' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'locations' creating");
        }
    }

    public static void routesTableMaker(Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS routes(\n" +
                    "     id INT AUTO_INCREMENT,\n" +
                    "     `from` INT,\n" +
                    "     `to` INT,\n" +
                    "     euro_price FLOAT,\n" +
                    "     trip_duration INT,\n" +
                    "     travel_data VARCHAR(255),\n" +
                    "     PRIMARY KEY (id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'routes' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'routes' creating");
        }
    }

    public static void travelDataTableMaker (Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS travel_data(\n" +
                    "     id INT,\n" +
                    "     `from` INT,\n" +
                    "     `to` INT,\n" +
                    "     transportation_type INT,\n" +
                    "     euro_price FLOAT,\n" +
                    "     time_in_minutes INT,\n" +
                    "     PRIMARY KEY (id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'travel_data' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'travel_data' creating");
        }
    }

    public static void flyingRoutesTableMaker(Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS flying_routes(\n" +
                    "     id INT AUTO_INCREMENT,\n" +
                    "     `from` INT,\n" +
                    "     `to` INT,\n" +
                    "     euro_price FLOAT,\n" +
                    "     trip_duration INT,\n" +
                    "     travel_data VARCHAR(255),\n" +
                    "     PRIMARY KEY (id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'flying_routes' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'flying_routes' creating");
        }
    }

    public static void fixedRoutesTableMaker(Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS fixed_routes(\n" +
                    "     id INT AUTO_INCREMENT,\n" +
                    "     `from` INT,\n" +
                    "     `to` INT,\n" +
                    "     euro_price FLOAT,\n" +
                    "     trip_duration INT,\n" +
                    "     travel_data VARCHAR(255),\n" +
                    "     PRIMARY KEY (id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'fixed_routes' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'fixed_routes' creating");
        }
    }

    //additional_tables
    public static void countriesMaker (Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS countries(\n" +
                    "     country_name VARCHAR(60),\n" +
                    "     country_id INT,\n" +
                    "     country_name_ru VARCHAR(60),\n" +
                    "     PRIMARY KEY(country_id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'countries' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'countries' creating");
        }
    }

    public static void currenciesMaker (Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS currencies(\n" +
                    "     id INT,\n" +
                    "     name VARCHAR(40),\n" +
                    "     code VARCHAR(20),\n" +
                    "     symbol VARCHAR(10),\n" +
                    "     one_euro_rate FLOAT,\n" +
                    "     rtr_symbol VARCHAR(30),\n" +
                    "     PRIMARY KEY(id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'currencies' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'currencies' creating");
        }
    }

    public static void transportationTypesMaker (Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS transportation_types(\n" +
                    "     id INT,\n" +
                    "     name VARCHAR(30),\n" +
                    "     PRIMARY KEY (id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'transportation_types' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'transportation_types' creating");
        }
    }

    public static void travelDataCountMaker (Connection connection) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS travel_data_counter(\n" +
                    "     id INT,\n" +
                    "     count INT,\n" +
                    "     PRIMARY KEY (id)\n" +
                    ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'travel_data_counter' created");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'travel_data_counter' creating");
        }
    }

    public static void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
