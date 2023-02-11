package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.counters.Counters;
import org.example.visual.additional_classes.DBCounterTypes;
import org.example.visual.additional_classes.DBCredentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.*;

public class DBCounting {

    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    DBCredentials credentials = CounterMenuTest.credentials;
    DBCounterTypes counterTypes;
    JFrame countingFrame;
    JPanel countingPanel;
    JCheckBox flyingRoutes;
    JCheckBox fixedRoutes;
    JCheckBox allRoutes;
    JCheckBox databaseSave;
    JCheckBox stringSave;
    JTextField placeForStringToSave;
    JLabel placeToSaveLabel;
    JButton countButton;

    public DBCounting () {
        counterTypes = new DBCounterTypes(true,false,false, true, false, false);
        countingFrame = new JFrame("Counter settings");
        countingPanel = new JPanel();
        allRoutes = new JCheckBox("Calculation of 'routes'");
        flyingRoutes = new JCheckBox("Расчет 'flying_routes'");
        fixedRoutes = new JCheckBox("Расчет 'fixed_routes'");
        databaseSave = new JCheckBox("Save data into the database");
        stringSave = new JCheckBox("Save results into .csv file");
        placeToSaveLabel = new JLabel("Folder where to save file .csv");
        placeForStringToSave = new JTextField(40);
        countButton = new JButton("Start counting");

        JPanel placePanel = new JPanel();
        placePanel.add(placeToSaveLabel);
        placePanel.add(placeForStringToSave);
        placePanel.setLayout(new GridLayout(1, 2, 0, 20));

        countButton.setSize(40,40);
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (databaseSave.isSelected()){
                    counterTypes.setResultToDatabase(true);
                } else {
                    counterTypes.setResultToDatabase(false);
                }

                if (stringSave.isSelected() && !placeForStringToSave.getText().equals("")) {
                    counterTypes.setResultToString(true);
                }
                else {
                    counterTypes.setResultToString(false);
                }

                if (!databaseSave.isSelected() && !stringSave.isSelected()){
                    stringMaker("You didn't chose data storage. Choose how to save data.");
                }

                if (allRoutes.isSelected()) {
                    String allowed_transportation_types = "(1,2,3,5,7,9,10)";
                    stringMaker("Counting routes...");
                    Counters.calculateRoutes(connectionMaker(credentials),
                            allowed_transportation_types,
                            "routes",
                            counterTypes.isResultToString(),
                            counterTypes.isResultToDatabase(),
                            placeForStringToSave.getText() + "/routes.csv");
                    stringMaker("routes successfully counted");
                }

                if (flyingRoutes.isSelected()) {
                    String allowed_transportation_types = "(1)";
                    stringMaker("Counting flying_routes...");
                    Counters.calculateRoutes(connectionMaker(credentials),
                                allowed_transportation_types,
                                "flying_routes",
                                counterTypes.isResultToString(),
                                counterTypes.isResultToDatabase(),
                                placeForStringToSave.getText() + "/flying_routes.csv");
                        stringMaker("flying_routes successfully counted");
                }

                if (fixedRoutes.isSelected()) {
                    String allowed_transportation_types = "(2,3,5,7,9,10)";
                    stringMaker("Counting fixed_routes...");
                       Counters.calculateRoutes(connectionMaker(credentials),
                            allowed_transportation_types,
                            "fixed_routes",
                            counterTypes.isResultToString(),
                            counterTypes.isResultToDatabase(),
                            placeForStringToSave.getText() + "/fixed_routes.csv");
                    stringMaker("fixed_routes successfully counted");
                }
            }
        });

        countingPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        countingPanel.setLayout(new GridLayout(8, 2, 0, 20));
        countingPanel.add(allRoutes);
        countingPanel.add(flyingRoutes);
        countingPanel.add(fixedRoutes);
        countingPanel.add(databaseSave);
        countingPanel.add(stringSave);
        countingPanel.add(placePanel);
        countingPanel.add(countButton);

        countingFrame.add(countingPanel, BorderLayout.CENTER);
        countingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        countingFrame.setBounds(300, 150, 40, 40);
        countingFrame.setSize(new Dimension(100,60));
        countingFrame.pack();
        countingFrame.setVisible(true);
    }

    public Connection connectionMaker (DBCredentials credentials) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(credentials.getURL() + "/" + credentials.getSchema(),credentials.getUser(),credentials.getPassword());
        } catch (SQLException e) {
            stream.println("DBCounting: connection lost");
        }
        return connection;
    }

    public void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
