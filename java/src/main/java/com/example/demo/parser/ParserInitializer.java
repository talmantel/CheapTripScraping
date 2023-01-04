package com.example.demo.parser;

import com.example.demo.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ParserInitializer {
    final static String COMMON_PATH = Constants.COMMON_PATH;
    final static String OUTCOME_PATH = Constants.OUTCOME_PATH;
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

//        System.out.println(Arrays.toString(CSVtoJson.CSVoString(COMMON_PATH + Constants.TRAVEL_DATA_NEW)));

        //id changer
//        System.out.println(CSVtoJson.idChanger(CSVtoJson.CSVoString(Constants.PATH_ALT + Constants.STRING_FILE_TRANSFORM)));
        CSVtoJson.stringToFile(CSVtoJson.idChanger(CSVtoJson.CSVoString(Constants.PATH_ALT + "new_routesah.csv")), Constants.PATH_ALT + "PPP.csv");

//        CSVtoJson.jsonToFile(CSVtoJson.travelDataToJson(
//                CSVtoJson.CSVoString(COMMON_PATH + Constants.TRAVEL_DATA_NEW)
//        ), OUTCOME_PATH + "new_travel_data.json");

         // Parse countries to Json
//        CSVtoJson.jsonToFile(CSVtoJson.countriesToJson(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_countries.csv")),OUTCOME_PATH + "cheap_trip_countries.json");

        // Parse currencies to Json
//        CSVtoJson.jsonToFile(CSVtoJson.currenciesToJson(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_currencies.csv")),OUTCOME_PATH + "cheap_trip_currencies.json");

        // Parse fixed route to Json
//        CSVtoJson.jsonToFile(CSVtoJson.routesToJsonAlt(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_fixed_routes.csv")),OUTCOME_PATH + "cheap_trip_fixed_routes.json");

        // Parse fixed route without ride
//        CSVtoJson.jsonToFile(CSVtoJson.routesToJsonAlt(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_fixed_routes_without_ride_share.csv")),OUTCOME_PATH + "cheap_trip_fixed_routes_without_ride_share.json");

        // Parse flying routes
//        CSVtoJson.jsonToFile(CSVtoJson.routesToJsonAlt(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_flying_routes.csv")),OUTCOME_PATH + "cheap_trip_flying_routes.json");

        // Parse locations
//        CSVtoJson.jsonToFile(CSVtoJson.locationsToJson(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_locations.csv")),OUTCOME_PATH + "cheap_trip_locations.json");

        // Parse routes
//        CSVtoJson.jsonToFile(CSVtoJson.routesToJsonAlt(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_routes.csv")),OUTCOME_PATH + "cheap_trip_routes.json");

        // Parse routes without ride
//        CSVtoJson.jsonToFile(CSVtoJson.routesToJsonAlt(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_routes_without_ride_share.csv")),OUTCOME_PATH + "cheap_trip_routes_without_ride_share.json");

        // Parse transportation type
//        CSVtoJson.jsonToFile(CSVtoJson.transportationTypesToJson(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_transportation_types.csv")),OUTCOME_PATH + "cheap_trip_transportation_types.json");

        // Parse travel data
//        CSVtoJson.jsonToFile(CSVtoJson.travelDataToJson(
//                CSVtoJson.CSVoString(COMMON_PATH + "cheap_trip_travel_data.csv")),OUTCOME_PATH + "cheap_trip_travel_data.json");
    }
}