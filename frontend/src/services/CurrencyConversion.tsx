import Big from 'big.js';

export function convertCurrency(
  startCurrencyRate: string,
  targetCurrencyRate: string,
  initialAmount: number,
  ) {
  const ROUND_DOWN = 0;
  return Big(initialAmount)
    .div(Big(startCurrencyRate))
    .mul(Big(targetCurrencyRate))
    .round(2, ROUND_DOWN)
}

export function getConversionRate(
  startCurrencyRate: string,
  targetCurrencyRate: string,
) {
  const ROUND_DOWN = 0;
  return Big(targetCurrencyRate)
    .div(Big(startCurrencyRate))
    .round(6, ROUND_DOWN);
}
