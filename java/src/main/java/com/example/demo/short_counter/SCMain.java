package com.example.demo.short_counter;

import com.example.demo.classes.Location;
import com.example.demo.classes.TravelData;

import java.io.IOException;
import java.util.ArrayList;

public class SCMain {
    public static void main(String[] args) throws IOException {

        ArrayList<TravelData> list = fromCSVtoClasses.arrayToList(fromCSVtoClasses.CSVoString
                ("K:/Programming/Graphs" +
                        "(from_Roman)" +
                        "/CheapTripScraping1/java/start_pack/csv/travel_data.csv"));
        ArrayList<TravelData> list2 = fromCSVtoClasses.listShorter(list);
        fromCSVtoClasses.travelDataListToDb(list2);

//        ArrayList<Location> locationList = fromCSVtoClasses.stringsToLocations(fromCSVtoClasses.CSVoString("K" +
//                ":/Programming" +
//                "/Graphs" +
//                "(from_Roman)" +
//                "/CheapTripScraping1/java/empty_res/locations.csv"));
//        fromCSVtoClasses.locationsToDb(locationList);
    }
}
