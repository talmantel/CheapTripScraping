package com.example.demo.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TransportationType {
    public int id;
    public String name;

    public TransportationType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TransportationType(){}

    public String toString() {
        return "id=" + id +
                ", name='" + name;
    }
}
