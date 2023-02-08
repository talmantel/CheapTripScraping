package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.parser.ParserDBtoCSV;
import org.example.visual.additional_classes.DBCredentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBImportCSV {

    DBCredentials credentials = CounterMenuTest.credentials;
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    JFrame csvFrame;
    JPanel csvPanel;
    JLabel routesLabel;
    JTextField routesPath;
    JButton routesButton;
    JPanel routesPanel;
    JLabel fixedRoutesLabel;
    JTextField fixedRoutesPath;
    JButton fixedRoutesButton;
    JPanel fixedRoutesPanel;
    JLabel flyingRoutesLabel;
    JTextField flyingRoutesPath;
    JButton flyingRoutesButton;
    JPanel flyingRoutesPanel;

    public DBImportCSV (){
        csvFrame = new JFrame("Импорт from locations в формате .csv");
        csvPanel = new JPanel();

        routesPanel = new JPanel();
        routesLabel = new JLabel("Путь для сохранения from__.csv routes");
        routesPath = new JTextField();
        routesButton = new JButton("Импорт routes");
        routesButton.setSize(30,20);
        routesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!routesPath.getText().equals("")) {
                    stringMaker("Parsing from locations to csv (routes)...");
                    String path = routesPath.getText();
                    ParserDBtoCSV.csvExtractRoutesCommon(connectionMaker(credentials),"routes", path);
                    ParserDBtoCSV.csvExtractRoutesPartly(connectionMaker(credentials),"routes",path);
                    stringMaker("Parsing from locations to csv (routes) finished");
                } else {
                    stringMaker("Didn't chosen folder for saving routes");
                }
            }
        });
        routesPanel.add(routesLabel);
        routesPanel.add(routesPath);
        routesPanel.setLayout(new GridLayout(1,2,0,10));


        fixedRoutesPanel = new JPanel();
        fixedRoutesLabel = new JLabel("Путь для сохранения from__.csv fixed_routes");
        fixedRoutesPath = new JTextField();
        fixedRoutesButton = new JButton("Импорт fixed_routes");
        fixedRoutesButton.setSize(30,20);
        fixedRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fixedRoutesPath.getText().equals("")) {
                    stringMaker("Parsing from locations to csv (fixed_routes)...");
                    String path = fixedRoutesPath.getText();
                    ParserDBtoCSV.csvExtractRoutesCommon(connectionMaker(credentials),"fixed_routes",path);
                    ParserDBtoCSV.csvExtractRoutesPartly(connectionMaker(credentials),"fixed_routes",path);
                    stringMaker("Parsing from locations to csv (fixed_routes) finished");
                } else {
                    stringMaker("Didn't chosen folder for saving fixed_routes");
                }
            }
        });
        fixedRoutesPanel.add(fixedRoutesLabel);
        fixedRoutesPanel.add(fixedRoutesPath);
        fixedRoutesPanel.setLayout(new GridLayout(1,2,0,10));

        flyingRoutesPanel = new JPanel();
        flyingRoutesLabel = new JLabel("Путь для сохранения from__.csv flying_routes");
        flyingRoutesPath = new JTextField();
        flyingRoutesButton = new JButton("Импорт flying_routes");
        flyingRoutesButton.setSize(30,20);
        flyingRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flyingRoutesPath.getText().equals("")) {
                    stringMaker("Parsing from locations to csv (flying routes)...");
                    String path = flyingRoutesPath.getText();
                    ParserDBtoCSV.csvExtractRoutesCommon(connectionMaker(credentials),"flying_routes",path);
                    ParserDBtoCSV.csvExtractRoutesPartly(connectionMaker(credentials),"flying_routes",path);
                    stringMaker("Parsing from locations to csv (flying_routes) finished");
                } else {
                    stringMaker("Didn't chosen folder for saving flying_routes");
                }
            }
        });
        flyingRoutesPanel.add(flyingRoutesLabel);
        flyingRoutesPanel.add(flyingRoutesPath);
        flyingRoutesPanel.setLayout(new GridLayout(1,2,0,10));


        csvPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        csvPanel.setLayout(new GridLayout(6, 2, 0, 20));
        csvPanel.add(routesPanel);
        csvPanel.add(routesButton);
        csvPanel.add(fixedRoutesPanel);
        csvPanel.add(fixedRoutesButton);
        csvPanel.add(flyingRoutesPanel);
        csvPanel.add(flyingRoutesButton);

        csvFrame.add(csvPanel, BorderLayout.CENTER);
        csvFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        csvFrame.setBounds(300, 150, 40, 40);
        csvFrame.setSize(new Dimension(50,60));
        csvFrame.pack();
        csvFrame.setVisible(true);
    }

    public Connection connectionMaker (DBCredentials credentials) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(credentials.getURL() + "/" + credentials.getSchema(),credentials.getUser(),credentials.getPassword());
        } catch (SQLException e) {
            System.out.println("DBImportCSV: connection lost");
        }
        return connection;
    }

    public void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
