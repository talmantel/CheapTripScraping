package org.example.functional.table_maker;

import org.example.CounterMenuTest;
import org.example.visual.Console;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableDropper {

    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);

    // drop 'transportation_types'
    public static void transportationTypesDropper (Connection connection) {
        try {
            String query = "DROP TABLE transportation_types";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'transportation_types' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'transportation_types' dropping");
        }
    }

    // drop 'countries'
    public static void countryDropper (Connection connection) {
        try {
            String query = "DROP TABLE countries";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'countries' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'countries' dropping");
        }
    }

    // drop 'currencies'
    public static void currenciesDropper (Connection connection) {
        try {
            String query = "DROP TABLE currencies";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'currencies' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'currencies' dropping");
        }
    }

    // drop 'locations'
    public static void locationsDropper (Connection connection) {
        try {
            String query = "DROP TABLE locations";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'locations' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'locations' dropping");
        }
    }

    // drop 'travel_data'
    public static void travelDataDropper (Connection connection) {
        try {
            String query = "DROP TABLE travel_data";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'travel_data' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'travel_data' dropping");
        }
    }

    // drop 'routes'
    public static void routesDropper (Connection connection) {
        try {
            String query = "DROP TABLE routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'routes' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'routes' dropping");
        }
    }

    // drop 'flying_routes'
    public static void flyingRoutesDropper (Connection connection) {
        try {
            String query = "DROP TABLE flying_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'flying' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'flying_routes' dropping");
        }
    }

    // drop 'fixed_routes'
    public static void fixedRoutesDropper (Connection connection) {
        try {
            String query = "DROP TABLE fixed_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'fixed_routes' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'fixed_routes' dropping");
        }
    }

    // drop 'travel_data_counter_routes'
    public static void travelDataCounterRoutesDropper(Connection connection) {
        try {
            String query = "DROP TABLE travel_data_counter_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'travel_data_counter_routes' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'travel_data_counter_routes' dropping");
        }
    }

    // drop 'travel_data_counter_fixed_routes'
    public static void travelDataCounterFixedRoutesDropper(Connection connection) {
        try {
            String query = "DROP TABLE travel_data_counter_fixed_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'travel_data_counter_fixed_routes' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'travel_data_counter_fixed_routes' dropping");
        }
    }

    // drop 'travel_data_counter_flying_routes'
    public static void travelDataCounterFlyingRoutesDropper(Connection connection) {
        try {
            String query = "DROP TABLE travel_data_counter_flying_routes";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Table 'travel_data_counter_flying_routes' was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while table 'travel_data_counter_flying_routes' dropping");
        }
    }

    // drop schema
    public static void schemaDrop (Connection connection, String schema) {
        try {
            String query = "DROP SCHEMA " + schema;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            stringMaker("Schema " + schema + " was dropped");
        } catch (SQLException e) {
            stringMaker("An error occurred while schema " + schema + " dropping");
        }
    }
    public static void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
