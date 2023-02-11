package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.table_maker.NewInserter;
import org.example.visual.additional_classes.DBCredentials;
import org.example.functional.table_maker.OldInserter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBLoading {

    DBCredentials credentials = CounterMenuTest.credentials;
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);

    JFrame loadingFrame;
    JPanel loadingPanel;
    JCheckBox usingStandardTables;
    JCheckBox travelDataWithIndexes;
    JButton downloadButton;
    JLabel countriesLabel;
    JTextField countriesAddress;
    JLabel locationsLabel;
    JTextField locationsAddress;
    JLabel currenciesLabel;
    JTextField currenciesAddress;
    JLabel travelDataLabel;
    JTextField travelDataField;
    JLabel transportationTypesLabel;
    JTextField transportationTypesField;

    public DBLoading(){
        loadingFrame = new JFrame("Loading tables");
        loadingPanel = new JPanel();

        Dimension dimension = new Dimension(20,15);

        JPanel countryPanel = new JPanel();
        countriesLabel = new JLabel("countries: ", SwingConstants.LEFT);
        countriesAddress = new JTextField(30);
        countriesLabel.setSize(dimension);
        countriesAddress.setSize(dimension);
        countryPanel.add(countriesLabel);
        countryPanel.add(countriesAddress);
        countryPanel.setLayout(new GridLayout(1,2,0,10));

        JPanel locationsPanel = new JPanel();
        locationsLabel = new JLabel("locations: ", SwingConstants.LEFT);
        locationsAddress = new JTextField(30);
        locationsLabel.setSize(dimension);
        locationsAddress.setSize(dimension);
        locationsPanel.add(locationsLabel);
        locationsPanel.add(locationsAddress);
        locationsPanel.setLayout(new GridLayout(1,2,0,10));

        JPanel currenciesPanel = new JPanel();
        currenciesLabel = new JLabel("currencies: ", SwingConstants.LEFT);
        currenciesAddress = new JTextField(30);
        currenciesLabel.setSize(dimension);
        currenciesAddress.setSize(dimension);
        currenciesPanel.add(currenciesLabel);
        currenciesPanel.add(currenciesAddress);
        currenciesPanel.setLayout(new GridLayout(1,2,0,10));

        JPanel transportationTypesPanel = new JPanel();
        transportationTypesLabel = new JLabel("transportation_types: ", SwingConstants.LEFT);
        transportationTypesField = new JTextField(30);
        transportationTypesLabel.setSize(dimension);
        transportationTypesField.setSize(dimension);
        transportationTypesPanel.add(transportationTypesLabel);
        transportationTypesPanel.add(transportationTypesField);
        transportationTypesPanel.setLayout(new GridLayout(1,2,0,10));

        JPanel travelDataPanel = new JPanel();
        travelDataLabel = new JLabel("travel_data: ", SwingConstants.LEFT);
        travelDataField = new JTextField(30);
        travelDataLabel.setSize(dimension);
        travelDataField.setSize(dimension);
        travelDataPanel.add(travelDataLabel);
        travelDataPanel.add(travelDataField);
        travelDataPanel.setLayout(new GridLayout(1,2,0,10));

        travelDataWithIndexes = new JCheckBox("Import travel_data with 'id' column is .csv file");

        usingStandardTables = new JCheckBox("Load standard tables countries, locations, currencies, " +
                "transportation_types");

        downloadButton = new JButton("Start");
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean countries = false;
                boolean currencies = false;
                boolean locations = false;
                boolean transportation_types = false;
                if (countriesAddress.getText().equals("")) {
                    countries = false;
                } else {
                    countries = true;
                    try {
                        stringMaker("Inserting countries...");
                        NewInserter.insertNewCountries(NewInserter.CSVoString(countriesAddress.getText()),
                                connectionMaker(credentials));
                        stringMaker("Insertion of countries completed");
                    } catch (IOException e1) {
                        stringMaker("DBLoading: problem with the new insertion of 'countries'");
                    }
                }

                if (currenciesAddress.getText().equals("")) {
                    currencies = false;
                } else {
                    currencies = true;
                    try {
                        stringMaker("Inserting currencies...");
                        NewInserter.insertNewCurrencies(NewInserter.CSVoString(currenciesAddress.getText()),
                                connectionMaker(credentials));
                        stringMaker("Insertion of currencies completed");
                    } catch (IOException e1) {
                        stringMaker("DBLoading: problem with the new insertion of 'currencies'");
                    }
                }

                if (locationsAddress.getText().equals("")) {
                    locations = false;
                } else {
                    locations = true;
                    try {
                        stream.println("Inserting locations...");
                        NewInserter.creatingNewLocations((NewInserter.CSVoString(locationsAddress.getText())),
                                connectionMaker(credentials));
                        stream.println("Insertion of locations completed");
                    } catch (IOException e1) {
                        stream.println("DBLoading: problem with the new insertion of 'locations'");
                    }
                }

                if (transportationTypesField.getText().equals("")) {
                    transportation_types = false;
                } else {
                    transportation_types = true;
                    try {
                        stringMaker("Inserting transportation_types...");
                        NewInserter.insertNewTransportationTypes(NewInserter.CSVoString(transportationTypesField.getText()),
                                connectionMaker(credentials));
                        stringMaker("Insertion of transportation_types completed");
                    } catch (IOException e1) {
                        stringMaker("DBLoading: problem with the new insertion of 'transportation_types'");
                    }
                }

                if (!travelDataField.getText().equals("") && travelDataWithIndexes.isSelected()) {
                    try {
                        stringMaker("Inserting of travel_data...");
                        NewInserter.insertNewTravelDataWithIndexes(NewInserter.CSVoString(travelDataField.getText()),
                                connectionMaker(credentials));
                        stringMaker("Insertion of travel_data completed");
                    } catch (IOException e1) {
                        stringMaker("DBLoading: problem with the new insertion of 'travel_data'");
                    }
                }

                if (!travelDataField.getText().equals("") && !travelDataWithIndexes.isSelected()) {
                    try {
                        stringMaker("Inserting of travel_data...");
                        NewInserter.insertNewTravelDataWithoutIndexes(NewInserter.CSVoString(travelDataField.getText()),
                                connectionMaker(credentials));
                        stringMaker("Insertion of travel_data completed");
                    } catch (IOException e1) {
                        stringMaker("DBLoading: problem with the new insertion of 'travel_data'");
                    }
                }

                if (usingStandardTables.isSelected() && !countries) {
                    OldInserter.insertDefaultCountries(connectionMaker(credentials));
                }
                if (usingStandardTables.isSelected() && !currencies) {
                    OldInserter.insertDefaultCurrencies(connectionMaker(credentials));
                }
                if (usingStandardTables.isSelected() && !locations) {
                    OldInserter.insertDefaultLocationsOld(connectionMaker(credentials));
                }
                if (usingStandardTables.isSelected() && !transportation_types) {
                    OldInserter.insertDefaultTransportationTypes(connectionMaker(credentials));
                }
            }
        });

        loadingPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        loadingPanel.setLayout(new GridLayout(8, 2, 0, 20));
        loadingPanel.add(countryPanel);
        loadingPanel.add(locationsPanel);
        loadingPanel.add(currenciesPanel);
        loadingPanel.add(transportationTypesPanel);
        loadingPanel.add(travelDataPanel);
        loadingPanel.add(travelDataWithIndexes);
        loadingPanel.add(usingStandardTables);
        loadingPanel.add(downloadButton);

        loadingFrame.add(loadingPanel, BorderLayout.CENTER);
        loadingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loadingFrame.setBounds(300, 150, 40, 40);
        loadingFrame.setSize(new Dimension(100,60));
        loadingFrame.pack();
        loadingFrame.setVisible(true);
    }

    public Connection connectionMaker (DBCredentials credentials) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(credentials.getURL() + "/" + credentials.getSchema(),credentials.getUser(),credentials.getPassword());
        } catch (SQLException e) {
            stream.println("DBLoading: connection lost");
        }
        return connection;
    }

    public void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
