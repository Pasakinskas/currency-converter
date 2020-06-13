package com.pasakinskas.currencyconverter.controllers;

import com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepository;
import com.pasakinskas.currencyconverter.models.CurrencyRate;
import com.pasakinskas.currencyconverter.services.CurrencyRateFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CurrencyRateController {
    final private CurrencyRateRepository currencyRateRepository;
    final private CurrencyRateFetcher currencyRateFetcher;

    @Autowired
    public CurrencyRateController(
            CurrencyRateRepository currencyRateRepository,
            CurrencyRateFetcher currencyRateFetcher
    ) {
        this.currencyRateRepository = currencyRateRepository;
        this.currencyRateFetcher = currencyRateFetcher;
    }

    @GetMapping("/currency-rates")
    public List<CurrencyRate> getAllCurrencyRates() {
        return currencyRateRepository.findAll();
    }

    @GetMapping("/live-rates")
    public List<CurrencyRate> getLiveRates() {
        return currencyRateFetcher.updateCurrencyRates();
    }

    @GetMapping("/unique-currencies")
    public List<String> getUniqueCurrencies() {
        return currencyRateRepository.findDistinctCurrencyCodes();
    }

}
