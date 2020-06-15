package com.pasakinskas.currencyconverter.dataAccess;

import com.pasakinskas.currencyconverter.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    CurrencyRate findFirstByCurrencyCodeOrderByCreatedAtDesc(String currencyCode);

    default CurrencyRate findByCurrencyCode(String currencyCode) {
        return findFirstByCurrencyCodeOrderByCreatedAtDesc(currencyCode);
    }

    @Query("SELECT DISTINCT currencyRate.currencyCode FROM CurrencyRate currencyRate")
    List<String> findDistinctCurrencyCodes();
}
