package com.pasakinskas.currencyconverter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepository;
import com.pasakinskas.currencyconverter.models.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.WebServiceException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CurrencyRateFetcher {

    @Value("${app.api-url}")
    private String API_URL;

    @Value("${app.default-currency-code}")
    private String DEFAULT_CURRENCY_CODE;

    @Value("${app.historic-api}")
    private String HISTORIC_API;

    final private CurrencyRateRepository currencyRateRepository;

    @Autowired
    public CurrencyRateFetcher(CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    private String getApiUrl() {
        return API_URL + DEFAULT_CURRENCY_CODE;
    }

    private String getHistoricApiUrl(String startDate, String endDate) {
        return HISTORIC_API + "?start_at=" + startDate + "&end_at=" + endDate + "&base=" + DEFAULT_CURRENCY_CODE;
    }

    public List<CurrencyRate> updateCurrencyRates() {
        JsonNode json = requestCurrencyRates(getApiUrl());
        List<CurrencyRate> fetchedCurrencyRates = parseCurrencyRates(json);
        fetchedCurrencyRates.forEach(CurrencyRate::validate);
        return currencyRateRepository.saveAll(fetchedCurrencyRates);
    }

    private JsonNode requestCurrencyRates(String apiUrl) {
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(json.get("date").asText());
            return jsonToCurrencyRates(liveRates, baseCurrency, date);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CurrencyRate> jsonToCurrencyRates(JsonNode json, String baseCurrency, Date date) {
        List<CurrencyRate> currencyRates = new ArrayList<>();
        json.fieldNames().forEachRemaining(currencyCode -> {
            BigDecimal rateToBaseCurrency = json.findValue(currencyCode).decimalValue();
            CurrencyRate currencyRate = new CurrencyRate(
                    currencyCode,
                    baseCurrency,
                    rateToBaseCurrency,
                    date
            );
            currencyRate.validate();
            currencyRates.add(currencyRate);
        });
        return currencyRates;
    }

    public List<CurrencyRate> fetchHistoricCurrencyHates(String beginDate, String endDate) {
        JsonNode json = requestCurrencyRates(getHistoricApiUrl(beginDate, endDate));
        List<CurrencyRate> historicRates = parseHistoricRates(json);
        historicRates.forEach(CurrencyRate::validate);
        return currencyRateRepository.saveAll(historicRates);
    }

    public List<CurrencyRate> parseHistoricRates(JsonNode json) {
        String baseCurrency = json.get("base").textValue();
        JsonNode historicRates = json.get("rates");

        List<CurrencyRate> historicCurrencyRates = new ArrayList<>();
        historicRates
            .fieldNames()
            .forEachRemaining(fieldName -> {
                Date date = getDateFromString(fieldName, "yyyy-MM-dd");
                JsonNode historicRateValues = historicRates.get(fieldName);
                historicCurrencyRates.addAll(jsonToCurrencyRates(historicRateValues, baseCurrency, date));
        });

        return historicCurrencyRates;
    }

    private Date getDateFromString(String dateString, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
