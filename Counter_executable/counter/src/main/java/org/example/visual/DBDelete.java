package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.table_maker.TableDropper;
import org.example.visual.additional_classes.DBCredentials;
import org.example.visual.additional_classes.DBTablesDeleteList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBDelete {

    DBCredentials credentials = CounterMenuTest.credentials;
    DBTablesDeleteList deleteList;
    JFrame deleteMenu;
    JPanel deletePanel;
    JCheckBox deleteCountries;
    JCheckBox deleteCurrencies;
    JCheckBox deleteLocations;
    JCheckBox deleteTravelData;
    JCheckBox deleteTransportationTypes;
    JCheckBox deleteCountingTables;
    JButton executeButton;
    JButton deleteSchemaButton;
    public DBDelete (){

        deleteMenu = new JFrame("Delete tables");
        deletePanel = new JPanel();

        deleteList = new DBTablesDeleteList(false,false,false,false,false,false,false,false,false);

        deleteCountries = new JCheckBox("Delete table 'countries'");
        deleteCurrencies = new JCheckBox("Delete table 'currencies'");
        deleteLocations = new JCheckBox("Delete table 'locations'");
        deleteTransportationTypes = new JCheckBox("Delete table 'transportations_types'");
        deleteTravelData = new JCheckBox("Delete table 'travel_data'");
        deleteCountingTables = new JCheckBox("Delete counting tables");

        executeButton = new JButton("Delete");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (deleteCountries.isSelected()) {
                    deleteList.setDeleteCountries(true);
                } else {
                    deleteList.setDeleteCountries(false);
                }

                if (deleteCurrencies.isSelected()) {
                    deleteList.setDeleteCurrencies(true);
                } else {
                    deleteList.setDeleteCurrencies(false);
                }

                if (deleteLocations.isSelected()) {
                    deleteList.setDeleteLocations(true);
                } else {
                    deleteList.setDeleteLocations(false);
                }

                if (deleteTransportationTypes.isSelected()) {
                    deleteList.setDeleteTransportationTypes(true);
                } else {
                    deleteList.setDeleteTransportationTypes(false);
                }

                if (deleteTravelData.isSelected()) {
                    deleteList.setDeleteTravelData(true);
                } else {
                    deleteList.setDeleteTravelData(false);
                }

                if (deleteCountingTables.isSelected()) {
                    deleteList.setDeleteRoutes(true);
                    deleteList.setDeleteFixedRoutes(true);
                    deleteList.setDeleteFlyingRoutes(true);
                    deleteList.setDeleteTravelDataCounter(true);
                } else {
                    deleteList.setDeleteRoutes(false);
                    deleteList.setDeleteFixedRoutes(false);
                    deleteList.setDeleteFlyingRoutes(false);
                    deleteList.setDeleteTravelDataCounter(false);
                }

                if (deleteList.isDeleteCountries()) {
                    TableDropper.countryDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteCurrencies()) {
                    TableDropper.currenciesDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteLocations()) {
                    TableDropper.locationsDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteTravelData()) {
                    TableDropper.travelDataDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteTransportationTypes()) {
                    TableDropper.transportationTypesDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteRoutes()) {
                    TableDropper.routesDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteFixedRoutes()) {
                    TableDropper.fixedRoutesDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteFlyingRoutes()) {
                    TableDropper.flyingRoutesDropper(connectionMaker(credentials));
                }
                if (deleteList.isDeleteTravelDataCounter()) {
                    TableDropper.travelDataCounterRoutesDropper(connectionMaker(credentials));
                    TableDropper.travelDataCounterFixedRoutesDropper(connectionMaker(credentials));
                    TableDropper.travelDataCounterFlyingRoutesDropper(connectionMaker(credentials));
                }

            }
        });

        deleteSchemaButton = new JButton("Удалить схему полностью");
        deleteSchemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableDropper.schemaDrop(connectionMaker(credentials), credentials.getSchema());
                credentials.setSchema("");
            }
        });

        deletePanel.setBorder(BorderFactory.createEmptyBorder(80,80,80,80));
        deletePanel.setLayout(new GridLayout(0, 1, 0, 15));

        deletePanel.add(deleteCountries);
        deletePanel.add(deleteCurrencies);
        deletePanel.add(deleteLocations);
        deletePanel.add(deleteTransportationTypes);
        deletePanel.add(deleteTravelData);
        deletePanel.add(deleteCountingTables);
        deletePanel.add(executeButton);
        deletePanel.add(deleteSchemaButton);

        deleteMenu.add(deletePanel, BorderLayout.CENTER);
        deleteMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteMenu.pack();
        deleteMenu.setVisible(true);
    }

    public Connection connectionMaker (DBCredentials credentials) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(credentials.getURL() + "/" + credentials.getSchema(),credentials.getUser(),credentials.getPassword());
        } catch (SQLException e) {
            System.out.println("DBDelete: connection lost");
        }
        return connection;
    }
}