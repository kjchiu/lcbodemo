import React from 'react';
import ReactDOM from 'react-dom';
import Network from './Network';
import Lcbo from './Lcbo';
import App from './components/App.jsx';

import Bootstrap from 'bootstrap/dist/css/bootstrap.min.css';

ReactDOM.render(<App />, document.getElementById('root'));
window.NETWORK = Network;
console.log('hi');
