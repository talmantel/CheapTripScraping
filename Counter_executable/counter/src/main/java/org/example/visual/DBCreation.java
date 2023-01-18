package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.table_maker.TablesInitializer;
import org.example.visual.additional_classes.DBCredentials;
import org.example.visual.additional_classes.DBTablesCreationList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCreation {

    DBTablesCreationList creationList;
    public DBCredentials credentials = CounterMenuTest.credentials;
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);

    JFrame creationFrame;
    JPanel creationPanel;

    JCheckBox createCountries;
    JCheckBox createCurrencies;
    JCheckBox createLocations;
    JCheckBox createTransportationTypes;
    JCheckBox createTravelData;

    JCheckBox createCountingTable;
    JCheckBox createAdditional;
    JButton createButton;

    public DBCreation() {
        creationList = new DBTablesCreationList(false,false,false,false,false,false,false,false);
        creationFrame = new JFrame("Создание таблиц");
        creationPanel = new JPanel();

        createCountries = new JCheckBox("Создать таблицу 'countries'");
        createCurrencies = new JCheckBox("Создать таблицу 'currencies'");
        createLocations = new JCheckBox("Создать таблицу 'locations'");
        createTransportationTypes = new JCheckBox("Создать таблицу 'transportation_types'");
        createTravelData = new JCheckBox("Создать таблицу 'travel_data'");
        createCountingTable = new JCheckBox("Создать результирующие таблицы расчета");
        createAdditional = new JCheckBox("Дополнительные таблицы (travel_data_count, countries, currencies)");
        createButton = new JButton("Готово");

        createButton.setSize(40,40);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (createCountries.isSelected()) {
                    creationList.setCreateCountries(true);
                } else {
                    creationList.setCreateCountries(false);
                }

                if (createCurrencies.isSelected()) {
                    creationList.setCreateCurrencies(true);
                } else {
                    creationList.setCreateCurrencies(false);
                }

                if (createLocations.isSelected()) {
                    creationList.setCreateLocations(true);
                } else {
                    creationList.setCreateLocations(false);
                }

                if (createTransportationTypes.isSelected()) {
                    creationList.setCreateTransportationTypes(true);
                } else {
                    creationList.setCreateTransportationTypes(false);
                }

                if (createTravelData.isSelected()) {
                    creationList.setCreateTravelData(true);
                } else {
                    creationList.setCreateTravelData(false);
                }

                if (createCountingTable.isSelected()) {
                    creationList.setCreateRoutes(true);
                    creationList.setCreateFixedRoutes(true);
                    creationList.setCreateFlyingRoutes(true);
                } else {
                    creationList.setCreateRoutes(false);
                    creationList.setCreateFixedRoutes(false);
                    creationList.setCreateFlyingRoutes(false);
                }

                if (creationList.isCreateCountries()) {
                    TablesInitializer.countriesMaker(connectionMaker(credentials));
                }

                if (creationList.isCreateLocations()) {
                    TablesInitializer.locationTableMaker(connectionMaker(credentials));
                }

                if (creationList.isCreateCurrencies()) {
                    TablesInitializer.currenciesMaker(connectionMaker(credentials));
                }

                if (creationList.isCreateRoutes()) {
                    TablesInitializer.routesTableMaker(connectionMaker(credentials));
                }

                if (creationList.isCreateFixedRoutes()) {
                    TablesInitializer.fixedRoutesTableMaker(connectionMaker(credentials));
                }

                if (creationList.isCreateFlyingRoutes()) {
                    TablesInitializer.flyingRoutesTableMaker(connectionMaker(credentials));
                }

                if (creationList.isCreateTravelData()) {
                    TablesInitializer.travelDataTableMaker(connectionMaker(credentials));
                }

                if (creationList.isCreateTransportationTypes()) {
                    TablesInitializer.transportationTypesMaker(connectionMaker(credentials));
                }
            }
        });

        creationPanel.setBorder(BorderFactory.createEmptyBorder(80,80,80,80));
        creationPanel.setLayout(new GridLayout(0, 1, 0, 20));

        creationPanel.add(createCountries);
        creationPanel.add(createCurrencies);
        creationPanel.add(createLocations);
        creationPanel.add(createTransportationTypes);
        creationPanel.add(createTravelData);
        creationPanel.add(createCountingTable);
        creationPanel.add(createButton);

        creationFrame.add(creationPanel, BorderLayout.CENTER);
        creationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        creationFrame.pack();
        creationFrame.setVisible(true);
    }

    public Connection connectionMaker (DBCredentials credentials) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(credentials.getURL() + "/" + credentials.getSchema(),credentials.getUser(),credentials.getPassword());
        } catch (SQLException e) {
            stream.println("DBCreation: connection lost");
        }
        return connection;
    }
}
