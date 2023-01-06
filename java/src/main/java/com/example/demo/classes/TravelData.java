package com.example.demo.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TravelData {
    private int id;
    private int from;
    private int to;
    private int transportation_type;
    private float euro_price;
    private int time_in_minutes;

    public TravelData(int id, int from, int to, int transportation_type, float euro_price, int time_in_minutes) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.transportation_type = transportation_type;
        this.euro_price = euro_price;
        this.time_in_minutes = time_in_minutes;
    }

    public TravelData(int from, int to, int transportation_type, float euro_price, int time_in_minutes) {
        this.from = from;
        this.to = to;
        this.transportation_type = transportation_type;
        this.euro_price = euro_price;
        this.time_in_minutes = time_in_minutes;
    }

    public TravelData() {
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", transportation_type=" + transportation_type +
                ", euro_price=" + euro_price +
                ", time_in_minutes=" + time_in_minutes;
    }

    public String getName() {
        return "" + id;
    }
}
