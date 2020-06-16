import { config } from "../config";

export async function fetchCurrencyCodes() {
  const res = await fetch(`${config.API_URL}/currencies`);
  return res.json();
}

export async function fetchCurrencyRate(currencyCode: string) {
  const res = await fetch(`${config.API_URL}/currency-rates/${currencyCode}`);
  return res.json();
}

export async function fetchHistoricRates(targetCurrency: string, dateFrom: string, dateTo: string) {
  const res = await fetch(`http://localhost:8080/historic-rates/${targetCurrency}?start=${dateFrom}&end=${dateTo}`);
  return res.json();
}