package com.pasakinskas.currencyconverter.dataAccess;

import com.pasakinskas.currencyconverter.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    CurrencyRate findFirstByCurrencyCodeOrderByTimeRecordedDesc(String currencyCode);

    default CurrencyRate findByCurrencyCode(String email) {
        return findFirstByCurrencyCodeOrderByTimeRecordedDesc(email);
    }

    @Query("SELECT DISTINCT currencyRate.currencyCode FROM CurrencyRate currencyRate")
    List<String> findDistinctCurrencyCodes();
}
