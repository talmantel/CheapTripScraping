package org.example.visual.additional_classes;

public class DBImportSQLList {
    boolean importCountriesSQL;
    boolean importCurrenciesSQL;
    boolean importLocationsSQL;
    boolean importTransportationTypesSQL;
    boolean importTravelDataSQL;
    boolean importRoutesSQL;
    boolean importFixedRoutesSQL;
    boolean importFlyingRoutesSQL;

    public DBImportSQLList (boolean importCountriesSQL,
                         boolean importCurrenciesSQL,
                         boolean importLocationsSQL,
                         boolean importTransportationTypesSQL,
                         boolean importTravelDataSQL,
                         boolean importRoutesSQL,
                         boolean importFixedRoutesSQL,
                         boolean importFlyingRoutesSQL) {
        this.importCountriesSQL = importCountriesSQL;
        this.importCurrenciesSQL = importCurrenciesSQL;
        this.importLocationsSQL = importLocationsSQL;
        this.importTransportationTypesSQL = importTransportationTypesSQL;
        this.importTravelDataSQL = importTravelDataSQL;
        this.importRoutesSQL = importRoutesSQL;
        this.importFixedRoutesSQL = importFixedRoutesSQL;
        this.importFlyingRoutesSQL = importFlyingRoutesSQL;
    }

    public boolean isImportCountriesSQL() {
        return importCountriesSQL;
    }

    public void setImportCountriesSQL(boolean importCountriesSQL) {
        this.importCountriesSQL = importCountriesSQL;
    }

    public boolean isImportCurrenciesSQL() {
        return importCurrenciesSQL;
    }

    public void setImportCurrenciesSQL(boolean importCurrenciesSQL) {
        this.importCurrenciesSQL = importCurrenciesSQL;
    }

    public boolean isImportLocationsSQL() {
        return importLocationsSQL;
    }

    public void setImportLocationsSQL(boolean importLocationsSQL) {
        this.importLocationsSQL = importLocationsSQL;
    }

    public boolean isImportTransportationTypesSQL() {
        return importTransportationTypesSQL;
    }

    public void setImportTransportationTypesSQL(boolean importTransportationTypesSQL) {
        this.importTransportationTypesSQL = importTransportationTypesSQL;
    }

    public boolean isImportTravelDataSQL() {
        return importTravelDataSQL;
    }

    public void setImportTravelDataSQL(boolean importTravelDataSQL) {
        this.importTravelDataSQL = importTravelDataSQL;
    }

    public boolean isImportRoutesSQL() {
        return importRoutesSQL;
    }

    public void setImportRoutesSQL(boolean importRoutesSQL) {
        this.importRoutesSQL = importRoutesSQL;
    }

    public boolean isImportFixedRoutesSQL() {
        return importFixedRoutesSQL;
    }

    public void setImportFixedRoutesSQL(boolean importFixedRoutesSQL) {
        this.importFixedRoutesSQL = importFixedRoutesSQL;
    }

    public boolean isImportFlyingRoutesSQL() {
        return importFlyingRoutesSQL;
    }

    public void setImportFlyingRoutesSQL(boolean importFlyingRoutesSQL) {
        this.importFlyingRoutesSQL = importFlyingRoutesSQL;
    }

}
