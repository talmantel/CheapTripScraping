package org.example.functional.counters;

import org.example.CounterMenuTest;
import org.example.classes.DirectRoute;
import org.example.classes.Location;
import org.example.visual.Console;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counters {

    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    public static void calculateRoutes(Connection connection,
                                       String allowedTransportationTypes,
                                       String saveToTable,
                                       boolean stringSave,
                                       boolean databaseSave,
                                       String fileAddress) {
        String finalString = "";
        StringBuilder builder = new StringBuilder();
        String obj = null;
        int finalCount = 1;
        try {
            System.out.println("Counting of " + saveToTable + " in progress");
            System.out.println("DB is connected, " + connection);
            System.out.println("Started scanning routes");
            System.out.println("Getting locations");
            PreparedStatement statement = connection.prepareStatement("select * from locations");
            statement.execute();

            ResultSet locationsResultSet = statement.getResultSet();
            ArrayList<Location> locations = new ArrayList<>();

            SimpleDirectedWeightedGraph<Integer, DefaultEdge> routeGraph = new SimpleDirectedWeightedGraph<>(DefaultEdge.class);
            while (locationsResultSet.next()) {
                int id = locationsResultSet.getInt("id");
                String name = locationsResultSet.getString("name");
                int country_id = locationsResultSet.getInt("country_id");
                double latitude = locationsResultSet.getDouble("latitude");
                double longitude = locationsResultSet.getDouble("longitude");
                String name_ru = locationsResultSet.getString("name_ru");
                locations.add(new Location(id, name, country_id, latitude, longitude, name_ru));
                routeGraph.addVertex(id);
            }
            System.out.println("getting data");
            statement = connection.prepareStatement("select `from`, `to`, euro_price from travel_data where " +
                    "transportation_type in " + allowedTransportationTypes);
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
                    GraphPath<Integer, DefaultEdge> path = DijkstraShortestPath.findPathBetween(routeGraph, from.getId(), to.getId());
                    if (path == null) continue;
                    StringBuilder query = new StringBuilder("select * from travel_data where transportation_type in " + allowedTransportationTypes);
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
                    statement = connection.prepareStatement(query.toString());
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
                            DirectRoute directRoute = new DirectRoute(bestTravelOptionID, currentFromID, currentToID, minPrice);
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
                    if (stringSave) {
                        obj = "(" + finalCount + "," + from.getId() + "," + to.getId() + "," + totalPrice + "," + travelData + ")";
                        builder.append(obj);
                        builder.append(",");
                        finalCount++;
                    }
                    if (databaseSave) {
                        statement = connection.prepareStatement("insert into " + saveToTable + "(`from`, `to`, " +
                                "euro_price, travel_data) values (" +
                                from.getId() + ", " +
                                to.getId() + ", " +
                                totalPrice + ", '" +
                                travelData + "')");
                        statement.execute();
                    }
                }
            }
            if (stringSave) {
                finalString = builder.toString();
                stringToFile(finalString, fileAddress);
            }
            String createCounterTableQuery = "CREATE TABLE travel_data_additional_counter_" + saveToTable + " (id INT, count " +
                    "LONG);";
            statement = connection.prepareStatement(createCounterTableQuery);
            statement.execute();
            for (Map.Entry entry : dataCounter.entrySet()) {
                int id = (int) entry.getKey();
                long count = (long) entry.getValue();
                String query = "INSERT INTO travel_data_additional_counter_" + saveToTable + " (id, count) VALUES (" +
                        id + "," + count + ")";
                statement = connection.prepareStatement(query);
                statement.execute();
            }
            shortCounter(saveToTable,connection);
            commonCounter(saveToTable,connection);
            connection.close();

        } catch (SQLException e) {
            stream.println("Counter: connection lost");
        }
    }

    public static void stringToFile(String routes, String fileAddress) {
        try (FileWriter file = new FileWriter(fileAddress)) {
            file.write(routes);
            file.flush();
        } catch (IOException e) {
            stream.println("stringToFile: problem");
        }
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
       stream.println("Counter of " + saveToTable + " created");
    }

    public static void shortCounter(String saveToTable, Connection connection) {
        HashMap <Integer, Long> map = new HashMap<>();
        try {
            String query = "SELECT travel_data from " + saveToTable;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet routesSet = statement.getResultSet();
            while (routesSet.next()) {
                String route = routesSet.getString("travel_data");
                String [] datas = route.split(",");
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
                    "(travel_data_additional_counter_" + saveToTable +".count-travel_data_only_direct_" + saveToTable + ".count) AS count" +
                    " FROM travel_data_additional_counter_" + saveToTable + " JOIN travel_data_only_direct_"  + saveToTable +
                    " on travel_data_additional_counter_" + saveToTable + ".id = travel_data_only_direct_"  + saveToTable + ".id;";

            System.out.println(query3);
            statement = connection.prepareStatement(query3);
            statement.execute();
        } catch (SQLException e){
            stream.println("shortCounter: connection lost");
        }
    }
}