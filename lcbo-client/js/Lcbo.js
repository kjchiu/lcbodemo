import request from './Network';

class Lcbo {

	/**
	 * Find products that match query
	 * @param {String} query
	 * @param {Number} page
	 * @param {String} @optional token auth token
	 */
	find(query, page, token) {
		var url = 'rest/product?q=' + encodeURIComponent(query);
		if (page) {
			url += '&page=' + page;
		}
		var headers;
		if (token) {
			headers = {
				'Auth-Token': token
			}
		}
		return request('GET', url, headers).then(function done(xhr) {
			var result = JSON.parse(xhr.response);
			return Promise.resolve(result);
		});
	}
}

export default new Lcbo();

