package com.example.demo.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Currency {
    private int id;
    private String name;
    private String code;
    private String symbol;
    private float one_euro_rate;
    private String rtr_symbol;

    public Currency (int id,String name, String code, String symbol, float one_euro_rate, String rtr_symbol) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.symbol = symbol;
        this.one_euro_rate = one_euro_rate;
        this.rtr_symbol = rtr_symbol;
    }

    public Currency() {}

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", symbol='" + symbol + '\'' +
                ", one_euro_rate=" + one_euro_rate +
                ", rtr_symbol='" + rtr_symbol;
    }
}
