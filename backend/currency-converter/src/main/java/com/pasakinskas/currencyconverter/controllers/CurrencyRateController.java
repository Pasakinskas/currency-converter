package com.pasakinskas.currencyconverter.controllers;

import com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepository;
import com.pasakinskas.currencyconverter.dataTransfer.CurrencyRateDTO;
import com.pasakinskas.currencyconverter.models.CurrencyRate;
import com.pasakinskas.currencyconverter.services.CurrencyRateFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@CrossOrigin
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

    @GetMapping("/currency-rates/{code}")
    public ResponseEntity<CurrencyRateDTO> getCurrencyRate(@PathVariable("code") String currencyCode) {
        CurrencyRate currencyRate = currencyRateRepository.findByCurrencyCode(currencyCode);
        if (currencyRate != null) {
            CurrencyRateDTO rate = new CurrencyRateDTO(
                    currencyRate.getCurrencyCode(),
                    currencyRate.getBaseCurrency(),
                    currencyRate.getRateToBaseCurrency()
            );
            return new ResponseEntity<>(rate, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/update-rates")
    public ResponseEntity<List<CurrencyRate>> getLiveRates() {
        List<CurrencyRate> liveRates = currencyRateFetcher.updateCurrencyRates();
        return new ResponseEntity<>(liveRates, HttpStatus.OK);
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getUniqueCurrencies() {
        List<String> currencyCodes = currencyRateRepository.findDistinctCurrencyCodes();
        return new ResponseEntity<>(currencyCodes, HttpStatus.OK);
    }
}
