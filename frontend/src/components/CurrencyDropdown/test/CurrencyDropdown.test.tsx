import React from "react";
import TestRenderer from 'react-test-renderer';
import { CurrencyDropdown } from "../CurrencyDropdown";
import { mount } from "enzyme";

describe("currencyDropdown", () => {
  test("Renders correctly", () => {
    const testDropdown = <CurrencyDropdown 
      id="test-id"
      labelText="mock-dropdown"
      onChange={e => e}
      currencyCodes={["EUR", "USD", "GBP"]}
      />
    const dropdown = TestRenderer
      .create(testDropdown)
      .toJSON();
 
    expect(dropdown).toMatchSnapshot();
  });

  test("displays initial to-dos", () => {
    const testDropdown = <CurrencyDropdown 
    id="test-id"
    labelText="mock-dropdown"
    onChange={e => e}
    currencyCodes={["EUR", "USD", "GBP"]}
    />
    const wrapper = mount(testDropdown);
    expect(wrapper.find("option")).toHaveLength(4);
  });
});