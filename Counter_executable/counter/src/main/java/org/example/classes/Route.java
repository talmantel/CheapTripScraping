package org.example.classes;

import java.util.ArrayList;

public class Route {
    private int id;
    private int from;
    private int to;
    private float euro_price;
    private int trip_duration;
    private String travel_data;

    public Route(int id, int from, int to, float euro_price, int trip_duration, String travel_data) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.euro_price = euro_price;
        this.trip_duration = trip_duration;
        this.travel_data = travel_data;
    }

    public Route() {
    }

    public String toString() {
        return "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", euro_price=" + euro_price +
                ", travel_data='" + travel_data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public float getEuro_price() {
        return euro_price;
    }

    public void setEuro_price(float euro_price) {
        this.euro_price = euro_price;
    }

    public int getTrip_duration() {
        return trip_duration;
    }

    public void setTrip_duration(int trip_duration) {
        this.trip_duration = trip_duration;
    }

    public String getTravel_data() {
        return travel_data;
    }

    public void setTravel_data(String travel_data) {
        this.travel_data = travel_data;
    }
}
