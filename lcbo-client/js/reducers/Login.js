import {
	AUTHED,
	AUTH_FAILED,
	CREATE_USER,
	CREATE_USER_FAILED,
	LOGOUT,
	UPDATED_HISTORY,
	encodePassword
} from '../Actions';

const SESSION_LENGTH_MS = 3600000;

export default function login(state={}, action) {
	switch(action.type) {
		case AUTHED:
			setAuthCookie(action.token);
			return {
				user: action.user,
				token: action.token,
				history: action.history
			};
		case LOGOUT:
		case CREATE_USER_FAILED:
		case AUTH_FAILED:
			expireAuthCookie();
			// TODO: store some flag to distinguish between
			// lack of auth vs failed auth
			return {
				user: '',
				token: '',
				history: []
			};
		case UPDATED_HISTORY:
			return {
				...state,
				history: action.history
			};
		default:
			return state;
	}
}

function setAuthCookie(token) {
	document.cookie = 'token=' + token + '; expires=' + new Date(Date.now() + SESSION_LENGTH_MS).toUTCString();
}

function expireAuthCookie() {
	document.cookie = 'token=; expire=' + new Date(0).toUTCString();
}
