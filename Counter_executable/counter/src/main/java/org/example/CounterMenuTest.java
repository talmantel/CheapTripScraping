package org.example;

import org.example.functional.counters.Counters;
import org.example.visual.Console;
import org.example.visual.CounterMenu;
import org.example.visual.additional_classes.DBCredentials;

public class CounterMenuTest {
    public static Console console;
    public static DBCredentials credentials = new DBCredentials(null,null,null,null);
    public static void main(String[] args) {

        CounterMenu menu = new CounterMenu();
        console = new Console();
    }
}
