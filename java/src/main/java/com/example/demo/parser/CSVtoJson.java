package com.example.demo.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;

public class CSVtoJson {

    public static String[] CSVoString(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();
        String[] lines = line.split("\\),\\(");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("[(')]", "");
        }
        return lines;
    }

    public static JsonObject[] countriesToJson (String [] input) throws IOException{
        int k = input.length;
        JsonObject[] output = new JsonObject[k];
        for (int i = 0; i < k; i++){
            String [] arr = input[i].split(",");
            JsonObject object = new JsonObject();
            object.addProperty("country_name", arr[0]);
            object.addProperty("country_id", Integer.parseInt(arr[1]));
            object.addProperty("country_name_ru", arr[2]);
            output[i] = object;
        }
        return output;
    }

    public static JsonObject[] currenciesToJson (String [] input) throws IOException {
        int k = input.length;
        JsonObject[] output = new JsonObject[k];
        for (int i = 0; i < k; i++){
            String [] arr = input[i].split(",");
            JsonObject object = new JsonObject();
            object.addProperty("id",Integer.parseInt(arr[0]));
            object.addProperty("name",arr[1]);
            object.addProperty("code",arr[2]);
            object.addProperty("symbol",arr[3]);
            object.addProperty("one_euro_rate",Float.parseFloat(arr[4]));
            object.addProperty("rtr_symbol",arr[5]);
            output[i] = object;
            System.out.println(jsonObjectToString(object));
        }
        return output;
    }

    public static JsonObject[] routesToJsonAlt (String [] input) {
        int k = input.length;
        JsonObject[] output = new JsonObject[k];
        for (int i = 0; i < k; i++){
            String [] arr = input[i].split(",");
            JsonObject object = new JsonObject();
            //object.addProperty("id",Integer.parseInt(arr[0]));
            object.addProperty("id",arr[0]);
            object.addProperty("from",Integer.parseInt(arr[1]));
            object.addProperty("to",Integer.parseInt(arr[2]));
            object.addProperty("euro_price",Float.parseFloat(arr[3]));
            JsonArray dataList = new JsonArray();
            for (int j = 4; j < arr.length; j++) {
                dataList.add(Integer.parseInt(arr[j]));
            }
            object.add("travel_data",dataList);
            output[i] = object;
            System.out.println(jsonObjectToString(object));
        }
        return output;
    }

    public static JsonObject[] locationsToJson (String [] input) {
        int k = input.length;
        JsonObject[] output = new JsonObject[k];
        for (int i = 0; i < k; i++){
            String [] arr = input[i].split(",");
            JsonObject object = new JsonObject();
            object.addProperty("id",Integer.parseInt(arr[0]));
            object.addProperty("name", arr[1]);
            object.addProperty("country_id",Integer.parseInt(arr[2]));
            object.addProperty("latitude",Double.parseDouble(arr[3]));
            object.addProperty("longitude",Double.parseDouble(arr[4]));
            object.addProperty("name_ru",arr[5]);
            output[i] = object;
        }
        return output;
    }

    public static JsonObject[] transportationTypesToJson (String [] input) {
        int k = input.length;
        JsonObject[] output = new JsonObject[k];
        for (int i = 0; i < k; i++){
            String [] arr = input[i].split(",");
            JsonObject object = new JsonObject();
            object.addProperty("id",Integer.parseInt(arr[0]));
            object.addProperty("name", arr[1]);
            output[i] = object;
        }
        return output;
    }


    public static JsonObject[] travelDataToJson (String [] input) {
        int k = input.length;
        JsonObject[] output = new JsonObject[k];
        for (int i = 0; i < k; i++){
            String [] arr = input[i].split(",");
            JsonObject object = new JsonObject();
            object.addProperty("id", Integer.parseInt(arr[0]));
            object.addProperty("from", Integer.parseInt(arr[1]));
            object.addProperty("to", Integer.parseInt(arr[2]));
            object.addProperty("transportation_type", Integer.parseInt(arr[3]));
            object.addProperty("euro_price", Float.parseFloat(arr[4]));
            object.addProperty("time_in_minutes", Integer.parseInt(arr[5]));
            output[i] = object;
        }
        return output;
    }

    public static void jsonToFile (JsonObject[] input, String filename) {
        int k = input.length;
        JsonObject general = new JsonObject();
        for (int i = 0; i < k; i++){
            general.add(String.valueOf(i+1),input[i]);
        }

        try (FileWriter file = new FileWriter(filename)) {
            System.out.println(general.toString());
            file.write(general.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String jsonObjectToString (JsonObject object) {
        return object.toString();
    }

}
