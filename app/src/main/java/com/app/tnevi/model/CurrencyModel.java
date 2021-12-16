package com.app.tnevi.model;

public class CurrencyModel {

    private String currenyName, currencyId;

    public CurrencyModel(String currenyName, String currencyId) {
        this.currenyName = currenyName;
        this.currencyId = currencyId;
    }

    public String getCurrenyName() {
        return currenyName;
    }

    public void setCurrenyName(String currenyName) {
        this.currenyName = currenyName;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }
}
