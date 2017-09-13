import lcbo from './Lcbo';

export const QUERY_PRODUCTS = 'QUERY_PRODUCTS';
export const RECEIVE_PRODUCTS = 'RECEIVE_PRODUCTS';

/**
 * Signal product query initiated
 * @param {String} query
 * @param {Number} page
 */
export function queryProducts(query, page) {
	page |= 1;
	return dispatch => {
		dispatch({
			type: QUERY_PRODUCTS,
			query,
			page
		});

		lcbo.find(query, page).then(function received(result) {
			dispatch({
				type: RECEIVE_PRODUCTS,
				result
			});
		});
	};
}
