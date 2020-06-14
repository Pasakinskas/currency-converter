package com.pasakinskas.currencyconverter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepository;
import com.pasakinskas.currencyconverter.models.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.WebServiceException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CurrencyRateFetcher {

    @Value("${app.api-url}")
    private String API_URL;

    @Value("${app.default-currency-code}")
    private String DEFAULT_CURRENCY_CODE;

    final private CurrencyRateRepository currencyRateRepository;

    @Autowired
    public CurrencyRateFetcher(CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    private String getApiUrl() {
        return API_URL + DEFAULT_CURRENCY_CODE;
    }

    public List<CurrencyRate> updateCurrencyRates() {
        JsonNode json = requestLiveCurrencyRates(getApiUrl());
        List<CurrencyRate> fetchedCurrencyRates = parseCurrencyRates(json);
        fetchedCurrencyRates.forEach(CurrencyRate::validate);
        return currencyRateRepository.saveAll(fetchedCurrencyRates);
    }

    private JsonNode requestLiveCurrencyRates(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> response = restTemplate
                .getForEntity(apiUrl, String.class);

        if (response.getStatusCode().isError()) {
            throw new WebServiceException("Error fetching live rates");
        }
        try {
            return mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    public List<CurrencyRate> parseCurrencyRates(JsonNode json) {
        JsonNode liveRates = json.get("rates");
        String baseCurrency = json.get("base").textValue();
        return jsonToCurrencyRates(liveRates, baseCurrency);
    }

    public List<CurrencyRate> jsonToCurrencyRates(JsonNode json, String baseCurrency) {
        List<CurrencyRate> currencyRates = new ArrayList<>();
        json.fieldNames().forEachRemaining(currencyCode -> {
            BigDecimal rateToBaseCurrency = BigDecimal.valueOf(json.findValue(currencyCode).asLong());
            CurrencyRate currencyRate = new CurrencyRate(
                    currencyCode,
                    baseCurrency,
                    rateToBaseCurrency,
                    Instant.now()
            );
            currencyRate.validate();
            currencyRates.add(currencyRate);
        });
        return currencyRates;
    }

    public void fetchHistoricCurrencyHates(Date beginDate, Date endDate) {}
}
