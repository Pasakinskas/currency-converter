package com.pasakinskas.currencyconverter.controllers;

import com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepository;
import com.pasakinskas.currencyconverter.dataTransfer.CurrencyRateDTO;
import com.pasakinskas.currencyconverter.models.CurrencyRate;
import com.pasakinskas.currencyconverter.services.CurrencyRateFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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
        if (currencyRate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CurrencyRateDTO rate = new CurrencyRateDTO(
                currencyRate.getCurrencyCode(),
                currencyRate.getBaseCurrency(),
                currencyRate.getRateToBaseCurrency(),
                currencyRate.getDate()
        );
        return new ResponseEntity<>(rate, HttpStatus.OK);
    }

    @GetMapping("/update-rates")
    public ResponseEntity<List<CurrencyRateDTO>> getLiveRates() {
        List<CurrencyRate> liveRates = currencyRateFetcher.updateCurrencyRates();
        List<CurrencyRateDTO> publicRates = liveRates.stream()
                .map(rate -> new CurrencyRateDTO(
                    rate.getCurrencyCode(),
                    rate.getBaseCurrency(),
                    rate.getRateToBaseCurrency(),
                    rate.getDate()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(publicRates, HttpStatus.OK);
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getUniqueCurrencies() {
        List<String> currencyCodes = currencyRateRepository.findDistinctCurrencyCodes();
        return new ResponseEntity<>(currencyCodes, HttpStatus.OK);
    }

    @GetMapping("/historic")
    public ResponseEntity<List<CurrencyRate>> getHistorics(
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate
    ) {
        List<CurrencyRate> currencyCodes = currencyRateFetcher.fetchHistoricCurrencyHates(startDate, endDate);
        return new ResponseEntity<>(currencyCodes, HttpStatus.OK);
    }

    @GetMapping("/historic-rates/{code}")
    public List<CurrencyRate> getCurrencyRate(
            @PathVariable("code") String currencyCode,
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate
        ) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return currencyRateRepository.findCurrencyRatesBetweenDates(
                formatter.parse(startDate),
                formatter.parse(endDate),
                currencyCode
            );
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
