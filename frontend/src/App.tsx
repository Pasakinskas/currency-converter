import React from 'react';
import './App.css';
import {CurrencyInputForm} from './components/CurrencyInputForm/CurrencyInputForm';
import "purecss";
import { CurrencyChart } from './components/CurrencyChart/CurrencyChart';

function App() {
  return (
    <div className="App">
      <CurrencyInputForm />
      <CurrencyChart />
    </div>
  );
}

export default App;
