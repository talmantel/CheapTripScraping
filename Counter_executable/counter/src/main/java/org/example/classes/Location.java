package org.example.classes;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }
}
