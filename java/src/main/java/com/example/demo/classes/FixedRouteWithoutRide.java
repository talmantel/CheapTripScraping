package com.example.demo.classes;

public class FixedRouteWithoutRide {
    private int id;
    private int from;
    private int to;
    private float euro_price;
    private String travel_data;

    public FixedRouteWithoutRide(int id, int from, int to, float euro_price, String travel_data) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.euro_price = euro_price;
        this.travel_data = travel_data;
    }

    public FixedRouteWithoutRide() {
    }

    public String toString() {
        return "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", euro_price=" + euro_price +
                ", travel_data.csv='" + travel_data;
    }
}
