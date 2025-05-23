import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import 'bootstrap/dist/css/bootstrap.css';

const root = ReactDOM.createRoot(document.getElementById('root'));

document.documentElement.setAttribute('data-bs-theme','dark')
 
root.render(
  <React.StrictMode>
	  <BrowserRouter>
	    <App />
	  </BrowserRouter>
  </React.StrictMode>
);