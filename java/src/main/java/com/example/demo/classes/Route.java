package com.example.demo.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
@Getter
@Setter
public class Route {
    private int id;
    private int from;
    private int to;
    private float euro_price;
    private ArrayList<Integer> travel_data;

    public Route(int id, int from, int to, float euro_price, ArrayList<Integer> travel_data) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.euro_price = euro_price;
        this.travel_data = travel_data;
    }

    public Route(){}

    public String toString() {
        return "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", euro_price=" + euro_price +
                ", travel_data='" + travel_data;
    }

    public String getName() {
        return ""+id;
    }

//    public String dataArrayToString(int [] array){
//        String output = "";
//        for (int i = 0; i < array.length; i++) {
//            output = output + array[i];
//            if (i < array.length-1) {
//                output = output + ",";
//            }
//        }
//        return output;
//    }
}
