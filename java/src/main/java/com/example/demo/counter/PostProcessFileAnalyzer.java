package com.example.demo.counter;

import com.example.demo.Constants;
import com.example.demo.parser.CSVtoJson;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PostProcessFileAnalyzer {
    public static void main(String[] args) throws IOException {
        pushCountIntoDB(travelDataCounterFromFile(Constants.COMMON_PATH + "routes.csv"));
    }

    public static HashMap<Integer, Long> travelDataCounterFromFile(String filename) throws IOException {
        String[] input = CSVtoJson.CSVoString(filename);
        HashMap<Integer, Long> map = new HashMap<>();
        int k = input.length;
        for (int i = 0; i < k; i++) {
            String[] arr = input[i].split(",");
            for (int j = 4; j < arr.length; j++) {
                if (!map.containsKey(Integer.parseInt(arr[j]))) {
                    map.put(Integer.parseInt(arr[j]), (long) 1);
                } else {
                    long number = map.get(Integer.parseInt(arr[j]));
                    map.put(Integer.parseInt(arr[j]), number + 1);
                }
            }
        }
        return map;
    }

    public static void pushCountIntoDB(HashMap<Integer, Long> map) {
        try {
            Connection connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD);
            for (Map.Entry entry : map.entrySet()) {
                int id = (int) entry.getKey();
                String query = "SELECT * FROM travel_data_count WHERE id = " + id;
                PreparedStatement statement1 = connection.prepareStatement(query);
                statement1.execute();
                ResultSet set = statement1.getResultSet();
                int countId = 0;
                long count = 0;
                while (set.next()) {
                    countId = set.getInt("id");
                    count = set.getLong("count");
                }
                if (countId == 0) {
                    PreparedStatement statement2 = connection.prepareStatement(
                            "INSERT INTO travel_data_count (id, count) VALUES (" + entry.getKey() +
                                    "," + entry.getValue() + ")");
                    statement2.execute();
                } else {
                    long sum = count + (long) entry.getValue();
                    String query3 = "UPDATE travel_data_count SET count = " + (sum) + " WHERE id = " + id;
                    PreparedStatement statement3 = connection.prepareStatement(query3);
                    statement3.execute();
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Problem with the connection with DB");
        }
    }
}
