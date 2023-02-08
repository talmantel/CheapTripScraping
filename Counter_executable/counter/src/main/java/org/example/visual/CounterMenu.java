package org.example.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CounterMenu {

    //Visual interface
    public JFrame counterMenuFrame;
    JPanel counterMenuPanel;
    JButton DBSettingsButton;
    JButton DBCreationButton;
    JButton DBLoadingButton;
    JButton DBDeleteButton;
    JButton StartCountingButton;
    JButton ImportToJsonButton;

    JButton ImportRoutesJsonButton;

    JButton ImportRoutesCSVButton;

    JButton ImportSQLButton;

    public CounterMenu (){
        counterMenuFrame = new JFrame();
        counterMenuPanel = new JPanel();
        DBSettingsButton = new JButton("Настройка базы данных");
        DBCreationButton = new JButton("Создание таблиц");
        DBLoadingButton = new JButton("Наполнение таблиц");
        DBDeleteButton = new JButton("Удаление таблиц");
        StartCountingButton = new JButton("Управление расчетом");
        ImportToJsonButton = new JButton("Импорт баз данных в формате .json");
        ImportRoutesJsonButton = new JButton("Импорт routes в формате .json");
        ImportRoutesCSVButton = new JButton("Импорт routes в формате .csv");
        ImportSQLButton = new JButton("Импорт SQL файлов таблиц базы данных");

        DBSettingsButton.setSize(40,40);
        DBSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBSettings settings = new DBSettings();
            }
        });

        DBCreationButton.setSize(40,40);
        DBCreationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCreation creation = new DBCreation();
            }
        });

        DBLoadingButton.setSize(40,40);
        DBLoadingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBLoading loading = new DBLoading();
            }
        });

        DBDeleteButton.setSize(40,40);
        DBDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBDelete delete = new DBDelete();
            }
        });

        StartCountingButton.setSize(40,40);
        StartCountingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCounting counting = new DBCounting();
            }
        });

        ImportToJsonButton.setSize(40,40);
        ImportToJsonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBImportJson importJson = new DBImportJson();
            }
        });

        ImportRoutesJsonButton.setSize(40,40);
        ImportRoutesJsonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBImportRoutesJson routesJson = new DBImportRoutesJson();
            }
        });

        ImportRoutesCSVButton.setSize(40,40);
        ImportRoutesCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBImportCSV importCSV = new DBImportCSV();
            }
        });

        ImportSQLButton.setSize(40,40);
        ImportSQLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBImportSQL importSQL = new DBImportSQL();
            }
        });

        counterMenuPanel.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
        counterMenuPanel.setLayout(new GridLayout(0, 1, 0, 20));

        counterMenuPanel.add(DBSettingsButton);
        counterMenuPanel.add(DBCreationButton);
        counterMenuPanel.add(DBLoadingButton);
        counterMenuPanel.add(DBDeleteButton);
        counterMenuPanel.add(StartCountingButton);
        counterMenuPanel.add(ImportToJsonButton);
        counterMenuPanel.add(ImportRoutesJsonButton);
        counterMenuPanel.add(ImportRoutesCSVButton);
        counterMenuPanel.add(ImportSQLButton);

        counterMenuFrame.add(counterMenuPanel, BorderLayout.CENTER);
        counterMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        counterMenuFrame.setTitle("Меню расчетчика");
        counterMenuFrame.pack();
        counterMenuFrame.setVisible(true);
    }
}
