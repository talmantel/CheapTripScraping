package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.parser.ParserDBtoJson;
import org.example.visual.additional_classes.DBCredentials;
import org.example.visual.additional_classes.DBImportList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBImportJson {

    DBCredentials credentials = CounterMenuTest.credentials;
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    DBImportList importList;
    JFrame importFrame;
    JPanel importPanel;
    JCheckBox importCountries;
    JCheckBox importCurrencies;
    JCheckBox importLocations;
    JCheckBox importTransportationTypes;
    JCheckBox importTravelData;
    JCheckBox importRoutes;
    JCheckBox importFixedRoutes;
    JCheckBox importFlyingRoutes;

    JCheckBox importRoutesCounter;

    JCheckBox importFixedRoutesCounter;

    JCheckBox importFlyingRoutesCounter;

    JPanel pathPanel;
    JLabel pathLabel;
    JTextField pathField;

    JButton importStart;

    public DBImportJson() {

        importList = new DBImportList(false,false,false,false,false,false,false,false, false,false,false);

        importFrame = new JFrame("Импорт баз данных в формате Json");
        importPanel = new JPanel();

        Dimension dimension = new Dimension(20,15);

        importCountries = new JCheckBox("Сформировать countries.json");
        importCurrencies = new JCheckBox("Сформировать currencies.json");
        importLocations = new JCheckBox("Сформировать locations.json");
        importTransportationTypes = new JCheckBox("Сформировать transportation_types.json");
        importTravelData = new JCheckBox("Сформировать travel_data.json");
        importRoutes = new JCheckBox("Сформировать routes.json");
        importFixedRoutes = new JCheckBox("Сформировать fixed_routes.json");
        importFlyingRoutes = new JCheckBox("Сформировать flying_routes.json");
        importRoutesCounter = new JCheckBox("Сформировать travel_data_counter_routes.json");
        importFixedRoutesCounter = new JCheckBox("Сформировать travel_data_counter_fixed_routes.json");
        importFlyingRoutesCounter = new JCheckBox("Сформировать travel_data_counter_flying_routes.json");

        pathPanel = new JPanel();
        pathLabel = new JLabel("Путь к папке: ", SwingConstants.LEFT);
        pathField = new JTextField(40);
        pathLabel.setSize(dimension);
        pathField.setSize(dimension);
        pathPanel.add(pathLabel);
        pathPanel.add(pathField);
        pathPanel.setLayout(new GridLayout(1,2,0,10));

        importStart = new JButton("Импорт");
        importStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (importCountries.isSelected()) {
                    importList.setImportCountries(true);
                } else {
                    importList.setImportCountries(false);
                }

                if (importCurrencies.isSelected()) {
                    importList.setImportCurrencies(true);
                } else {
                    importList.setImportCurrencies(false);
                }

                if (importLocations.isSelected()) {
                    importList.setImportLocations(true);
                } else {
                    importList.setImportLocations(false);
                }

                if (importTransportationTypes.isSelected()) {
                    importList.setImportTransportationTypes(true);
                } else {
                    importList.setImportTransportationTypes(false);
                }

                if (importTravelData.isSelected()) {
                    importList.setImportTravelData(true);
                } else {
                    importList.setImportTravelData(false);
                }

                if (importRoutes.isSelected()) {
                    importList.setImportRoutes(true);
                } else {
                    importList.setImportRoutes(false);
                }

                if (importFixedRoutes.isSelected()) {
                    importList.setImportFixedRoutes(true);
                } else {
                    importList.setImportFixedRoutes(false);
                }

                if (importFlyingRoutes.isSelected()) {
                    importList.setImportFlyingRoutes(true);
                } else {
                    importList.setImportFlyingRoutes(false);
                }

                if (importRoutesCounter.isSelected()) {
                    importList.setImportRoutesCounter(true);
                } else {
                    importList.setImportRoutesCounter(false);
                }

                if (importFixedRoutesCounter.isSelected()) {
                    importList.setImportFixedRoutesCounter(true);
                } else {
                    importList.setImportFixedRoutesCounter(false);
                }

                if (importFlyingRoutesCounter.isSelected()) {
                    importList.setImportFlyingRoutesCounter(true);
                } else {
                    importList.setImportFlyingRoutesCounter(false);
                }

                if (importList.isImportCountries() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'countries' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.countriesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/countries.json");
                    stringMaker("Parsing 'countries' to Json finished");
                }

                if (importList.isImportCurrencies() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'currencies' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.currenciesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/currencies.json");
                    stringMaker("Parsing 'currencies' to Json finished");
                }

                if (importList.isImportLocations() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'locations' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.locationsToJson(connectionMaker(credentials)),
                            pathField.getText()+"/locations.json");
                    stringMaker("Parsing 'locations' to Json finished");
                }

                if (importList.isImportTransportationTypes() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'transportation_types' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.transportationTypesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/transportation_types.json");
                    stringMaker("Parsing 'transportation_types' to Json finished");
                }

                if (importList.isImportTravelData() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'travel_data' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.travelDataToJson(connectionMaker(credentials)),
                            pathField.getText()+"/travel_data.json");
                    stringMaker("Parsing 'travel_data' to Json finished");
                }

                if (importList.isImportRoutes() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'routes' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.routesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/routes.json");
                    stringMaker("Parsing 'routes' to Json finished");
                }

                if (importList.isImportFixedRoutes() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'fixed_routes' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.fixedRoutesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/fixed_routes.json");
                    stringMaker("Parsing 'fixed_routes' to Json finished");
                }

                if (importList.isImportFlyingRoutes() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'flying_routes' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.flyingRoutesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/flying_routes.json");
                    stringMaker("Parsing 'flying_routes' to Json finished");
                }

                if (importList.isImportRoutesCounter() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'travel_data_counter_routes' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.travelDataCounterRoutesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/travel_data_counter_routes.json");
                    stringMaker("Parsing 'travel_data_counter_routes' to Json finished");
                }

                if (importList.isImportFixedRoutesCounter() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'travel_data_counter_fixed_routes' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.travelDataCounterFixedRoutesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/travel_data_counter_fixed_routes.json");
                    stringMaker("Parsing 'travel_data_counter_fixed_routes' to Json finished");
                }

                if (importList.isImportFlyingRoutesCounter() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'travel_data_counter_flying_routes' to Json...");
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.travelDataCounterFlyingRoutesToJson(connectionMaker(credentials)),
                            pathField.getText()+"/travel_data_counter_flying_routes.json");
                    stringMaker("Parsing 'travel_data_counter_flying_routes' to Json finished");
                }
            }
        });

        importPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        importPanel.setLayout(new GridLayout(13, 2, 0, 10));
        importPanel.add(importCountries);
        importPanel.add(importCurrencies);
        importPanel.add(importLocations);
        importPanel.add(importTransportationTypes);
        importPanel.add(importTravelData);
        importPanel.add(importRoutes);
        importPanel.add(importFixedRoutes);
        importPanel.add(importFlyingRoutes);
        importPanel.add(importRoutesCounter);
        importPanel.add(importFixedRoutesCounter);
        importPanel.add(importFlyingRoutesCounter);
        importPanel.add(pathPanel);
        importPanel.add(importStart);

        importFrame.add(importPanel, BorderLayout.CENTER);
        importFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        importFrame.setBounds(300, 70, 40, 40);
        importFrame.setSize(new Dimension(100,60));
        importFrame.pack();
        importFrame.setVisible(true);
    }

    public Connection connectionMaker (DBCredentials credentials) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(credentials.getURL() + "/" + credentials.getSchema(),credentials.getUser(),credentials.getPassword());
        } catch (SQLException e) {
            stream.println("DBImportJson: lost connection");
        }
        return connection;
    }

    public void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
