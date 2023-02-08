package org.example.classes;

public class TransportationType {

    private int id;
    private String name;

    public TransportationType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TransportationType() {
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
}
