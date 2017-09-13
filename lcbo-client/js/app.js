import Bootstrap from 'bootstrap/dist/css/bootstrap.min.css';

import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import thunkMiddleware from 'redux-thunk';
import { createLogger } from 'redux-logger';

import Network from './Network';
import lcbo from './Lcbo';
import App from './components/App.jsx';

import products from './reducers/Products';
import login from './reducers/Login';
import {
	applyMiddleware,
	combineReducers,
	createStore
} from 'redux';

import { auth }from './Actions';

let reducer = combineReducers({
	products,
	login
});

const store = createStore(reducer, applyMiddleware(
	thunkMiddleware,
	createLogger()
));
window.STORE = store;

//window.ACTIONS = Actions;

// send auth request
// before starting render
var matches = /token=(.+)\b/.exec(document.cookie);
if (matches && matches[1]) {
	var token = matches[1];
	store.dispatch(auth(token));
}

render(
	<Provider store={store}>
		<App />
	</Provider>,
	document.getElementById('root'));
