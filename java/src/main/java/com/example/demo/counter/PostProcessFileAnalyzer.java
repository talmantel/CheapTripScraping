package com.example.demo.counter;

import com.example.demo.Constants;
import com.example.demo.parser.CSVtoJson;

import java.io.IOException;
import java.util.HashMap;

public class PostProcessFileAnalyzer {
    public static void main(String[] args) {

    }

    public static void travelDataCounterFromFile(String filename) throws IOException {
        String[] input = CSVtoJson.CSVoString(filename);
        HashMap<Integer, Long> map = new HashMap<>();
        int k = input.length;
        for (int i = 0; i < k; i++) {
            String[] arr = input[i].split(",");
            for (int j = 4; j < arr.length; j++) {
                if (!map.containsKey(arr[j])) {
                    map.put(Integer.parseInt(arr[j]), (long) 1);
                } else {
                    long number = map.get(Integer.parseInt(arr[j]));
                    map.put(Integer.parseInt(arr[j]), number + 1);
                }
            }
        }
    }
}
