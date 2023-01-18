package org.example.visual.additional_classes;

public class DBCounterTypes {

    boolean countAll;
    boolean countFixed;
    boolean countFlying;
    boolean resultToDatabase;
    boolean resultToString;
    boolean travelDataCountWithoutDirect;

    public DBCounterTypes (boolean countAll,
                           boolean countFixed,
                           boolean countFlying,
                           boolean resultToDatabase,
                           boolean resultToString,
                           boolean travelDataCountWithoutDirect) {
        this.countAll = countAll;
        this.countFixed = countFixed;
        this.countFlying = countFlying;
        this.resultToDatabase = resultToDatabase;
        this.resultToString = resultToString;
        this.travelDataCountWithoutDirect = travelDataCountWithoutDirect;
    }

    public boolean isResultToDatabase() {
        return resultToDatabase;
    }

    public void setResultToDatabase(boolean resultToDatabase) {
        this.resultToDatabase = resultToDatabase;
    }

    public boolean isResultToString() {
        return resultToString;
    }

    public void setResultToString(boolean resultToString) {
        this.resultToString = resultToString;
    }

    public boolean isTravelDataCountWithoutDirect() {
        return travelDataCountWithoutDirect;
    }

    public void setTravelDataCountWithoutDirect(boolean travelDataCountWithoutDirect) {
        this.travelDataCountWithoutDirect = travelDataCountWithoutDirect;
    }

    public boolean isCountAll() {
        return countAll;
    }

    public void setCountAll(boolean countAll) {
        this.countAll = countAll;
    }

    public boolean isCountFixed() {
        return countFixed;
    }

    public void setCountFixed(boolean countFixed) {
        this.countFixed = countFixed;
    }

    public boolean isCountFlying() {
        return countFlying;
    }

    public void setCountFlying(boolean countFlying) {
        this.countFlying = countFlying;
    }
}
