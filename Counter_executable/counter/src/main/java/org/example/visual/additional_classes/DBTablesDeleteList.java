package org.example.visual.additional_classes;

public class DBTablesDeleteList {
    boolean deleteCountries;
    boolean deleteCurrencies;
    boolean deleteLocations;
    boolean deleteTravelData;
    boolean deleteTransportationTypes;
    boolean deleteRoutes;
    boolean deleteFixedRoutes;
    boolean deleteFlyingRoutes;
    boolean deleteTravelDataCounter;

    public DBTablesDeleteList (boolean deleteCountries,
                               boolean deleteCurrencies,
                               boolean deleteLocations,
                               boolean deleteTravelData,
                               boolean deleteTransportationTypes,
                               boolean deleteRoutes,
                               boolean deleteFixedRoutes,
                               boolean deleteFlyingRoutes,
                               boolean deleteTravelDataCounter) {
        this.deleteCountries = deleteCountries;
        this.deleteCurrencies = deleteCurrencies;
        this.deleteLocations = deleteLocations;
        this.deleteTravelData = deleteTravelData;
        this.deleteTransportationTypes = deleteTransportationTypes;
        this.deleteRoutes = deleteRoutes;
        this.deleteFixedRoutes = deleteFixedRoutes;
        this.deleteFlyingRoutes = deleteFlyingRoutes;
        this.deleteTravelDataCounter = deleteTravelDataCounter;
    }

    public boolean isDeleteCountries() {
        return deleteCountries;
    }

    public void setDeleteCountries(boolean deleteCountries) {
        this.deleteCountries = deleteCountries;
    }

    public boolean isDeleteCurrencies() {
        return deleteCurrencies;
    }

    public void setDeleteCurrencies(boolean deleteCurrencies) {
        this.deleteCurrencies = deleteCurrencies;
    }

    public boolean isDeleteLocations() {
        return deleteLocations;
    }

    public void setDeleteLocations(boolean deleteLocations) {
        this.deleteLocations = deleteLocations;
    }

    public boolean isDeleteTravelData() {
        return deleteTravelData;
    }

    public void setDeleteTravelData(boolean deleteTravelData) {
        this.deleteTravelData = deleteTravelData;
    }

    public boolean isDeleteTransportationTypes() {
        return deleteTransportationTypes;
    }

    public void setDeleteTransportationTypes(boolean deleteTransportationTypes) {
        this.deleteTransportationTypes = deleteTransportationTypes;
    }

    public boolean isDeleteRoutes() {
        return deleteRoutes;
    }

    public void setDeleteRoutes(boolean deleteRoutes) {
        this.deleteRoutes = deleteRoutes;
    }

    public boolean isDeleteFixedRoutes() {
        return deleteFixedRoutes;
    }

    public void setDeleteFixedRoutes(boolean deleteFixedRoutes) {
        this.deleteFixedRoutes = deleteFixedRoutes;
    }

    public boolean isDeleteFlyingRoutes() {
        return deleteFlyingRoutes;
    }

    public void setDeleteFlyingRoutes(boolean deleteFlyingRoutes) {
        this.deleteFlyingRoutes = deleteFlyingRoutes;
    }

    public boolean isDeleteTravelDataCounter() {
        return deleteTravelDataCounter;
    }

    public void setDeleteTravelDataCounter(boolean deleteTravelDataCounter) {
        this.deleteTravelDataCounter = deleteTravelDataCounter;
    }
}
