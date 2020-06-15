package com.pasakinskas.currencyconverter.controllers;

import com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepository;
import com.pasakinskas.currencyconverter.models.CurrencyRate;
import com.pasakinskas.currencyconverter.services.CurrencyRateFetcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.pasakinskas.currencyconverter.dataAccess.CurrencyRateRepositoryTest.getDateFromString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyRateController.class)
public class CurrencyRateControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CurrencyRateRepository currencyRateRepository;

    @MockBean
    private CurrencyRateFetcher currencyRateFetcher;

    @Test
    public void whenRequestingCurrencyList_returnsListOfUniqueCurrencies() throws Exception {
        String[] arr = new String[]{"EUR", "USD", "GBP"};
        List<String> currencyCodes = Arrays.asList(arr);
        given(currencyRateRepository.findDistinctCurrencyCodes()).willReturn(currencyCodes);

        mvc.perform(get("/currencies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is("EUR")))
                .andExpect(jsonPath("$[1]", is("USD")))
                .andExpect(jsonPath("$[2]", is("GBP")));
    }

    @Test
    public void whenRequestingCurrencyRate_returnsValidDTO() throws Exception{
        CurrencyRate eurRate = new CurrencyRate(
                "EUR",
                "USD",
                BigDecimal.valueOf(1.12345),
                getDateFromString("yyyy-MM-dd", "2001-01-01"));

        given(currencyRateRepository.findByCurrencyCode("EUR")).willReturn(eurRate);

        mvc.perform(get("/currency-rates/EUR")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode", is("EUR")))
                .andExpect(jsonPath("$.baseCurrency", is("USD")))
                .andExpect(jsonPath("$.rateToBaseCurrency", is(1.12345)))
                .andExpect(jsonPath("$.date", is("2000-12-31T22:00:00.000+00:00")));
    }

    @Test
    public void whenRequestingInvalidCurrencyRate_returnsNotFound() throws Exception {
        given(currencyRateRepository.findByCurrencyCode("bad-code")).willReturn(null);

        mvc.perform(get("/currency-rates/bad-code")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestingLiveRates_SavedCurrencyListDTOIsReturned() throws Exception {
        List<CurrencyRate> currencyRates = new ArrayList<>();
        currencyRates.add(new CurrencyRate(
                "EUR",
                "USD",
                BigDecimal.valueOf(1.12345),
                getDateFromString("yyyy-MM-dd", "2001-01-01"))
        );
        given(currencyRateFetcher.updateCurrencyRates()).willReturn(currencyRates);
        mvc.perform(get("/update-rates")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].currencyCode", is("EUR")))
                .andExpect(jsonPath("$[0].baseCurrency", is("USD")))
                .andExpect(jsonPath("$[0].rateToBaseCurrency", is(1.12345)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].createdAt").doesNotExist());
    }
}
