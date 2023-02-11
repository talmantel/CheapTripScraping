package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.parser.ParserDBtoSQL;
import org.example.visual.additional_classes.DBCredentials;
import org.example.visual.additional_classes.DBImportSQLList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBImportSQL {

    DBCredentials credentials = CounterMenuTest.credentials;
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);

    DBImportSQLList importList;
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
    JPanel pathPanel;
    JLabel pathLabel;
    JTextField pathField;
    JButton importStart;

    public DBImportSQL () {
        importList = new DBImportSQLList(false,false,false,false,false,false,false,false);

        importFrame = new JFrame("Export tables as SQL");
        importPanel = new JPanel();

        Dimension dimension = new Dimension(20,15);

        importCountries = new JCheckBox("Export countries.sql");
        importCurrencies = new JCheckBox("Export currencies.sql");
        importLocations = new JCheckBox("Export locations.sql");
        importTransportationTypes = new JCheckBox("Export transportation_types.sql");
        importTravelData = new JCheckBox("Export travel_data.sql");
        importRoutes = new JCheckBox("Export routes.sql");
        importFixedRoutes = new JCheckBox("Export fixed_routes.sql");
        importFlyingRoutes = new JCheckBox("Export flying_routes.sql");

        pathPanel = new JPanel();
        pathLabel = new JLabel("Путь к папке: ", SwingConstants.LEFT);
        pathField = new JTextField(40);
        pathLabel.setSize(dimension);
        pathField.setSize(dimension);
        pathPanel.add(pathLabel);
        pathPanel.add(pathField);
        pathPanel.setLayout(new GridLayout(1,2,0,10));

        importStart = new JButton("Export");
        importStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (importCountries.isSelected()) {
                    importList.setImportCountriesSQL(true);
                } else {
                    importList.setImportCountriesSQL(false);
                }

                if (importCurrencies.isSelected()) {
                    importList.setImportCurrenciesSQL(true);
                } else {
                    importList.setImportCurrenciesSQL(false);
                }

                if (importLocations.isSelected()) {
                    importList.setImportLocationsSQL(true);
                } else {
                    importList.setImportLocationsSQL(false);
                }

                if (importTransportationTypes.isSelected()) {
                    importList.setImportTransportationTypesSQL(true);
                } else {
                    importList.setImportTransportationTypesSQL(false);
                }

                if (importTravelData.isSelected()) {
                    importList.setImportTravelDataSQL(true);
                } else {
                    importList.setImportTravelDataSQL(false);
                }

                if (importRoutes.isSelected()) {
                    importList.setImportRoutesSQL(true);
                } else {
                    importList.setImportRoutesSQL(false);
                }

                if (importFixedRoutes.isSelected()) {
                    importList.setImportFixedRoutesSQL(true);
                } else {
                    importList.setImportFixedRoutesSQL(false);
                }

                if (importFlyingRoutes.isSelected()) {
                    importList.setImportFlyingRoutesSQL(true);
                } else {
                    importList.setImportFlyingRoutesSQL(false);
                }

                if (importList.isImportCountriesSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'countries' to SQL...");
                    ParserDBtoSQL.countriesSQL(ParserDBtoSQL.countriesToString(connectionMaker(credentials)),
                            pathField.getText());
                    stringMaker("Working with parse of 'countries' to SQL finished");
                }

                if (importList.isImportCurrenciesSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'currencies' to SQL...");
                    ParserDBtoSQL.currenciesSQL(ParserDBtoSQL.currenciesToString(connectionMaker(credentials)),
                            pathField.getText());
                    stringMaker("Working with parse of 'currencies' to SQL finished");
                }

                if (importList.isImportLocationsSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'locations' to SQL...");
                    ParserDBtoSQL.locationsSQL(ParserDBtoSQL.locationsToString(connectionMaker(credentials)),
                            pathField.getText());
                    stringMaker("Working with parse of 'locations' to SQL finished");
                }

                if (importList.isImportTransportationTypesSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'transportation_types' to SQL...");
                    ParserDBtoSQL.transportationTypesSQL(ParserDBtoSQL.transportationTypesToString(connectionMaker(credentials)),
                            pathField.getText());
                    stringMaker("Working with parse of 'transportation_types' to SQL finished");
                }

                if (importList.isImportTravelDataSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'travel_data' to SQL...");
                    ParserDBtoSQL.travelDataSQL(ParserDBtoSQL.travelDataToString(connectionMaker(credentials)),
                            pathField.getText());
                    stringMaker("Working with parse of 'travel_data' to SQL finished");
                }

                if (importList.isImportRoutesSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'routes' to SQL...");
                    ParserDBtoSQL.routesSQL(ParserDBtoSQL.routesToString(connectionMaker(credentials),"routes"),
                            "routes", pathField.getText());
                    stringMaker("Working with parse of 'routes' to SQL finished");
                }

                if (importList.isImportFixedRoutesSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'fixed_routes' to SQL...");
                    ParserDBtoSQL.routesSQL(ParserDBtoSQL.routesToString(connectionMaker(credentials),"fixed_routes"),
                            "fixed_routes", pathField.getText());
                    stringMaker("Working with parse of 'fixed_routes' to SQL finished");
                }

                if (importList.isImportFlyingRoutesSQL() && !pathField.getText().equals("")) {
                    stringMaker("Parsing 'flying_routes' to SQL...");
                    ParserDBtoSQL.routesSQL(ParserDBtoSQL.routesToString(connectionMaker(credentials),"flying_routes"),
                            "flying_routes", pathField.getText());
                    stringMaker("Working with parse of 'flying_routes' to SQL finished");
                }
            }
        });

        importPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        importPanel.setLayout(new GridLayout(10, 2, 0, 10));
        importPanel.add(importCountries);
        importPanel.add(importCurrencies);
        importPanel.add(importLocations);
        importPanel.add(importTransportationTypes);
        importPanel.add(importTravelData);
        importPanel.add(importRoutes);
        importPanel.add(importFixedRoutes);
        importPanel.add(importFlyingRoutes);
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
            stream.println("DBImportSQL: lost connection");
        }
        return connection;
    }

    public void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
