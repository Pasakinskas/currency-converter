import Big from 'big.js';
import { convertCurrency, getConversionRate } from "../CurrencyConversion";

describe("convertCurrency", () => {
  test("should convert currencies correctly", () => {
    const result = convertCurrency("1.05", "1.25", 100);
    expect(result).toBeInstanceOf(Big);
    expect(result.toString()).toBe("119.04");
  });

  test("should return same value if rates are the same", () => {
    const result = convertCurrency("1.00000", "1.000000", 100);
    expect(result.toString()).toBe("100")
  });

  test("should round down to two spaces", () => {
    const result = convertCurrency("1.12345", "1.000000", 100);
    expect(result.toString()).toBe("89.01");
  });
});

describe("getConversionRate", () => {
  test("calculates correctly and rounds to six spaces after comma", () => {
    const result = getConversionRate("0.3", "1.000");
    expect(result.toString()).toBe("3.333333")
  });
});
