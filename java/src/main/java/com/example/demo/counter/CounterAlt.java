package com.example.demo.counter;

import com.example.demo.Constants;
import com.example.demo.classes.DirectRoute;
import com.example.demo.classes.Location;
import com.example.demo.classes.Route;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CounterAlt {

    public static void main(String[] args) {
        //calculateRoutes(Constants.allowed_Transportation_Types_FixedRoutes, "fixed_routes");
        calculateRoutes(Constants.allowed_Transportation_Types_Routes, "routes");
        //calculateRoutes(Constants.allowed_Transportation_Types_FlyingRoutes, "flying_routes");
        //getAmount("110054,810055,770039,590005,850009,160005,180065,930277");
    }

    private static void calculateRoutes(String allowedTransportationTypes, String saveToTable) {
        String finalString = "";
        StringBuilder builder = new StringBuilder();
        String obj = null;
        int finalCount = 1;
        try {
            Connection conn = DriverManager.getConnection(Constants.DB_URL_ALT, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            System.out.println("DB is connected, " + conn);
            System.out.println("Started scanning routes");
            System.out.println("Getting locations");
            PreparedStatement statement = conn.prepareStatement("select * from locations");
            statement.execute();

            ResultSet locationsResultSet = statement.getResultSet();
            ArrayList<Location> locations = new ArrayList<>();

            SimpleDirectedWeightedGraph<Integer, DefaultEdge> routeGraph =
                    new SimpleDirectedWeightedGraph<>(DefaultEdge.class);
            while (locationsResultSet.next()) {
                int id = locationsResultSet.getInt("id");
                String name = locationsResultSet.getString("name");
                double latitude = locationsResultSet.getDouble("latitude");
                double longitude = locationsResultSet.getDouble("longitude");
                locations.add(new Location(id, name, latitude, longitude));
                routeGraph.addVertex(id);
            }
            System.out.println("getting data");
            statement = conn.prepareStatement("select `from`, `to`, euro_price from travel_data where " +
                    "transportation_type in " + allowedTransportationTypes)/*+ allowedTransportationTypes)*/;
            statement.execute();
            ResultSet travelDataResultSet = statement.getResultSet();
            System.out.println(travelDataResultSet);
            while (travelDataResultSet.next()) {
                int fromID = travelDataResultSet.getInt("from");
                int toID = travelDataResultSet.getInt("to");
                float price = travelDataResultSet.getFloat("euro_price");
                DefaultEdge e = routeGraph.getEdge(fromID, toID);
                if (e != null) {
                    System.out.println("Updating Price from: " + fromID + ", to: " + toID);
                    if (routeGraph.getEdgeWeight(e) > price) routeGraph.setEdgeWeight(e, price);
                } else {
                    if (fromID != toID) {
                        System.out.println("Adding to graph from: " + fromID + ", to: " + toID);
                        e = routeGraph.addEdge(fromID, toID);
                        routeGraph.setEdgeWeight(e, price);
                    }
                }
            }
            HashMap<Integer, Long> dataCounter = new HashMap<>();
            for (Location from : locations) {
                System.out.println("Scanning from: " + from);
                for (Location to : locations) {
                    if (to.getId() == from.getId()) continue;
                    System.out.println("--Scanning route from: " + from + " to: " + to);
                    GraphPath<Integer, DefaultEdge> path = DijkstraShortestPath.findPathBetween(routeGraph,
                            from.getId(), to.getId());
                    if (path == null) continue;
                    StringBuilder query = new StringBuilder("select * from travel_data where " +
                            "transportation_type in " + allowedTransportationTypes);
                    List<DefaultEdge> edgeList = path.getEdgeList();
                    System.out.println("edgelist size = " + edgeList.size());
                    if (edgeList == null || edgeList.size() == 0) continue;
                    for (int i = 0; i < edgeList.size(); i++) {
                        DefaultEdge edge = edgeList.get(i);
                        int edgeFrom = routeGraph.getEdgeSource(edge);
                        int edgeTo = routeGraph.getEdgeTarget(edge);
                        if (i != 0) query.append("OR ");
                        if (i == 0) {
                            query.append(" HAVING (`from` = ").append(edgeFrom).append(" and `to` = ").append(edgeTo).append(") ");
                        } else {
                            query.append(" (`from` = ").append(edgeFrom).append(" and `to` = ").append(edgeTo).append(") ");
                        }
                    }
                    query.append(" ORDER BY FIELD(`from`, ");
                    for (int i = 0; i < edgeList.size(); i++) {
                        int edgeFrom = routeGraph.getEdgeSource(edgeList.get(i));
                        if (i != 0) query.append(", ");
                        query.append(edgeFrom);
                    }
                    query.append(")");
                    System.out.println("----get direct routes query: " + query);
                    ArrayList<DirectRoute> directRoutes = new ArrayList<>();
                    statement = conn.prepareStatement(query.toString());
                    statement.execute();
                    ResultSet pathResultSet = statement.getResultSet();
                    float totalPrice = 0;
                    StringBuilder travelData = new StringBuilder();
                    int currentFromID = -1, currentToID = -1, bestTravelOptionID = -1;
                    float minPrice = -1;
                    while (pathResultSet.next()) {
                        int id = pathResultSet.getInt("id");
                        int fromID = pathResultSet.getInt("from");
                        int toID = pathResultSet.getInt("to");
                        float euroPrice = pathResultSet.getFloat("euro_price");
                        if (currentFromID == -1) {
                            currentFromID = fromID;
                            currentToID = toID;
                            minPrice = euroPrice;
                            bestTravelOptionID = id;
                        } else if (currentFromID == fromID) {
                            if (minPrice > euroPrice) {
                                minPrice = euroPrice;
                                bestTravelOptionID = id;
                            }
                        } else {
                            totalPrice += minPrice;
                            if (travelData.length() > 0)
                                travelData.append(",");
                            travelData.append(bestTravelOptionID);
                            DirectRoute directRoute = new DirectRoute(bestTravelOptionID, currentFromID, currentToID,
                                    minPrice);
                            directRoutes.add(directRoute);
                            System.out.println("----Travel: " + directRoutes);
                            currentFromID = fromID;
                            currentToID = toID;
                            minPrice = euroPrice;
                            bestTravelOptionID = id;
                        }
                    }
                    totalPrice += minPrice;
                    if (travelData.length() > 0)
                        travelData.append(",");
                    travelData.append(bestTravelOptionID);
                    if (!dataCounter.containsKey(bestTravelOptionID)) {
                        dataCounter.put(bestTravelOptionID, (long) 1);
                    } else {
                        long count = dataCounter.get(bestTravelOptionID);
                        count++;
                        dataCounter.put(bestTravelOptionID, count);
                    }
                    DirectRoute directRoute = new DirectRoute(bestTravelOptionID, currentFromID, currentToID, minPrice);
                    directRoutes.add(directRoute);
                    System.out.println("----Travel: " + directRoutes);
                    String[] arr = travelData.toString().split(",");
                    int duration = 0;
                    for (int i = 0; i < arr.length; i++) {
                        int id = Integer.parseInt(arr[i]);
                        int minutes = 0;
                        String q = "SELECT time_in_minutes FROM travel_data WHERE id = " + id;
                        System.out.println(q);
                        PreparedStatement statement2 = conn.prepareStatement(q);
                        statement2.execute();
                        ResultSet set = statement2.getResultSet();
                        while (set.next()) {
                            minutes = set.getInt("time_in_minutes");
                            System.out.println(minutes);
                        }
                        duration = duration + minutes;
                        System.out.println("duration - " + duration);
                    }
                    String queryo = "insert into " + saveToTable + " (`from`, `to`, euro_price," +
                            "trip_duration, travel_data) values (" +
                            from.getId() + ", " +
                            to.getId() + ", " +
                            totalPrice + ", " +
                            duration + ", '" +
                            travelData + "')";
                    System.out.println(queryo);
                    statement = conn.prepareStatement(queryo);
                    statement.execute();
//                    obj = "(" + finalCount + "," + from.getId() + "," + to.getId() + "," + totalPrice + "," +
//                    travelData + ")";
//                    builder.append(obj);
//                    builder.append(",");
//                    finalCount++;
                }
            }
//            finalString = builder.toString();
//            stringToFile(finalString);
//            System.out.println("map count");
//            for (Map.Entry entry : dataCounter.entrySet()) {
//                int id = (int) entry.getKey();
//                System.out.println(id);
//                long count = (long) entry.getValue();
//                System.out.println(count);
//                String query = "INSERT INTO travel_data_count_alternative (id, count) VALUES (" +
//                        id + "," + count + ")";
//                statement = conn.prepareStatement(query);
//                statement.execute();
//            }
            String createCounterTableQuery = "CREATE TABLE travel_data_additional_counter_" + saveToTable + " (id " +
                    "INT, count LONG);";
            statement = conn.prepareStatement(createCounterTableQuery);
            statement.execute();
            for (Map.Entry entry : dataCounter.entrySet()) {
                int id = (int) entry.getKey();
                long count = (long) entry.getValue();
                String query = "INSERT INTO travel_data_additional_counter_" + saveToTable + " (id, count) VALUES (" +
                        id + "," + count + ")";
                System.out.println(query);
                statement = conn.prepareStatement(query);
                statement.execute();
            }
            shortCounter(saveToTable, conn);
            commonCounter(saveToTable, conn);
            conn.close();

        } catch (SQLException e) {
            System.out.println("No connection with DB");
        }
    }

    public static void stringToFile(String routes) {
        try (FileWriter file = new FileWriter(Constants.PATH_ALT + Constants.STRING_FILE_ROUTES)) {
            file.write(routes);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getAmount(String s) {
        int duration = 0;
        String[] arr = s.split(",");
        try {
            Connection connection = DriverManager.getConnection(Constants.DB_URL_ALT, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            for (int i = 0; i < arr.length; i++) {
                int id = Integer.parseInt(arr[i]);
                int minutes = 0;
                String q = "SELECT time_in_minutes FROM travel_data WHERE id = " + id;
                System.out.println(q);
                PreparedStatement statement = connection.prepareStatement(q);
                statement.execute();
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    minutes = set.getInt("time_in_minutes");
                    System.out.println(minutes);
                }
                duration = duration + minutes;
            }
        } catch (SQLException e) {
            System.out.println("problem getAmount");
        }
        System.out.println("duration - " + duration);
    }

    public static void commonCounter(String saveToTable, Connection connection) {
        String query = "CREATE TABLE travel_data_counter_" + saveToTable + " AS " +
                "SELECT travel_data.id,`from`,`to`,transportation_type,euro_price,time_in_minutes,"
                + " count_finish_" + saveToTable + ".count FROM travel_data JOIN count_finish_" + saveToTable +
                " ON travel_data.id = count_finish_" + saveToTable + ".id";
        System.out.println(query);
        String query2 = "DROP TABLE count_finish_" + saveToTable;
        String query3 = "DROP TABLE travel_data_only_direct_" + saveToTable;
        String query4 = "DROP TABLE travel_data_additional_counter_" + saveToTable;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            PreparedStatement statement1 = connection.prepareStatement(query2);
            statement1.execute();
            PreparedStatement statement2 = connection.prepareStatement(query3);
            statement2.execute();
            PreparedStatement statement3 = connection.prepareStatement(query4);
            statement3.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Counter of " + saveToTable + " created");
    }

    public static void shortCounter(String saveToTable, Connection connection) {
        HashMap<Integer, Long> map = new HashMap<>();
        try {
            String query = "SELECT travel_data from " + saveToTable;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet routesSet = statement.getResultSet();
            while (routesSet.next()) {
                String route = routesSet.getString("travel_data");
                String[] datas = route.split(",");
                if (datas.length == 1) {
                    int id = Integer.parseInt(datas[0]);
                    if (!map.containsKey(id)) {
                        map.put(id, (long) 1);
                    } else {
                        long count = map.get(id);
                        count++;
                        map.put(id, count);
                    }
                }
            }
            String query2 = "CREATE TABLE travel_data_only_direct_" + saveToTable + " (id INT, count LONG);";
            statement = connection.prepareStatement(query2);
            statement.execute(query2);
            System.out.println("travel_data_only_direct - created");
            for (Map.Entry entry : map.entrySet()) {
                int id = (int) entry.getKey();
                long count = (long) entry.getValue();
                String query1 = "INSERT INTO travel_data_only_direct_" + saveToTable + " (id, count) VALUES (" +
                        id + "," + count + ")";
                statement = connection.prepareStatement(query1);
                statement.execute();
            }

            String query3 = "CREATE TABLE count_finish_" + saveToTable +
                    " AS SELECT travel_data_additional_counter_" + saveToTable + ".id," +
                    "(travel_data_additional_counter_" + saveToTable + ".count-travel_data_only_direct_" + saveToTable + ".count) AS count" +
                    " FROM travel_data_additional_counter_" + saveToTable + " JOIN travel_data_only_direct_" + saveToTable +
                    " on travel_data_additional_counter_" + saveToTable + ".id = travel_data_only_direct_" + saveToTable + ".id;";

            System.out.println(query3);
            statement = connection.prepareStatement(query3);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("shortCounter: problem");
        }
    }
}

