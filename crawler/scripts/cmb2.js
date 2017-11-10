/* jshint -W041 */
var DEBUG = true,
	fs = require('fs'),
	system = require('system'),
	jsCookies = require('./cookieformat').jsCookies,
	common = require('./common.js'),
	getCredentials = common.getCredentials,
	waitFor = common.waitFor,
	createClickElementInDom = common.createClickElementInDom,
	processSequence = common.processSequence,
	credentials = {},
	store = {};

page = require('webpage').create();

page.onConsoleMessage = function(msg, lineNum, sourceId) {
	if (DEBUG) {
		console.log(">>" + msg);
	}
};

page.onError = function(msg) {
	if (DEBUG)
		console.log('ERROR: ' + msg);
};

page.settings.loadImages = DEBUG;

processSequence([
	getCredentialsFromSystemKeychain,
	navLogin,
	fillLoginForm,
	validateData,
	exit
], 0);

function getCredentialsFromSystemKeychain(callback) {
	getCredentials("cmb", function(login, password) {
		credentials.login = login;
		credentials.password = password;
		callback();
	});
}

function navLogin(callback) {
	page.open('https://mon.cmb.fr/auth/login', function(status) {
		if (status !== "success") {
			console.log("Unable to access network");
		} else {
			waitFor(function() {
				return page.evaluate(function() {
					return document.querySelector("button.btn-submit") != undefined;
				});
			}, function() {
				callback();
			});
		}
	});
}

function captureAjaxResponsesToConsole() {
	page.evaluate(function() {
		(function(open) {
			// Restore original console.log
			var i = document.createElement('iframe');
			i.style.display = 'none';
			document.body.appendChild(i);
			window.console = i.contentWindow.console;
			// Capture xhr response
			XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {
				this.addEventListener("readystatechange", function() {
					if (this.readyState === 4) {
						if (window.xhrCapture == undefined) {
							window.xhrCapture = [];
						}
						window.xhrCapture.push({ 'response': this.responseText, 'url': url });
					}
				}, false);
				open.call(this, method, url, async, user, pass);
			};
		})(XMLHttpRequest.prototype.open);
		return 1;
	});
}

function fillLoginForm(callback) {

	page.onResourceRequested = function(requestData, networkRequest) {
		if (requestData.url == "https://mon.cmb.fr/domiapi/oauth/json/edr/infosPerson") {
			captureAjaxResponsesToConsole();
		}

	};

	page.evaluate(createClickElementInDom);
	page.evaluate(function(credentials) {

		document.querySelector("#userLogin").value = credentials.login;
		document.querySelector("#userPassword").value = credentials.password;
		document.querySelector("button.btn-submit").click();

	}, credentials);

	var count = 0;

	waitFor(
		function() {
			return page.evaluate(function() {
				var synthesecomptes = false;
				var detailcompte = false;
				if (window.xhrCapture) {
					for (var i in window.xhrCapture) {
						var e = window.xhrCapture[i];

						if (e.url == "/domiapi/oauth/json/accounts/synthesecomptes") {
							synthesecomptes = true;
							window.__synthesecomptes = e.response;
						}
						if (e.url == "/domiapi/oauth/json/accounts/detailcompte") {
							detailcompte = true;
							window.__detailcompte = e.response;
						}
					}
				}
				return synthesecomptes && detailcompte;
			});
		},
		function() {

			store.synthese = page.evaluate(function() {
				return window.__synthesecomptes;
			});
			store.details = page.evaluate(function() {
				return window.__detailcompte;
			});

			callback();
		}
	);
}

function validateData(callback) {

	console.log(store.details);

	callback();
}

function exit() {
	if (page) {
		page.close();
	}
	setTimeout(function() {
		phantom.exit();
	}, 0);
	phantom.onError = function() {};
}
