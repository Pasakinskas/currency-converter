import React, { ChangeEvent } from 'react';

import { CurrencyDropdown } from "../CurrencyDropdown/CurrencyDropdown";
import {convertCurrency, fetchCurrencyCodes, fetchCurrencyRate} from "../../services/CurrencyConversion";

export function CurrencyInputForm() {
  const [initialCurrency, setInitialCurrency] = React.useState("");
  const [targetCurrency, setTargetCurrency] = React.useState("");
  const [initialAmount, setInitialAmount] = React.useState();
  const [convertedAmount, setConvertedAmount] = React.useState();

  const [currencyCodes, setCurrencyCodes] = React.useState([]);
  const [currencyRates] = React.useState(new Map());

  React.useEffect(() => {
    const asyncGetCurrencyCodes = async () => {
      setCurrencyCodes(await fetchCurrencyCodes())
    }
    asyncGetCurrencyCodes();
  }, []);

  const convert = () => {
    const convertedAmount = convertCurrency(
      currencyRates.get(initialCurrency)["rateToBaseCurrency"],
      currencyRates.get(targetCurrency)["rateToBaseCurrency"],
      initialAmount,
    );
    setConvertedAmount(convertedAmount);
  }

  const fetchRateIfNotExists = async (currencyCode: string) => {
   if (!currencyRates.has(currencyCode)) {
     const currencyRate = await fetchCurrencyRate(currencyCode);
     currencyRates.set(currencyCode, currencyRate);
   }
  }

  const handleChange = (
    e: ChangeEvent<HTMLSelectElement>,
    setterFunction: React.Dispatch<any>,
    ) => {
    fetchRateIfNotExists(e.target.value);
    setterFunction(e.target.value);
  }

  return (
    <div className="pure-form pure-form-aligned">
      <div className="pure-control-group">
        <label htmlFor="aligned-name">Initial Amount</label>
        <input
          className="pure-u-1-5"
          type="number"
          step="0.01"
          min="0"
          defaultValue="0"
          placeholder="Initial amount"
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
    </div>
  );
}