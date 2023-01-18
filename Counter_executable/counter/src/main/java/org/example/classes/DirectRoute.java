package org.example.classes;

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

    public int getBestTravelOptionID() {
        return bestTravelOptionID;
    }

    public void setBestTravelOptionID(int bestTravelOptionID) {
        this.bestTravelOptionID = bestTravelOptionID;
    }

    public int getCurrentFromID() {
        return currentFromID;
    }

    public void setCurrentFromID(int currentFromID) {
        this.currentFromID = currentFromID;
    }

    public int getCurrentToID() {
        return currentToID;
    }

    public void setCurrentToID(int currentToID) {
        this.currentToID = currentToID;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }
}