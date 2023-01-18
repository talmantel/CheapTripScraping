package org.example.classes;

import java.util.ArrayList;

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

    public ArrayList<Integer> getTravel_data() {
        return travel_data;
    }

    public void setTravel_data(ArrayList<Integer> travel_data) {
        this.travel_data = travel_data;
    }
}
