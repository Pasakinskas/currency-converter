package com.pasakinskas.currencyconverter.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
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
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(precision=20, scale=10)
    private BigDecimal rateToBaseCurrency;

    @NotNull
    private Date date;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    public CurrencyRate() {}

    public CurrencyRate(
            String currencyCode,
            String baseCurrency,
            BigDecimal rateToBaseCurrency,
            Date date
    ) {
        this.currencyCode = currencyCode;
        this.baseCurrency = baseCurrency;
        this.rateToBaseCurrency = rateToBaseCurrency;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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