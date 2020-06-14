import Big from 'big.js';

export function convertCurrency(
  startCurrencyRate: string,
  targetCurrencyRate: string,
  initialAmount: number,
  ) {
  const ROUND_DOWN = 0;
  return new Big(initialAmount)
    .div(Big(startCurrencyRate))
    .mul(Big(targetCurrencyRate))
    .round(2, ROUND_DOWN)
}

export async function fetchCurrencyCodes() {
  const res = await fetch("http://localhost:8080/currencies");
  return res.json();
}

export async function fetchCurrencyRate(currencyCode: string) {
  const res = await fetch(`http://localhost:8080/currency-rates/${currencyCode}`);
  return res.json();
}