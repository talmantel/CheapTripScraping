package com.example.demo.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Country {

    private String country_name;
    private int country_id;
    private String country_name_ru;

    public Country (String country_name, int country_id, String country_name_ru) {
        this.country_name = country_name;
        this.country_id = country_id;
        this.country_name_ru = country_name_ru;
    }

    public Country () {}
}
