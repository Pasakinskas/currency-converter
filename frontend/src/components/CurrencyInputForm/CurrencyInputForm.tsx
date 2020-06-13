import React from 'react';
import {convertCurrency} from "../../services/CurrencyConversionService";

export function CurrencyInputForm() {
  const [initialCurrency, setInitialCurrency] = React.useState();
  const [targetCurrency, setTargetCurrency] = React.useState();
  const [initialAmount, setInitialAmount] = React.useState();
  const [convertedAmount, setConvertedAmount] = React.useState();

  const convert = () => {
    console.log(initialAmount, initialCurrency, targetCurrency);
  }
  return (
    <div className="pure-form pure-form-aligned">
      <div className="pure-control-group">
        <label htmlFor="aligned-name">Initial Amount</label>
        <input
          className="pure-u-1-5"
          type="number"
          step="0.01"
          placeholder="Initial amount"
          required
          onChange={e => setInitialAmount(e.target.value)}
        />
      </div>
      <div className="pure-control-group">
        <label htmlFor="Initial Currency">Initial Currency</label>
        <select
          id="Initial Currency"
          className="pure-u-1-5"
          onChange={e => setInitialCurrency(e.target.value)}
        >
          <option>EUR</option>
          <option>GBP</option>
          <option>USD</option>
        </select>
      </div>
      <div className="pure-control-group">
        <label htmlFor="Target Currency">Target Currency</label>
        <select
          id="Target Currency"
          className="pure-u-1-5"
          onChange={e => setTargetCurrency(e.target.value)}
        >
          <option>EUR</option>
          <option>GBP</option>
          <option>USD</option>
        </select>
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
        <button onClick={convert} className="pure-button pure-button-primary">Calculate</button>
      </div>
    </div>
  );
}