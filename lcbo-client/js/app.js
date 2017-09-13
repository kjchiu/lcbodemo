import Bootstrap from 'bootstrap/dist/css/bootstrap.min.css';

import React from 'react';
import ReactDOM from 'react-dom';
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

let reducer = combineReducers({
	products,
	login
});

const store = createStore(reducer, applyMiddleware(
	thunkMiddleware,
	createLogger()
));
window.STORE = store;

import * as Actions from './Actions';
window.ACTIONS = Actions;
//store.dispatch(queryProducts('beer', 1));

store.dispatch(Actions.createUser('hello', 'there'));
//store.dispatch(Actions.login('hello', 'there'));

ReactDOM.render(<App />, document.getElementById('root'));
console.log('hi');
