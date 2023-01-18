package org.example.functional.parser;

import org.example.CounterMenuTest;
import org.example.visual.Console;

import java.io.*;

public class ParserCSVtoString {

    public static Console console = CounterMenuTest.console;
    public static PrintStream stream = new PrintStream(console);
    public static String[] CSVoString(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = "";
        String add = "";
        while (add != null) {
            String str = "";
            add = reader.readLine();
            if (add == null) {
                str = "";
            } else {
                str = "(" + add + "),";
            }
            line = line + str;
        }

        String[] lines = line.split("\\),\\(");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("[(')]", "");
            lines[i] = lines[i].replaceAll("null", "");
        }
        return lines;
    }
}
