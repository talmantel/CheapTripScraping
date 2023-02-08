package org.example.classes;

public class Country {

    private String country_name;
    private int country_id;
    private String country_name_ru;

    public Country(String country_name, int country_id, String country_name_ru) {
        this.country_name = country_name;
        this.country_id = country_id;
        this.country_name_ru = country_name_ru;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name_ru() {
        return country_name_ru;
    }

    public void setCountry_name_ru(String country_name_ru) {
        this.country_name_ru = country_name_ru;
    }
}

