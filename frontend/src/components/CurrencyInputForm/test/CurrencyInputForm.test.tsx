import React from "react";
import TestRenderer from 'react-test-renderer';

import { CurrencyInputForm } from "../CurrencyInputForm";

describe("currencyInputForm", () => {
  test("renders correctly", () => {
    const inputForm = <CurrencyInputForm />
    const renderedInputForm = TestRenderer
      .create(inputForm)
      .toJSON();
 
    expect(renderedInputForm).toMatchSnapshot();
  });
});
