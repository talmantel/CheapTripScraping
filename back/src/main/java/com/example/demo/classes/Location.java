package com.example.demo.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Location {
    private int id;
    private String name;
    private int country_id;
    private double latitude;
    private double longitude;
    private String name_ru;

    public Location(int id, String name, int country_id, double latitude, double longitude, String name_ru) {
        this.id = id;
        this.name = name;
        this.country_id = country_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name_ru = name_ru;
    }

    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", country_id=" + country_id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name_ru='" + name_ru;
    }
}