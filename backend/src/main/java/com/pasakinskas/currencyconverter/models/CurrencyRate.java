package com.pasakinskas.currencyconverter.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name="currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String currencyCode;

    @NotNull
    private String baseCurrency;

    @NotNull
    private BigDecimal rateToBaseCurrency;

    @NotNull
    private Instant timeRecorded;

    public CurrencyRate() {}

    public CurrencyRate(
            String currencyCode,
            String baseCurrency,
            BigDecimal rateToBaseCurrency,
            Instant timeRecorded
    ) {
        this.currencyCode = currencyCode;
        this.baseCurrency = baseCurrency;
        this.rateToBaseCurrency = rateToBaseCurrency;
        this.timeRecorded = timeRecorded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getTimeRecorded() {
        return timeRecorded;
    }

    public void setTimeRecorded(Instant timeRecorded) {
        this.timeRecorded = timeRecorded;
    }
}