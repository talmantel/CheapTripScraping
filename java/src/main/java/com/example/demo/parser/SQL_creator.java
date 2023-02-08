package com.example.demo.parser;

import java.io.*;

public class SQL_creator {

    public static void main(String[] args) throws IOException {
//        String routes = CSVtoString("K:/Programming/Graphs(from_Roman)/CheapTripScraping1/java/empty_res/td and " +
//                "locations/routes.csv");
//        String fixed_routes = CSVtoString("K:/Programming/Graphs(from_Roman)/CheapTripScraping1/java/empty_res/td
//        and" +
//                " locations/fixed_routes.csv");
//        String flying_routes = CSVtoString("K:/Programming/Graphs(from_Roman)/CheapTripScraping1/java/empty_res/td " +
//                "and locations/flying_routes.csv");
//        String locations = CSVLocationsToString("K:/Programming/Graphs(from_Roman)" +
//                "/CheapTripScraping1/java/empty_res/td and locations/locations.csv");
//        String travel_data = CSVtoString("K:/Programming/Graphs(from_Roman)" +
//                "/CheapTripScraping1/java/empty_res/td and locations/travel_data.csv");
//        String transportation_types = CSVTTypesToString("K:/Programming/Graphs(from_Roman)" +
//                "/CheapTripScraping1/java/empty_res/td " +
//                "and locations/transportation_types.csv");
        String travel_data_routes_counter = CSVtoString("K:/Programming/Graphs(from_Roman)" +
                "/CheapTripScraping1/java/empty_res/td and locations/travel_data_counter_routes.csv");
        String travel_data_fixed_routes_counter = CSVtoString("K:/Programming/Graphs(from_Roman)" +
                "/CheapTripScraping1/java/empty_res/td and locations/travel_data_counter_fixed_routes.csv");
        String travel_data_flying_routes_counter = CSVtoString("K:/Programming/Graphs(from_Roman)" +
                "/CheapTripScraping1/java/empty_res/td and locations/travel_data_counter_flying_routes.csv");

//        routesCreatingSQL(routes, "routes", "routes.sql");
//        routesCreatingSQL(fixed_routes, "fixed_routes", "fixed_routes.sql");
//        routesCreatingSQL(flying_routes, "flying_routes", "flying_routes.sql");
//        locationsCreatingSQL(locations, "locations", "locations.sql");
//        travelDataCreatingSQL(travel_data, "travel_data", "travel_data.sql");
//        transportationTypesCreatingSQL(transportation_types, "transportation_types.sql");
//        travelDataCreatingSQL(travel_data_routes_counter, "travel_data_counter_routes", "travel_data_counter_routes" +
//                ".sql");
//        travelDataCreatingSQL(travel_data_fixed_routes_counter, "travel_data_counter_fixed_routes",
//                "travel_data_counter_fixed_routes" +
//                        ".sql");
        travelDataCreatingSQL(travel_data_flying_routes_counter, "travel_data_counter_flying_routes",
                "travel_data_counter_flying_routes" +
                        ".sql");
    }

    public static String PATH = "K:/Programming/Graphs(from_Roman)/CheapTripScraping1/java/empty_res/new_tables_sql";

    public static String CSVLocationsToString(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = "";
        String add = "";

        while (add != null) {
            String str = "";
            add = reader.readLine();
            if (add != null) {
                String[] arr = add.split(",");
                String edit = arr[0] + ",'" + arr[1] + "'," + arr[2] + "," + arr[3];
                str = "(" + edit + ")," + "\n";
                System.out.println(str);
                line = line + str;
            }
        }
        line = line.replaceAll("\"", "'");
        return line;
    }

    public static String CSVTTypesToString(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = "";
        String add = "";
        while (add != null) {
            String str = "";
            add = reader.readLine();
            if (add != null) {
                String[] arr = add.split(",");
                String edit = arr[0] + ",'" + arr[1] + "'";
                str = "(" + edit + ")," + "\n";
                System.out.println(str);
                line = line + str;
            }
        }
        line = line.replaceAll("\"", "'");
        return line;
    }

    public static String CSVtoString(String fileName) throws IOException {
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
                str = "(" + add + ")," + "\n";
            }
            System.out.println(str);
            line = line + str;
        }
        line = line.replaceAll("\"", "'");
        return line;
    }

    public static void routesCreatingSQL(String input, String routeTable, String filename) {
        String query = "INSERT INTO " + routeTable + " (id, `from`,`to`,euro_price, trip_duration, travel_data) " +
                "VALUES " + input;
        try (FileWriter file = new FileWriter(PATH + "/" + filename)) {
            file.write(query);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void locationsCreatingSQL(String input, String routeTable, String filename) {
        String query = "INSERT INTO " + routeTable + " (id, name, latitude, longitude) " +
                "VALUES " + input;
        try (FileWriter file = new FileWriter(PATH + "/" + filename)) {
            file.write(query);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void travelDataCreatingSQL(String input, String routeTable, String filename) {
        String query = "INSERT INTO " + routeTable + " (id, `from`, `to`, transportation_type, euro_price, " +
                "time_in_minutes) " +
                "VALUES " + input;
        try (FileWriter file = new FileWriter(PATH + "/" + filename)) {
            file.write(query);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void transportationTypesCreatingSQL(String input, String filename) {
        String query = "INSERT INTO transportation_types (id, name) VALUES " + input;
        try (FileWriter file = new FileWriter(PATH + "/" + filename)) {
            file.write(query);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
