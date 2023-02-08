package com.example.demo.short_counter;

import com.example.demo.Constants;
import com.example.demo.classes.Location;
import com.example.demo.classes.TravelData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class fromCSVtoClasses {


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
            System.out.println(str);
            line = line + str;
        }

        System.out.println(line);
        String[] lines = line.split("\\),\\(");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("[(')]", "");
            lines[i] = lines[i].replaceAll("null", "");
        }
        return lines;
    }

    public static ArrayList<TravelData> arrayToList(String[] array) {
        ArrayList<TravelData> data = new ArrayList<>();
        int k = array.length;
        for (int i = 0; i < k; i++) {
            String[] arr = array[i].split(",");
            TravelData travelData = new TravelData(
                    Integer.parseInt(arr[0]),
                    Integer.parseInt(arr[1]),
                    Integer.parseInt(arr[2]),
                    Integer.parseInt(arr[3]),
                    Float.parseFloat(arr[4]),
                    Integer.parseInt(arr[5])
            );
            data.add(travelData);
        }
        return data;
    }

    public static ArrayList<TravelData> listShorter(ArrayList<TravelData> list) {
        list.sort(new Comparator<TravelData>() {
            @Override
            public int compare(TravelData o1, TravelData o2) {
                return Comparator.comparing(TravelData::getFrom)
                        .thenComparing(TravelData::getTo)
                        .thenComparing(TravelData::getTransportation_type)
                        .thenComparing(TravelData::getEuro_price).compare(o1, o2);
            }
        });
        int count = 0;
        while (count < list.size() - 1) {
            if (list.get(count).getFrom() == list.get(count + 1).getFrom() &&
                    list.get(count).getTo() == list.get(count + 1).getTo() &&
                    list.get(count).getTransportation_type() == list.get(count + 1).getTransportation_type()) {
                list.remove(count + 1);
            } else {
                count++;
            }
        }
        return list;
    }

    public static void travelDataListToDb(ArrayList<TravelData> list) {
        try {
            Connection connection = DriverManager.getConnection(Constants.DB_URL_ALT,
                    Constants.DB_USER,
                    Constants.DB_PASSWORD);
            for (int i = 0; i < list.size(); i++) {
                String query = "INSERT INTO travel_data (id,`from`, `to`, transportation_type, euro_price, " +
                        "time_in_minutes) VALUES (" +
                        list.get(i).getId() + "," +
                        list.get(i).getFrom() + "," +
                        list.get(i).getTo() + "," +
                        list.get(i).getTransportation_type() + "," +
                        list.get(i).getEuro_price() + "," +
                        list.get(i).getTime_in_minutes() + ");";
                System.out.println(query);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
        } catch (SQLException e) {
            System.out.println("travelDataListToDb: problem with DB");
        }
    }

    public static ArrayList<Location> stringsToLocations(String[] input) {
        int k = input.length;
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            String[] arr = input[i].split(",");
            Location location = new Location(
                    Integer.parseInt(arr[0]),
                    arr[1],
                    Double.parseDouble(arr[2]),
                    Double.parseDouble(arr[3])
            );
            locations.add(location);
        }
        return locations;
    }

    public static void locationsToDb(ArrayList<Location> list) {
        try {
            Connection connection = DriverManager.getConnection(Constants.DB_URL_ALT,
                    Constants.DB_USER,
                    Constants.DB_PASSWORD);
            for (int i = 0; i < list.size(); i++) {
                String query = "INSERT INTO locations (id, name, latitude, longitude) VALUES (" +
                        list.get(i).getId() + ",'" +
                        list.get(i).getName() + "'," +
                        list.get(i).getLatitude() + "," +
                        list.get(i).getLongitude() + ");";
                System.out.println(query);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            }
        } catch (SQLException e) {
            System.out.println("travelDataListToDb: problem with DB");
        }
    }


    public static String dataToString(TravelData data) {
        return data.getFrom() + " " +
                data.getTo() + " " +
                data.getTransportation_type() + " " +
                data.getEuro_price() + " " +
                data.getTime_in_minutes();
    }
}
