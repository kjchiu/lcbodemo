import lcbo from './Lcbo';
import request from './Network';
import base64js from 'base64-js';

export const QUERY_PRODUCTS = 'QUERY_PRODUCTS';
export const RECEIVE_PRODUCTS = 'RECEIVE_PRODUCTS';

/**
 * Signal product query initiated
 * @param {String} query
 * @param {Number} page
 */
export function queryProducts(query, page) {
	page = page || 1;
	return dispatch => {
		dispatch({
			type: QUERY_PRODUCTS,
			query,
			page
		});

		lcbo.find(query, page).then(function received(result) {
			dispatch({
				type: RECEIVE_PRODUCTS,
				query,
				result
			});
		});
	};
}

export const AUTHED = 'AUTHED';
export const AUTH_FAILED = 'AUTH_FAILED';

/**
 * Dispatch AUTH action
 * @param {Function} dispatch
 * @param {String} user
 * @param {String} token
 */
function authenticated(dispatch, user, token) {
	dispatch({
		type: AUTHED,
		user,
		token
	});
}

/**
 * Authenticate with token
 * @param {String} token
 */
export function auth(token) {

	function failed() {
		return {
			type: AUTH_FAILED
		};

	}
	if (! token) {
		return failed();
	}

	return dispatch => {
		request('POST', '/rest/user', {
			"Auth-Token": token
		}).then(function done(xhr) {
			var user = xhr.response;
			if (! user || ! user.length) {
				dispatch(failed());
			} else {
				authenticated(dispatch, user, token);
			}
		});
	};
}

export const CREATE_USER = 'CREATE_USER';
export const CREATE_USER_FAILED = 'CREATE_USER_FAILED';
export function createUser(user, password) {
	var headers = addBasicAuth({}, user, password);
	return dispatch => {
		request('PUT', '/rest/user', headers).then(function done(xhr) {
			var token = xhr.response;
			if (! token) {
				dispatch({
					type: 'CREATE_USER_FAILED'
				});
				return;
			}
			authenticated(dispatch, user, token);
		});
	};
}

export const LOGIN = 'LOGIN';
export function login(user, password) {

	var headers = addBasicAuth({}, user, password);
	return dispatch => {
		request('GET', '/rest/user', headers).then(function done(xhr) {
			var token = xhr.response;
			authenticated(dispatch, user, token);
		});
	}

}

export const LOGOUT = 'LOGOUT';
export function logout() {
	return {
		type: 'LOGOUT'
	}
}

export function base64encode(str) {
	var bytes = new TextEncoder('utf-8').encode(str);
	return base64js.fromByteArray(bytes);
}

function addBasicAuth(headers = {}, user, password) {
	var credentials = user + ':' + password;
	headers.Authorization = 'Basic ' + base64encode(credentials);
	return headers;
}


