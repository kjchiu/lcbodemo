function request(method, url) {
	var xhr = new XMLHttpRequest();
	xhr.open(method, url, true);

	return new Promise(function prosmified(resolve, reject) {

		xhr.onreadystatechange = function onReadyStateChange() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				if (xhr.status === 200) {
					resolve(xhr);
				} else {
					reject(xhr);
				}
			}
		}

		xhr.send();
	});

};


class Lcbo {
	find(query) {
		return request('GET', 'rest/product?q=' + encodeURIComponent(query)).then(function done(xhr) {
			var result = JSON.parse(xhr.response);
			return Promise.resolve(result);
		});
	}
}

export default new Lcbo();

