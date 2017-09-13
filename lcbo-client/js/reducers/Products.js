import {
	QUERY_PRODUCTS,
	RECEIVE_PRODUCTS
} from '../Actions.js';

import Lcbo from '../Lcbo';


var DEFAULT_STATE = {
	querying: false,
	items: [],
	pageInfo: {}
}

export function products(state = {}, action) {
	switch(action.type) {
		case QUERY_PRODUCTS:
			return {
				...state,
				querying: true,
				query: action.query
			};

		case RECEIVE_PRODUCTS:
			var result = action.result;
			return {
				querying: false,
				items: result.items,
				pageInfo: result.pageInfo
			};
		default:
			return state;

	}
}
