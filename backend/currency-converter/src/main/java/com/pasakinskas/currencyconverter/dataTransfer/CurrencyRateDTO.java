package com.pasakinskas.currencyconverter.dataTransfer;

import java.math.BigDecimal;

public class CurrencyRateDTO {

    private String currencyCode;
    private String baseCurrency;
    private BigDecimal rateToBaseCurrency;

    public CurrencyRateDTO(String currencyCode, String baseCurrency, BigDecimal rateToBaseCurrency) {
        this.currencyCode = currencyCode;
        this.baseCurrency = baseCurrency;
        this.rateToBaseCurrency = rateToBaseCurrency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getRateToBaseCurrency() {
        return rateToBaseCurrency;
    }

    public void setRateToBaseCurrency(BigDecimal rateToBaseCurrency) {
        this.rateToBaseCurrency = rateToBaseCurrency;
    }
}
