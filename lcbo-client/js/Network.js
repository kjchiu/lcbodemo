export default function request(method, url, headers) {
	var xhr = new XMLHttpRequest();
	xhr.open(method, url, true);
	if (headers) {
		for(var header in headers) {
			// no reasonable expectation
			// passing anything but a map should work
			xhr.setRequestHeader(header, headers[header]);
		}
	}

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

