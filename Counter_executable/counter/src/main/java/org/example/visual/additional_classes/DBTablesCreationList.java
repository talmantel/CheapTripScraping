package org.example.visual.additional_classes;

public class DBTablesCreationList {

    boolean createCountries;
    boolean createCurrencies;
    boolean createTransportationTypes;
    boolean createTravelData;
    boolean createLocations;
    boolean createRoutes;
    boolean createFixedRoutes;
    boolean createFlyingRoutes;

    public DBTablesCreationList (boolean createCountries,
                                 boolean createCurrencies,
                                 boolean createTransportationTypes,
                                 boolean createTravelData,
                                 boolean createLocations,
                                 boolean createRoutes,
                                 boolean createFixedRoutes,
                                 boolean createFlyingRoutes) {
        this.createCountries = createCountries;
        this.createCurrencies = createCurrencies;
        this.createTransportationTypes = createTransportationTypes;
        this.createTravelData = createTravelData;
        this.createLocations = createLocations;
        this.createRoutes = createRoutes;
        this.createFixedRoutes = createFixedRoutes;
        this.createFlyingRoutes = createFlyingRoutes;
    }

    public boolean isCreateCountries() {
        return createCountries;
    }

    public void setCreateCountries(boolean createCountries) {
        this.createCountries = createCountries;
    }

    public boolean isCreateCurrencies() {
        return createCurrencies;
    }

    public void setCreateCurrencies(boolean createCurrencies) {
        this.createCurrencies = createCurrencies;
    }

    public boolean isCreateTransportationTypes() {
        return createTransportationTypes;
    }

    public void setCreateTransportationTypes(boolean createTransportationTypes) {
        this.createTransportationTypes = createTransportationTypes;
    }

    public boolean isCreateTravelData() {
        return createTravelData;
    }

    public void setCreateTravelData(boolean createTravelData) {
        this.createTravelData = createTravelData;
    }

    public boolean isCreateLocations() {
        return createLocations;
    }

    public void setCreateLocations(boolean createLocations) {
        this.createLocations = createLocations;
    }

    public boolean isCreateRoutes() {
        return createRoutes;
    }

    public void setCreateRoutes(boolean createRoutes) {
        this.createRoutes = createRoutes;
    }

    public boolean isCreateFixedRoutes() {
        return createFixedRoutes;
    }

    public void setCreateFixedRoutes(boolean createFixedRoutes) {
        this.createFixedRoutes = createFixedRoutes;
    }

    public boolean isCreateFlyingRoutes() {
        return createFlyingRoutes;
    }

    public void setCreateFlyingRoutes(boolean createFlyingRoutes) {
        this.createFlyingRoutes = createFlyingRoutes;
    }
}
