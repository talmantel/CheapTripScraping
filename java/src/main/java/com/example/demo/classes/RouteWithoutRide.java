package com.example.demo.classes;

import java.util.ArrayList;

public class RouteWithoutRide {
    private int id;
    private int from;
    private int to;
    private float euro_price;
    private ArrayList<Integer> travel_data;

    public RouteWithoutRide(int id, int from, int to, float euro_price, ArrayList<Integer> travel_data) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.euro_price = euro_price;
        this.travel_data = travel_data;
    }

    RouteWithoutRide(){}

    public String toString() {
        return "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", euro_price=" + euro_price +
                ", travel_data='" + travel_data;
    }
}
