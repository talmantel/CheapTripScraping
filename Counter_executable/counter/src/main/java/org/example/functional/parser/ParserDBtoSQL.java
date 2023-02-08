package org.example.functional.parser;

import org.example.CounterMenuTest;
import org.example.classes.*;
import org.example.visual.Console;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParserDBtoSQL {

    public static void main(String[] args) {

    }

    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    public static Connection connection;

    public static String countriesToString (Connection connection) {
        List<Country> countries = new ArrayList<>();
        try {
            String query = "SELECT * FROM countries";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet countriesSet = statement.getResultSet();
            while (countriesSet.next()) {
                String country_name = countriesSet.getString("country_name");
                int country_id = countriesSet.getInt("country_id");
                String country_name_ru = countriesSet.getString("country_name_ru");
                Country country = new Country(country_name,country_id,country_name_ru);
                countries.add(country);
            }
        } catch (SQLException e) {
            stream.println("countriesToString: Problem with the connection");
        }
        if (countries.isEmpty()) {
            stream.println("countriesToString: table 'countries' is empty");
            return null;
        }
        String result = "INSERT INTO countries (country_name, country_id, country_name_ru) VALUES ";
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            result = result + "('" + country.getCountry_name() + "'," +
                    country.getCountry_id() + ",'" +
                    country.getCountry_name_ru() + "')";
            if (i < countries.size()-1) {
                result = result + ",\n";
            } else {
                result = result + ";\n";
            }
        }
        return result;
    }

    public static void countriesSQL (String input, String path) {
        if (input == null) {
            stream.println("countriesSQL: table 'countries' is empty");
        } else {
            String result = "DROP TABLE IF EXISTS countries;" + "\n" +
                    "CREATE TABLE countries " + "\n" +
                    "(" + "\n" +
                    "   country_name        VARCHAR(60)         NOT NULL," + "\n" +
                    "   country_id          INT                 NOT NULL," + "\n" +
                    "   country_name_ru     VARCHAR(60)         NOT NULL," + "\n" +
                    "PRIMARY KEY (country_id)" + "\n" +
                    ") ENGINE = InnoDB" + "\n" +
                    "DEFAULT CHARSET = utf8;" + "\n" +
                    "LOCK TABLES countries WRITE;" + "\n" +
                    input + "\n" +
                    "UNLOCK TABLES;";
            sqlToFile(result,path + "/countries.sql");
        }
    }

    public static String currenciesToString (Connection connection) {
        List<Currency> currencies = new ArrayList<>();
        try {
            String query = "SELECT * FROM currencies";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet currenciesSet = statement.getResultSet();
            while (currenciesSet.next()) {
                int id = currenciesSet.getInt("id");
                String name = currenciesSet.getString("name");
                String code = currenciesSet.getString("code");
                String symbol = currenciesSet.getString("symbol");
                float one_euro_rate = currenciesSet.getFloat("one_euro_rate");
                String rtr_symbol = currenciesSet.getString("rtr_symbol");
                Currency currency = new Currency(id,name,code,symbol,one_euro_rate,rtr_symbol);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            stream.println("currenciesToString: Problem with the connection");
        }
        if (currencies.isEmpty()) {
            stream.println("currenciesToString: table 'currencies' is empty");
            return null;
        }
        String result = "INSERT INTO currencies (id, name, code, symbol, one_euro_rate, rtr_symbol) VALUES ";
        for (int i = 0; i < currencies.size(); i++) {
            Currency currency = currencies.get(i);
            result = result + "(" + currency.getId() + ",'" +
                    currency.getName() + "','" +
                    currency.getCode() + "','" +
                    currency.getSymbol() + "'," +
                    currency.getOne_euro_rate() + ",'" +
                    currency.getRtr_symbol() + "')";
            if (i < currencies.size()-1) {
                result = result + ",\n";
            } else {
                result = result + ";\n";
            }
        }
        return result;
    }

    public static void currenciesSQL (String input, String path) {
        if (input == null) {
            stream.println("currenciesSQL: table 'currencies' is empty");
        } else {
            String result = "DROP TABLE IF EXISTS currencies;" + "\n" +
                    "CREATE TABLE currencies " + "\n" +
                    "(" + "\n" +
                    "   id                  INT                 NOT NULL," + "\n" +
                    "   name                VARCHAR(40)         NOT NULL," + "\n" +
                    "   code                VARCHAR(20)         NOT NULL," + "\n" +
                    "   symbol              VARCHAR(10)         NOT NULL," + "\n" +
                    "   one_euro_rate       DECIMAL(12, 2)      NOT NULL," + "\n" +
                    "   rtr_symbol          VARCHAR(30)         NOT NULL," + "\n" +
                    "PRIMARY KEY (id)" + "\n" +
                    ") ENGINE = InnoDB" + "\n" +
                    "DEFAULT CHARSET = utf8;" + "\n" +
                    "LOCK TABLES currencies WRITE;" + "\n" +
                    input + "\n" +
                    "UNLOCK TABLES;";
            sqlToFile(result, path + "/currencies.sql");
        }
    }

    public static String locationsToString(Connection connection) {
        List<Location> locations = new ArrayList<>();
        try {
            String query = "SELECT * FROM locations";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet locationsSet = statement.getResultSet();
            while (locationsSet.next()) {
                int id = locationsSet.getInt("id");
                String name = locationsSet.getString("name");
                int country_id = locationsSet.getInt("country_id");
                double latitude = locationsSet.getDouble("latitude");
                double longitude = locationsSet.getDouble("longitude");
                String name_ru = locationsSet.getString("name_ru");
                Location location = new Location(id,name,country_id,latitude,longitude,name_ru);
                locations.add(location);
            }
        } catch (SQLException e) {
            stream.println("locationsToString: Problem with the connection");
        }
        if (locations.isEmpty()) {
            stream.println("locationsToString: table 'locations' is empty");
            return null;
        }
        String result = "INSERT INTO locations (id, name, country_id, latitude, longitude,name_ru) VALUES ";
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            result = result + "(" + location.getId() + ",'" +
                    location.getName() + "','" +
                    location.getCountry_id() + "','" +
                    location.getLatitude() + "'," +
                    location.getLongitude() + ",'" +
                    location.getName_ru() + "')";
            if (i < locations.size()-1) {
                result = result + ",\n";
            } else {
                result = result + ";\n";
            }
        }
        return result;
    }

    public static void locationsSQL (String input, String path) {
        if (input == null) {
            stream.println("locationsSQL: table 'locations' is empty");
        } else {
            String result = "DROP TABLE IF EXISTS locations;" + "\n" +
                    "CREATE TABLE locations " + "\n" +
                    "(" + "\n" +
                    "   id                  INT                 NOT NULL," + "\n" +
                    "   name                VARCHAR(40)         NOT NULL," + "\n" +
                    "   country_id          INT                 NOT NULL," + "\n" +
                    "   latitude            DECIMAL(12, 2)      NOT NULL," + "\n" +
                    "   longitude           DECIMAL(12, 2)      NOT NULL," + "\n" +
                    "   name_ru             VARCHAR(60)         NOT NULL," + "\n" +
                    "PRIMARY KEY (id)" + "\n" +
                    ") ENGINE = InnoDB" + "\n" +
                    "DEFAULT CHARSET = utf8;" + "\n" +
                    "LOCK TABLES currencies WRITE;" + "\n" +
                    input + "\n" +
                    "UNLOCK TABLES;";
            sqlToFile(result, path + "/locations.sql");
        }
    }

    public static String transportationTypesToString(Connection connection){
        List<TransportationType> types = new ArrayList<>();
        try {
            String query = "SELECT * FROM transportation_types";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet typesSet = statement.getResultSet();
            while (typesSet.next()) {
                int id = typesSet.getInt("id");
                String name = typesSet.getString("name");
                TransportationType type = new TransportationType(id,name);
                types.add(type);
            }
        } catch (SQLException e) {
            stream.println("transportationTypesToString: Problem with the connection");
        }
        if (types.isEmpty()) {
            stream.println("transportationTypesToString: table 'transportation_types' is empty");
            return null;
        }
        String result = "INSERT INTO transportation_types (id, name) VALUES ";
        for (int i = 0; i < types.size(); i++) {
            TransportationType type = types.get(i);
            result = result + "(" + type.getId() + ",'" +
                    type.getName() + "')";
            if (i < types.size()-1) {
                result = result + ",\n";
            } else {
                result = result + ";\n";
            }
        }
        return result;
    }

    public static void transportationTypesSQL (String input, String path) {
        if (input == null) {
            stream.println("transportationTypesSQL: table 'transportation_types' is empty");
        } else {
            String result = "DROP TABLE IF EXISTS transportation_types;" + "\n" +
                    "CREATE TABLE transportation_types " + "\n" +
                    "(" + "\n" +
                    "   id                  INT                 NOT NULL," + "\n" +
                    "   name                VARCHAR(30)         NOT NULL," + "\n" +
                    "PRIMARY KEY (id)" + "\n" +
                    ") ENGINE = InnoDB" + "\n" +
                    "DEFAULT CHARSET = utf8;" + "\n" +
                    "LOCK TABLES currencies WRITE;" + "\n" +
                    input + "\n" +
                    "UNLOCK TABLES;";
            sqlToFile(result, path + "/transportation_types.sql");
        }
    }

    public static String routesToString(Connection connection, String routeTable){
        List<Route> routes = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + routeTable;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet routesSet = statement.getResultSet();
            while (routesSet.next()) {
                int id = routesSet.getInt("id");
                int from = routesSet.getInt("from");
                int to = routesSet.getInt("to");
                float euro_price = routesSet.getFloat("euro_price");
                int trip_duration = routesSet.getInt("trip_duration");
                String travel_data = routesSet.getString("travel_data");
                Route route = new Route(id,from,to,euro_price,trip_duration,travel_data);
                routes.add(route);
            }
        } catch (SQLException e) {
            stream.println("routesToString: Problem with the connection");
        }
        if (routes.isEmpty()) {
            stream.println("routesToString: table '" + routeTable + "' is empty");
            return null;
        }
        String result = "INSERT INTO " + routeTable + " (id, `from`,`to`, euro_price, trip_duration, travel_data) " +
                "VALUES ";
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            result = result + "(" + route.getId() + "," +
                    route.getFrom() + "," +
                    route.getTo() + "," +
                    route.getEuro_price() + "," +
                    route.getTrip_duration() + ",'" +
                    route.getTravel_data() + "')";
            if (i < routes.size()-1) {
                result = result + ",\n";
            } else {
                result = result + ";\n";
            }
        }
        return result;
    }

    public static void routesSQL (String input, String routeTable, String path) {
        if (input == null) {
            stream.println("routesSQL: table '" + routeTable + "' is empty");
        } else {
            String result = "DROP TABLE IF EXISTS " + routeTable + ";" + "\n" +
                    "CREATE TABLE " + routeTable + "\n" +
                    "(" + "\n" +
                    "   id                  INT                 NOT NULL," + "\n" +
                    "   `from`              INT                 NOT NULL," + "\n" +
                    "   `to`                INT                 NOT NULL," + "\n" +
                    "   euro_price          FLOAT               NOT NULL," + "\n" +
                    "   trip_duration       INT                 NOT NULL," + "\n" +
                    "   travel_data         VARCHAR(255)        NOT NULL," + "\n" +
                    "PRIMARY KEY (id)" + "\n" +
                    ") ENGINE = InnoDB" + "\n" +
                    "DEFAULT CHARSET = utf8;" + "\n" +
                    "LOCK TABLES currencies WRITE;" + "\n" +
                    input + "\n" +
                    "UNLOCK TABLES;";
            sqlToFile(result, path + "/" + routeTable + ".sql");
        }
    }

    public static String travelDataToString (Connection connection){
        List<TravelData> datas = new ArrayList<>();
        try {
            String query = "SELECT * FROM travel_data";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet dataSet = statement.getResultSet();
            while (dataSet.next()) {
                int id = dataSet.getInt("id");
                int from = dataSet.getInt("from");
                int to = dataSet.getInt("to");
                int transportation_type = dataSet.getInt("transportation_type");
                float euro_price = dataSet.getFloat("euro_price");
                int time_in_minutes = dataSet.getInt("time_in_minutes");
                TravelData data = new TravelData(id,from,to,transportation_type,euro_price,time_in_minutes);
                datas.add(data);
            }
        } catch (SQLException e) {
            stream.println("travelDataToString: Problem with the connection");
        }
        if (datas.isEmpty()) {
            stream.println("travelDataToString: table 'travel_data' is empty");
            return null;
        }
        String result = "INSERT INTO travel_data (id, `from`,`to`, transportation_type, euro_price, time_in_minutes) " +
                "VALUES ";
        for (int i = 0; i < datas.size(); i++) {
            TravelData data = datas.get(i);
            result = result + "(" + data.getId() + "," +
                    data.getFrom() + "," +
                    data.getTo() + "," +
                    data.getTransportation_type() + "," +
                    data.getEuro_price() + "," +
                    data.getTime_in_minutes() + ")";
            if (i < datas.size()-1) {
                result = result + ",\n";
            } else {
                result = result + ";\n";
            }
        }
        return result;
    }

    public static void travelDataSQL (String input, String path) {
        String result = "DROP TABLE IF EXISTS travel_data;" + "\n" +
                "CREATE TABLE travel_data"  + "\n" +
                "(" + "\n" +
                "   id                  INT                 NOT NULL," + "\n" +
                "   `from`              INT                 NOT NULL," + "\n" +
                "   `to`                INT                 NOT NULL," + "\n" +
                "   transportation_type INT                 NOT NULL," + "\n" +
                "   euro_price          FLOAT               NOT NULL," + "\n" +
                "   time_in_minutes     INT                 NOT NULL," + "\n" +
                "PRIMARY KEY (id)" + "\n" +
                ") ENGINE = InnoDB" + "\n" +
                "DEFAULT CHARSET = utf8;" + "\n" +
                "LOCK TABLES currencies WRITE;" + "\n" +
                input + "\n" +
                "UNLOCK TABLES;";
        sqlToFile(result,path + "/travel_data.sql");
    }

    public static void sqlToFile (String input, String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(input);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}