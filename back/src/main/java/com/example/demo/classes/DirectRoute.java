package com.example.demo.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DirectRoute {
    private int bestTravelOptionID;
    private int currentFromID;
    private int currentToID;
    private float minPrice;

    public DirectRoute(int bestTravelOptionID, int currentFromID, int currentToID, float minPrice) {
        this.bestTravelOptionID = bestTravelOptionID;
        this.currentFromID = currentFromID;
        this.currentToID = currentToID;
        this.minPrice = minPrice;
    }

    public DirectRoute(){}
}
