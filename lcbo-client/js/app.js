import Bootstrap from 'bootstrap/dist/css/bootstrap.min.css';

import React from 'react';
import ReactDOM from 'react-dom';
import thunkMiddleware from 'redux-thunk';
import { createLogger } from 'redux-logger';

import Network from './Network';
import lcbo from './Lcbo';
import App from './components/App.jsx';

import { products } from './reducers/Products';
import { login } from './reducers/Login';
import {
	applyMiddleware,
	combineReducers,
	createStore
} from 'redux';

let reducer = combineReducers({
	products,
	login
});

const store = createStore(reducer, applyMiddleware(
	thunkMiddleware,
	createLogger()
));
window.STORE = store;

import { queryProducts } from './Actions';
store.dispatch(queryProducts('beer', 1));

ReactDOM.render(<App />, document.getElementById('root'));
console.log('hi');
