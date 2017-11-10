/* jshint -W041 */
var DEBUG = true,
	page = require('webpage').create(),
	fs = require('fs'),
	system = require('system'),
	dateformat = require('./dateformat').dateformat,
	jsCookies = require('./cookieformat').jsCookies,
	globalJSessionID = null,
	common = require('./common.js'),
	getCredentials = common.getCredentials,
	waitFor = common.waitFor,
	createClickElementInDom = common.createClickElementInDom,
	processSequence = common.processSequence,
	credentials = {},
	store = {};

page.onConsoleMessage = function(msg, lineNum, sourceId) {
	if (DEBUG)
		console.log('CONSOLE: ' + msg + ' (from line #' + lineNum + ' in "' + sourceId + '")');
};

page.onError = function(msg) {
	if (DEBUG)
		console.log('ERROR: ' + msg);
};

page.settings.loadImages = false;

processSequence([
	getCredentialsFromSystemKeychain,
	openBankWebSiteHomePage,
	fillLoginForm,
	loadTransactions,
	extractTransactions,
	loadCredits,
	exportData,
	exit
], 0);

function getCredentialsFromSystemKeychain(callback) {
	getCredentials("edenred", function(login, password) {
		credentials.login = login;
		credentials.password = password;
		callback();
	});
}

function openBankWebSiteHomePage(callback) {
	page.open('https://www.myedenred.fr/ExtendedAccount/Logon', function(status) {
		if (status !== "success") {
			console.log("Unable to access network");
		} else {
			waitFor(function() {
				return page.evaluate(function() {
					return document.querySelectorAll(".btn-red").length > 0;
				});
			}, function() {
				callback();
			});
		}
	});
}

function fillLoginForm(callback) {

	page.evaluate(function(login, pass) {
		document.querySelector("input#Email").value = login;
		document.querySelector("input#Password").value = pass;
		document.querySelector("input.btn.btn-red.submit").click();
	}, credentials.login, credentials.password);

	waitFor(
		function() {
			return page.evaluate(function() {
				return document.querySelector("p.link a") != null;
			});
		},
		function() {
			callback();
		},
		null,
		10000
	);

}

function loadTransactions(callback) {
	page.evaluate(function() {
		return document.querySelector("p.link a").click();
	});

	waitFor(
		function() {
			return page.evaluate(function() {
				return document.querySelector("table") != null;
			});
		},
		function() {
			callback();
		},
		function() {
			// On faile
			console.log("failed");
		},
		20000
	);
}

function extractTransactions(callback) {

	var tableContent = page.evaluate(function() {
		var operations = [];
		var rows = document.querySelector("table").rows;
		for (var i = 0; i < rows.length; i++) {
			var content = rows[i].innerText
				.replace(/\n/g, ";")
				.replace(/\t+/g, ";")
				.replace(/transaction confirmée/, "TRANSACTION_OK")
				//.replace(/[ ]*-[ ]*/g, ";")
				.replace(/€/g, "");
			operations.push(content);
		}

		return operations;
	});

	store.ops = tableContent;
	callback();
}

function loadCredits(callback) {

	page.evaluate(function() {
		document.querySelector("a#ui-id-2.ui-tabs-anchor").click();
	});


	var tableContent = page.evaluate(function() {
		var operations = [];
		var rows = document.querySelectorAll("table.table-transaction")[1].rows;
		for (var i = 0; i < rows.length; i++) {
			var content = rows[i].innerText
				.replace(/\n/g, ";")
				.replace(/\t+/g, ";")
				.replace(/transaction confirmée/, "TRANSACTION_OK")
				//.replace(/[ ]*-[ ]*/g, ";")
				.replace(/€/g, "");
			operations.push(content);
		}

		return operations;
	});

	store.ops = store.ops.concat(tableContent);

	callback();


}

function exportData(callback) {
	var tableContent = store.ops;

	// console.log(JSON.stringify(exportContent));
	fs.write("tmp/edenred.csv", tableContent.join('\n'), function(err) {
		console.log(err);
	});

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
