package com.pasakinskas.currencyconverter.models;


import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Column(precision=20, scale=10)
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

    @Override
    public String toString() {
        return "CurrencyRate{" +
                "id=" + id +
                ", currencyCode='" + currencyCode + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", rateToBaseCurrency=" + rateToBaseCurrency +
                ", timeRecorded=" + timeRecorded +
                '}';
    }

    public boolean validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CurrencyRate>> violations = validator.validate(this);
        if (violations.isEmpty()) {
            return true;
        }
        String errorMessage = violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        throw new RuntimeException(errorMessage);
    }
}