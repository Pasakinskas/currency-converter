import React, { ChangeEvent } from 'react';
import { Line } from 'react-chartjs-2';

import { CurrencyDropdown } from "../CurrencyDropdown/CurrencyDropdown";
import { fetchCurrencyCodes, fetchHistoricRates } from '../../services/CurrencyRetrieval';

export function CurrencyChart() {
  const [currencyCodes, setCurrencyCodes] = React.useState([]);
  const [targetCurrency, setTargetCurrency] = React.useState("");
  const [dateFrom, setDateFrom] = React.useState();
  const [dateTo, setDateTo] = React.useState();

  const [historicData, setHistoricData] = React.useState();

  React.useEffect(() => {
    const asyncGetCurrencyCodes = async () => {
      setCurrencyCodes(await fetchCurrencyCodes())
    }

    asyncGetCurrencyCodes();
  }, []);

  const handleChange = (
    e: ChangeEvent<HTMLSelectElement>,
    setterFunction: React.Dispatch<any>,
    ) => {
    setterFunction(e.target.value);
  }

  const handleInputChange = (
    e: ChangeEvent<HTMLInputElement>,
    setterFunction: React.Dispatch<any>,
    ) => {
    setterFunction(e.target.value);
  }

  const fetchChartData = async () => {
    setHistoricData(await fetchHistoricRates(targetCurrency, dateFrom, dateTo));
  }

  const buildDataForChart = () => {
    interface Data {
      date: String;
      rateToBaseCurrency: String
    }
    return {
      labels: historicData.map((data: Data) => data.date),
      datasets: [
        {
          label: 'Currency rate to USD',
          fill: false,
          lineTension: 0.5,
          backgroundColor: 'rgba(75,192,192,1)',
          borderColor: 'rgba(0,0,0,1)',
          borderWidth: 2,
          data: historicData.map((data: Data) => data.rateToBaseCurrency)
        }
      ]
    }
  }

  return (
    <div className="pure-form pure-form-aligned">
      <h3>View Historic Rates</h3>
      <div className="pure-control-group">
        <CurrencyDropdown
          id="target-currency"
          labelText="Target Currency"
          currencyCodes={currencyCodes}
          onChange={e => handleChange(e, setTargetCurrency)}
        />
      </div>
      <div className="pure-control-group">
        <label htmlFor="conversion-rate">Start Date</label>
        <input
          className="pure-u-1-5"
          id="start-date"
          type="date"
          value={dateFrom}
          onChange={e => handleInputChange(e, setDateFrom)}
        />
      </div>
      <div className="pure-control-group">
      <label htmlFor="conversion-rate">End Date</label>
        <input
          className="pure-u-1-5"
          id="end-date"
          type="date"
          value={dateTo}
          onChange={e => handleInputChange(e, setDateTo)}
        />
      </div>
      <div className="pure-controls">
        <button 
          onClick={fetchChartData}
          className="pure-button pure-button-primary"
        >Show Data
        </button>
      </div>
      {historicData && 
      <Line
          data={buildDataForChart()}
          options={{
            title:{
              display:true,
              text:'Historic Currency Data',
              fontSize:20
            },
            legend:{
              display:true,
              position:'right'
            }
          }}
        />}
    </div>
  );
}
