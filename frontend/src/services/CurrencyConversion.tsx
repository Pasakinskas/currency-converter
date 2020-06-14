import Big, { RoundingMode } from 'big.js';

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
