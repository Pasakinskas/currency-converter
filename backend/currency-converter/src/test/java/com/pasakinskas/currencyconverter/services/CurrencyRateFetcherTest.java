package com.pasakinskas.currencyconverter.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepository;
import com.pasakinskas.currencyconverter.models.CurrencyRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyRateFetcherTest {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    final private CurrencyRateFetcher currencyRateFetcher = new CurrencyRateFetcher(currencyRateRepository);

    @Test
    public void when_parsingValidJson_currencyRatesFormed() {
        JsonNode twoCurrencyRates = getJsonWithTwoCurrencyRates();
        List<CurrencyRate> testRates = currencyRateFetcher.parseCurrencyRates(twoCurrencyRates);
        Assert.assertEquals(2, testRates.size());
    }

    @Test
    public void when_buildingCurrencyRates_expectedValuesSet() {
        JsonNode twoCurrencyRates = getJsonWithTwoCurrencyRates();
        CurrencyRate testRate = currencyRateFetcher.parseCurrencyRates(twoCurrencyRates).get(0);

        Assert.assertEquals("USD", testRate.getBaseCurrency());
        Assert.assertEquals("CAD", testRate.getCurrencyCode());
        Assert.assertEquals(BigDecimal.valueOf(1.123456789), testRate.getRateToBaseCurrency());
        Assert.assertNotNull(testRate.getDate());
    }

    @Test
    public void when_parsingBadJson_getError() {
        JsonNode jsonWithMissingData = getJsonWithMissingData();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            currencyRateFetcher.parseCurrencyRates(jsonWithMissingData);
        });

        String expectedMessage = "must be greater than 0.0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private JsonNode getJsonWithTwoCurrencyRates() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ObjectNode child = mapper.createObjectNode();
        root.put("base", "USD");
        root.put("date", "2000-01-01");
        root.set("rates", child);

        child.put("CAD", 1.123456789);
        child.put("EUR", 0.987654321);
        return root;
    }

    private JsonNode getJsonWithMissingData() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ObjectNode child = mapper.createObjectNode();
        root.put("date", "2000-01-01");
        root.put("base", "USD");
        root.set("rates", child);

        child.put("USD", "broken");
        return root;
    }
}
