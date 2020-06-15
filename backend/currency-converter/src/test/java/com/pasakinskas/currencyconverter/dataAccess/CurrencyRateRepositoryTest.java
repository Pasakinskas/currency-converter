package com.pasakinskas.currencyconverter.dataAccess;

import com.pasakinskas.currencyconverter.models.CurrencyRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CurrencyRateRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Test
    public void whenFindByCurrencyCode_returnsMostRecentRateByCurrencyCode() {
        Date earlierDate = getDateFromString("yyyy-MM-dd", "2001-01-01");
        Date laterDate = getDateFromString("yyyy-MM-dd", "2001-01-02");

        CurrencyRate first = new CurrencyRate(
                "EUR",
                "USD",
                BigDecimal.valueOf(1.123456),
                earlierDate
        );
        CurrencyRate second = new CurrencyRate(
                "EUR",
                "USD",
                BigDecimal.valueOf(9.876541),
                laterDate
        );
        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.flush();

        CurrencyRate foundEurCurrencyRate = currencyRateRepository.findByCurrencyCode("EUR");

        Assert.assertEquals(second.getRateToBaseCurrency(), foundEurCurrencyRate.getRateToBaseCurrency());
        Assert.assertEquals(second.getDate(), foundEurCurrencyRate.getDate());
        Assert.assertEquals(second.getBaseCurrency(), foundEurCurrencyRate.getBaseCurrency());
        Assert.assertEquals(second.getCurrencyCode(), foundEurCurrencyRate.getCurrencyCode());

        Assert.assertNotNull(foundEurCurrencyRate.getId());
        Assert.assertNotNull(foundEurCurrencyRate.getCreatedAt());
    }

    @Test
    public void whenSavingInvalidCurrencyRate_ValidationThrowsException() {
        Date date = getDateFromString("yyyy-MM-dd", "2001-01-01");
        CurrencyRate eurCurrencyRate = new CurrencyRate(
                "EUR",
                null,
                BigDecimal.valueOf(1.123456),
                date
        );
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(eurCurrencyRate);
            entityManager.flush();
        });

        String expectedMessage = "interpolatedMessage='must not be null', propertyPath=baseCurrency";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenFIndDistinctCurrencyCodes_ReturnListOfUniqueCurrencyCodes() {
        Date earlierDate = getDateFromString("yyyy-MM-dd", "2001-01-01");
        CurrencyRate first = new CurrencyRate(
                "EUR",
                "USD",
                BigDecimal.valueOf(1.123456),
                earlierDate
        );
        CurrencyRate second = new CurrencyRate(
                "EUR",
                "USD",
                BigDecimal.valueOf(9.876541),
                earlierDate
        );
        CurrencyRate third = new CurrencyRate(
                "CAT",
                "USD",
                BigDecimal.valueOf(9.876541),
                earlierDate
        );
        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.persist(third);
        entityManager.flush();

        List<String> currencyCodes = currencyRateRepository.findDistinctCurrencyCodes();
        assertEquals(currencyCodes.size(), 2);
        assertEquals(currencyCodes.get(0), "CAT");
        assertEquals(currencyCodes.get(1), "EUR");
    }

    public static Date getDateFromString(String dateFormat, String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}


