import React, { ChangeEvent } from 'react';

import { CurrencyDropdown } from "../CurrencyDropdown/CurrencyDropdown";
import {
  convertCurrency,
  getConversionRate,
} from "../../services/CurrencyConversion";
import { 
  fetchCurrencyCodes,
  fetchCurrencyRate,
} from "../../services/CurrencyRetrieval";
import "./CurrencyInputForm.css";

export function CurrencyInputForm() {
  const [initialCurrency, setInitialCurrency] = React.useState("");
  const [targetCurrency, setTargetCurrency] = React.useState("");
  const [initialAmount, setInitialAmount] = React.useState();
  const [convertedAmount, setConvertedAmount] = React.useState();

  const [currencyCodes, setCurrencyCodes] = React.useState([]);
  const [currencyRates, setCurrencyRates] = React.useState(new Map());
  const [conversionRate, setConversionRate] = React.useState("");

  const [showErrorMessage, setShowErrorMessage] = React.useState(false)

  React.useEffect(() => {
    const asyncGetCurrencyCodes = async () => {
      setCurrencyCodes(await fetchCurrencyCodes())
    }

    const updateConversionRate = () => {
      const initialRate = currencyRates.get(initialCurrency);
      const targetRate = currencyRates.get(targetCurrency);

      if (initialRate && targetRate) {
        setConversionRate(
          getConversionRate(initialRate, targetRate)
          .toString()
        );
      }
    }
    asyncGetCurrencyCodes();
    updateConversionRate();
  }, [currencyRates, initialCurrency, targetCurrency]);

  const convert = () => {
    const initialRate = currencyRates.get(initialCurrency);
    const targetRate = currencyRates.get(targetCurrency);

    if (initialRate && targetRate && initialAmount) {
      setShowErrorMessage(false);
      const convertedAmount = convertCurrency(
        initialRate,
        targetRate,
        initialAmount,
      );
      setConvertedAmount(convertedAmount);
    } else {
      setShowErrorMessage(true);
    }
  }

  const fetchRateIfNotExists = async (currencyCode: string) => {
   if (!currencyRates.has(currencyCode)) {
     const currencyRate = await fetchCurrencyRate(currencyCode);
     const newCurrencyRates = new Map(Array.from(currencyRates));

     newCurrencyRates.set(currencyCode, currencyRate["rateToBaseCurrency"]);
     setCurrencyRates(newCurrencyRates);
    }
  }

  const handleChange = (
    e: ChangeEvent<HTMLSelectElement>,
    setterFunction: React.Dispatch<any>,
    ) => {
    setterFunction(e.target.value);
    fetchRateIfNotExists(e.target.value);
  }

  return (
    <div className="pure-form pure-form-aligned">
      <h3>Currency Converter</h3>
      <div className="pure-control-group">
        <label htmlFor="initial-amount">Initial Amount</label>
        <input
          id="initial-amount"
          className="pure-u-1-5"
          type="number"
          step="0.01"
          min="0"
          required
          onChange={e => setInitialAmount(e.target.value)}
        />
      </div>
      <div className="pure-control-group">
        <CurrencyDropdown
          id="initial-currency"
          labelText="Initial Currency"
          currencyCodes={currencyCodes}
          onChange={e => handleChange(e, setInitialCurrency)}
        />
      </div>
      <div className="pure-control-group">
        <CurrencyDropdown
          id="target-currency"
          labelText="Target Currency"
          currencyCodes={currencyCodes}
          onChange={e => handleChange(e, setTargetCurrency)}
        />
      </div>
      <div className="pure-control-group">
        <label htmlFor="conversion-rate">Conversion Rate</label>
        <input
          className="pure-u-1-5"
          id="conversion-rates"
          type="text"
          value={conversionRate}
          readOnly={true}
        />
      </div>
      <div className="pure-control-group">
        <label htmlFor="amount-after-conversion">Amount after Conversion</label>
        <input
          className="pure-u-1-5"
          id="amount-after-conversion"
          type="text"
          value={convertedAmount}
          readOnly={true}
        />
      </div>
      <div className="pure-controls">
        <button 
          onClick={convert}
          className="pure-button pure-button-primary"
        >Calculate
        </button>
      </div>
      {showErrorMessage && <p className="pure-controls danger" >Error! The form has missing data</p>}
    </div>
  );
}
