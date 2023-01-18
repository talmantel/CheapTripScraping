package org.example.visual;

import org.example.CounterMenuTest;
import org.example.visual.additional_classes.DBCredentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBSettings {

    public static DBCredentials credentials = CounterMenuTest.credentials;
    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);

    JFrame settingsFrame;
    JPanel settingsPanel;

    JTextField loginField;
    JLabel loginLabel;
    JPanel loginPanel;


    JTextField passwordField;
    JLabel passwordLabel;
    JPanel passwordPanel;

    JTextField urlField;
    JLabel urlLabel;
    JPanel urlPanel;

    JTextField schemaField;
    JLabel schemaLabel;
    JPanel schemaPanel;

    JButton readyButton;

    public DBSettings(){

        settingsFrame = new JFrame();
        settingsPanel = new JPanel();

        loginPanel = new JPanel();
        loginField = new JTextField("");
        loginLabel = new JLabel("Введите имя пользователя базы данных",SwingConstants.CENTER);
        loginPanel.add(loginField);
        loginPanel.add(loginLabel);
        loginPanel.setLayout(new GridLayout(0, 1, 10, 0));

        //password panel
        passwordPanel = new JPanel();
        passwordField = new JTextField("");
        passwordLabel = new JLabel("Введите пароль к базе данных",SwingConstants.CENTER);
        passwordPanel.add(passwordField);
        passwordPanel.add(passwordLabel);
        passwordPanel.setLayout(new GridLayout(0, 1, 10, 0));

        //url panel
        urlPanel = new JPanel();
        urlField = new JTextField("");
        urlLabel = new JLabel("Введите URL базы данных",SwingConstants.CENTER);
        urlPanel.add(urlField);
        urlPanel.add(urlLabel);
        urlPanel.setLayout(new GridLayout(0, 1, 10, 0));

        //schema panel
        schemaPanel = new JPanel();
        schemaField = new JTextField("");
        schemaLabel = new JLabel("Введите название схемы БД",SwingConstants.CENTER);
        schemaPanel.add(schemaField);
        schemaPanel.add(schemaLabel);
        schemaPanel.setLayout(new GridLayout(0, 1, 10, 0));

        readyButton = new JButton("Готово");
        readyButton.setSize(new Dimension(30,30));
        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String login = loginField.getText();
                    String password = passwordField.getText();
                    String URL = urlField.getText();
                    String schema = schemaField.getText();
                    credentials.setUser(login);
                    credentials.setPassword(password);
                    credentials.setURL(URL);
                    if (schema.equals("")) {
                        credentials.setSchema("data_counter");
                    } else {
                        credentials.setSchema(schema);
                    }
                    System.out.println(credentials.toString());
                    try {
                        schema = credentials.getSchema();
                        Connection connection = DriverManager.getConnection(URL,login,password);
                        stringMaker("Successfully connected to database");
                        String query = "CREATE SCHEMA IF NOT EXISTS " + schema + ";";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.execute();
                        stringMaker("Schema " + schema + " exists");
                    } catch (SQLException exception) {
                        stringMaker("No connection with the database");
                    }
                } catch (NullPointerException nullPointerException) {
                    stringMaker("Entered data is insufficient");
                }
                settingsFrame.dispose();
            }
        });

        settingsPanel.setBorder(BorderFactory.createEmptyBorder(80,80,80,80));
        settingsPanel.setLayout(new GridLayout(0, 1, 0, 20));

        settingsPanel.add(loginPanel);
        settingsPanel.add(passwordPanel);
        settingsPanel.add(urlPanel);
        settingsPanel.add(schemaPanel);

        settingsPanel.add(readyButton);

        settingsFrame.add(settingsPanel, BorderLayout.CENTER);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setTitle("DB settings");
        settingsFrame.pack();
        settingsFrame.setVisible(true);
    }

    public void stringMaker (String input) {
        System.out.println(input);
        stream.println(input);
    }
}
