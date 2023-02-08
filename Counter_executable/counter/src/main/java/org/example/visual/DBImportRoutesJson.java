package org.example.visual;

import org.example.CounterMenuTest;
import org.example.functional.parser.ParserDBtoCSV;
import org.example.functional.parser.ParserDBtoJson;
import org.example.visual.additional_classes.DBCredentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBImportRoutesJson {
    DBCredentials credentials = CounterMenuTest.credentials;
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    JFrame jsonFrame;
    JPanel jsonPanel;
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

    public DBImportRoutesJson (){
        jsonFrame = new JFrame("Импорт from locations в формате .json");
        jsonPanel = new JPanel();

        routesPanel = new JPanel();
        routesLabel = new JLabel("Путь для сохранения from__.json routes");
        routesPath = new JTextField();
        routesButton = new JButton("Импорт routes");
        routesButton.setSize(30,20);
        routesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!routesPath.getText().equals("")) {
                    stringMaker("Parsing from locations to json (routes)...");
                    String path = routesPath.getText();
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.routesToJson(connectionMaker(credentials)), path + "/routes.json");
                    ParserDBtoJson.routesFromLocationsToJson(connectionMaker(credentials),"routes",path);
                    stringMaker("Working with parsing to json (routes) finished");
                } else {
                    stringMaker("Didn't chosen folder for saving routes");
                }
            }
        });
        routesPanel.add(routesLabel);
        routesPanel.add(routesPath);
        routesPanel.setLayout(new GridLayout(1,2,0,10));


        fixedRoutesPanel = new JPanel();
        fixedRoutesLabel = new JLabel("Путь для сохранения from__.json fixed_routes");
        fixedRoutesPath = new JTextField();
        fixedRoutesButton = new JButton("Импорт fixed_routes");
        fixedRoutesButton.setSize(30,20);
        fixedRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fixedRoutesPath.getText().equals("")) {
                    stringMaker("Parsing from locations to json (fixed_routes)...");
                    String path = fixedRoutesPath.getText();
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.fixedRoutesToJson(connectionMaker(credentials)), path +
                            "/fixed_routes.json");
                    ParserDBtoJson.routesFromLocationsToJson(connectionMaker(credentials),"fixed_routes",path);
                    stringMaker("Working with parsing to json (fixed_routes) finished");
                } else {
                    stringMaker("Didn't chosen folder for saving fixed_routes");
                }
            }
        });
        fixedRoutesPanel.add(fixedRoutesLabel);
        fixedRoutesPanel.add(fixedRoutesPath);
        fixedRoutesPanel.setLayout(new GridLayout(1,2,0,10));

        flyingRoutesPanel = new JPanel();
        flyingRoutesLabel = new JLabel("Путь для сохранения from__.json flying_routes");
        flyingRoutesPath = new JTextField();
        flyingRoutesButton = new JButton("Импорт flying_routes");
        flyingRoutesButton.setSize(30,20);
        flyingRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flyingRoutesPath.getText().equals("")) {
                    stringMaker("Parsing from locations to json (flying routes)...");
                    String path = flyingRoutesPath.getText();
                    ParserDBtoJson.jsonToFile(ParserDBtoJson.flyingRoutesToJson(connectionMaker(credentials)),
                            path + "/flying_routes.json");
                    ParserDBtoJson.routesFromLocationsToJson(connectionMaker(credentials),"flying_routes",path);
                    stringMaker("Working with parsing to json (flying_routes) finished");
                } else {
                    stringMaker("Didn't chosen folder for saving flying_routes");
                }
            }
        });
        flyingRoutesPanel.add(flyingRoutesLabel);
        flyingRoutesPanel.add(flyingRoutesPath);
        flyingRoutesPanel.setLayout(new GridLayout(1,2,0,10));


        jsonPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        jsonPanel.setLayout(new GridLayout(6, 2, 0, 20));
        jsonPanel.add(routesPanel);
        jsonPanel.add(routesButton);
        jsonPanel.add(fixedRoutesPanel);
        jsonPanel.add(fixedRoutesButton);
        jsonPanel.add(flyingRoutesPanel);
        jsonPanel.add(flyingRoutesButton);

        jsonFrame.add(jsonPanel, BorderLayout.CENTER);
        jsonFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jsonFrame.setBounds(300, 150, 40, 40);
        jsonFrame.setSize(new Dimension(50,60));
        jsonFrame.pack();
        jsonFrame.setVisible(true);
    }

    public Connection connectionMaker (DBCredentials credentials) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(credentials.getURL() + "/" + credentials.getSchema(),credentials.getUser(),credentials.getPassword());
        } catch (SQLException e) {
            System.out.println("DBImportRoutesJson: connection lost");
        }
        return connection;
    }

    public void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
