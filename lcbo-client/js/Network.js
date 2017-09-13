define(function (require, exports, module) {

	function Network() {
	}

	Network.prototype.request = function request(method, url) {
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

	module.exports = Network;

});
