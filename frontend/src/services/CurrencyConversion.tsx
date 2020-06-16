import Big from 'big.js';

const ROUND_DOWN = 0;

export function convertCurrency(
  startCurrencyRate: string,
  targetCurrencyRate: string,
  initialAmount: number,
  ) {
  return Big(initialAmount)
    .div(Big(startCurrencyRate))
    .mul(Big(targetCurrencyRate))
    .round(2, ROUND_DOWN)
}

export function getConversionRate(
  startCurrencyRate: string,
  targetCurrencyRate: string,
) {
  return Big(targetCurrencyRate)
    .div(Big(startCurrencyRate))
    .round(6, ROUND_DOWN);
}
