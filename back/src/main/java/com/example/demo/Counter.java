package com.example.demo;

import com.example.demo.classes.DirectRoute;
import com.example.demo.classes.Location;
import com.example.demo.classes.Route;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Counter {

    public static void main(String[] args) {
        calculateRoutes(Constants.allowed_Transportation_Types_FixedRoutes,Constants.fixedRoutesDb);
        calculateRoutes(Constants.allowed_Transportation_Types_Routes,Constants.routesDb);
        calculateRoutes(Constants.allowed_Transportation_Types_FlyingRoutes,Constants.flyingRoutesDb);
    }

    private static void calculateRoutes(String allowedTransportationTypes, String saveToTable) {

        try {
            Connection conn = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD);
            System.out.println("DB is connected, " + conn);
            System.out.println("Started scanning routes");
            System.out.println("Getting locations");
            PreparedStatement statement = conn.prepareStatement("select * from locations");
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
                locations.add(new Location(id, name, country_id,latitude,longitude,name_ru));
                routeGraph.addVertex(id);
            }
            //System.out.println(routeGraph.vertexSet());
            System.out.println("getting data");
            statement = conn.prepareStatement("select `from`, `to`, euro_price from travel_data where transportation_type in " + allowedTransportationTypes )/*+ allowedTransportationTypes)*/;
            statement.execute();
            ResultSet travelDataResultSet = statement.getResultSet();
            System.out.println(travelDataResultSet);
            while (travelDataResultSet.next()) {
                int fromID = travelDataResultSet.getInt("from");
                int toID = travelDataResultSet.getInt("to");
                float price = travelDataResultSet.getFloat("euro_price");
                DefaultEdge e = routeGraph.getEdge(fromID, toID);
                if(e != null) {
                    System.out.println("Updating Price from: " + fromID + ", to: " + toID);
                    if(routeGraph.getEdgeWeight(e) > price) routeGraph.setEdgeWeight(e, price);
                } else {
                    System.out.println("Adding to graph from: " + fromID + ", to: " + toID);
                    e = routeGraph.addEdge(fromID, toID);
                    routeGraph.setEdgeWeight(e, price);
                }
            }
            for(Location from : locations){
                System.out.println("Scanning from: " + from);
                for(Location to : locations){
                    if(to.getId() == from.getId()) continue;
                    System.out.println("--Scanning route from: " + from + " to: " + to);
                    GraphPath<Integer, DefaultEdge> path = DijkstraShortestPath.findPathBetween(routeGraph, from.getId(), to.getId());
                    StringBuilder query = new StringBuilder("select * from travel_data where transportation_type in " + allowedTransportationTypes);
                    if(path == null) continue;
                    List<DefaultEdge> edgeList = path.getEdgeList();
                    System.out.println("edgelist size = " + edgeList.size());
                    if(edgeList == null || edgeList.size() == 0) continue;
                    for(int i = 0; i < edgeList.size(); i++){
                        DefaultEdge edge = edgeList.get(i);
                        int edgeFrom = routeGraph.getEdgeSource(edge);
                        int edgeTo = routeGraph.getEdgeTarget(edge);
                        if(i != 0) query.append("OR "); query.append(" HAVING (`from` = ").append(edgeFrom).append(" and `to` = ").append(edgeTo).append(") ");
                    } query.append(" ORDER BY FIELD(`from`, ");
                    for(int i = 0; i < edgeList.size(); i++){
                        int edgeFrom = routeGraph.getEdgeSource(edgeList.get(i));
                        if(i != 0) query.append(", ");
                        query.append(edgeFrom);
                    } query.append(")");
                    System.out.println("----get direct routes query: " + query);
                    ArrayList<DirectRoute> directRoutes = new ArrayList<>();
                    statement = conn.prepareStatement(query.toString()); statement.execute();
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
            if(currentFromID == -1){
                currentFromID = fromID;
                currentToID = toID;
                minPrice = euroPrice;
                bestTravelOptionID = id;
            } else if(currentFromID == fromID){
                if(minPrice > euroPrice){
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
        DirectRoute directRoute = new DirectRoute(bestTravelOptionID, currentFromID, currentToID, minPrice);
        directRoutes.add(directRoute);
        System.out.println("----Travel: " + directRoutes);

        statement = conn.prepareStatement("insert into " + saveToTable +" (`from`, `to`, euro_price, travel_data) values (" +
                from.getId() + ", " +
                to.getId() + ", " +
                totalPrice + ", '" +
                travelData + "')");
        statement.execute();
                }
            } conn.close();
        } catch (SQLException e) {
            System.out.println("No connection with DB");
        }
    }
}
