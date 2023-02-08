package org.example.visual.additional_classes;

public class DBImportList {

    boolean importCountries;
    boolean importCurrencies;
    boolean importLocations;
    boolean importTransportationTypes;
    boolean importTravelData;
    boolean importRoutesCounter;
    boolean importFixedRoutesCounter;
    boolean importFlyingRoutesCounter;

    public DBImportList (boolean importCountries,
                         boolean importCurrencies,
                         boolean importLocations,
                         boolean importTransportationTypes,
                         boolean importTravelData,
                         boolean importRoutesCounter,
                         boolean importFixedRoutesCounter,
                         boolean importFlyingRoutesCounter) {
        this.importCountries = importCountries;
        this.importCurrencies = importCurrencies;
        this.importLocations = importLocations;
        this.importTransportationTypes = importTransportationTypes;
        this.importTravelData = importTravelData;
        this.importRoutesCounter = importRoutesCounter;
        this.importFixedRoutesCounter = importFixedRoutesCounter;
        this.importFlyingRoutesCounter = importFlyingRoutesCounter;
    }

    public boolean isImportRoutesCounter() {
        return importRoutesCounter;
    }

    public void setImportRoutesCounter(boolean importRoutesCounter) {
        this.importRoutesCounter = importRoutesCounter;
    }

    public boolean isImportFixedRoutesCounter() {
        return importFixedRoutesCounter;
    }

    public void setImportFixedRoutesCounter(boolean importFixedRoutesCounter) {
        this.importFixedRoutesCounter = importFixedRoutesCounter;
    }

    public boolean isImportFlyingRoutesCounter() {
        return importFlyingRoutesCounter;
    }

    public void setImportFlyingRoutesCounter(boolean importFlyingRoutesCounter) {
        this.importFlyingRoutesCounter = importFlyingRoutesCounter;
    }

    public boolean isImportCountries() {
        return importCountries;
    }

    public void setImportCountries(boolean importCountries) {
        this.importCountries = importCountries;
    }

    public boolean isImportCurrencies() {
        return importCurrencies;
    }

    public void setImportCurrencies(boolean importCurrencies) {
        this.importCurrencies = importCurrencies;
    }

    public boolean isImportLocations() {
        return importLocations;
    }

    public void setImportLocations(boolean importLocations) {
        this.importLocations = importLocations;
    }

    public boolean isImportTransportationTypes() {
        return importTransportationTypes;
    }

    public void setImportTransportationTypes(boolean importTransportationTypes) {
        this.importTransportationTypes = importTransportationTypes;
    }

    public boolean isImportTravelData() {
        return importTravelData;
    }

    public void setImportTravelData(boolean importTravelData) {
        this.importTravelData = importTravelData;
    }

}
