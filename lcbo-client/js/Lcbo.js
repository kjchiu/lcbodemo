import request from './Network';

class Lcbo {
	find(query, page) {
		var url = 'rest/product?q=' + encodeURIComponent(query);
		if (page) {
			url += '&page=' + page;
		}
		console.log(query, page, '->', url);
		return request('GET', url).then(function done(xhr) {
			var result = JSON.parse(xhr.response);
			return Promise.resolve(result);
		});
	}
}

export default new Lcbo();

