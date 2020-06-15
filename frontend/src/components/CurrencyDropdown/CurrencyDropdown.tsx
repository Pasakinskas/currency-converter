import React, { ChangeEvent } from 'react';

type currencyDropdownProps = {
  id: string;
  className?: string;
  currencyCodes: Array<string>;
  onChange: (event: ChangeEvent<HTMLSelectElement>) => void;
  labelText: string
}

export function CurrencyDropdown(props: currencyDropdownProps) {
  const { 
    id, 
    currencyCodes,
    labelText,
    onChange: handleChange
  } = props;

  return (
    <React.Fragment>
      <label htmlFor={id}>{labelText}</label>
      <select id={id} className="pure-u-1-5" onChange={handleChange}>
        {currencyCodes
          .sort()
          .map(code => <option key={code}>{code}</option>)
        }
      </select>
    </React.Fragment>
  )
}
