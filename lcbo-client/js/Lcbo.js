import request from './Network';

class Lcbo {
	find(query) {
		return request('GET', 'rest/product?q=' + encodeURIComponent(query)).then(function done(xhr) {
			var result = JSON.parse(xhr.response);
			return Promise.resolve(result);
		});
	}
}

export default new Lcbo();

