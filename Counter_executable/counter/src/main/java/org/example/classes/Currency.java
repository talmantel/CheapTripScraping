package org.example.classes;

public class Currency {
    private int id;
    private String name;
    private String code;
    private String symbol;
    private float one_euro_rate;
    private String rtr_symbol;

    public Currency(int id, String name, String code, String symbol, float one_euro_rate, String rtr_symbol) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.symbol = symbol;
        this.one_euro_rate = one_euro_rate;
        this.rtr_symbol = rtr_symbol;
    }

    public Currency() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getOne_euro_rate() {
        return one_euro_rate;
    }

    public void setOne_euro_rate(float one_euro_rate) {
        this.one_euro_rate = one_euro_rate;
    }

    public String getRtr_symbol() {
        return rtr_symbol;
    }

    public void setRtr_symbol(String rtr_symbol) {
        this.rtr_symbol = rtr_symbol;
    }
}

