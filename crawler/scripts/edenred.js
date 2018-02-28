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

			var content = rows[i].innerText;
			content = content.replace(/\t/g, "");
			var cLines = content.split('\n')

			
			// Parse first line : 19h33 - ABALONE SUSHI
			var dateParts = cLines[0].split('/');
			var dd = dateParts[0];
			var mm = dateParts[1];

			var year = new Date().getFullYear();
			var month = new Date().getMonth() + 1; // Jan is 0
			var lineMonth = parseInt(mm);
			// Only 6 month are available on the edenred website
			if (lineMonth > month){
				year = year - 1;
			}
			
			// Parse line 2: 19h33 - ABALONE SUSHI
			var line2 = cLines[1];

			// Parse line 3: HALLES SAINT FRANCOIS 29000 QUIMPER
			var line3 = cLines[2];

			// Parse line 4: Transaction status
			var line4 = cLines[3].replace(/transaction confirmée/, "TRANSACTION_OK");

			// Parse line 5 : - 16,00€
			var line5 = cLines[4].replace(/€/g, "").replace(/ /g, '').replace(/,/g, '.').replace(/-/g, '').trim();
			
			var timestamp = new Date(year, mm, dd).getTime();

			var lineData = {
				compte: "EDENRED.TICKET_RESTO",
				date_valeur: timestamp,
				date_operation: timestamp,
				libelle: line2.trim() + " " +  line3.trim(),
				credit: 0,
				debit: line5,
				status: line4
			};

			operations.push(lineData);
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
			var content = rows[i].innerText;
			// console.log(content);
			// line 1: 30/08
			// line 2: 04h54 - XXXXXXXX
			// line 3: transaction confirmée  2015
			// line 4: + 000,00€
			// line 5: 00 x 0,00€
			
			content = content.replace(/\t/g, "");
			var cLines = content.split('\n')

			
			// Parse first line : 19h33 - ABALONE SUSHI
			var dateParts = cLines[0].split('/');
			var dd = dateParts[0];
			var mm = dateParts[1];
			var year = new Date().getFullYear();
			var month = new Date().getMonth() + 1; // Jan is 0
			var lineMonth = parseInt(mm);
			// Only 6 month are available on the edenred website
			if (lineMonth > month){
				year = year - 1;
			}
			
			// Parse line 2: 19h33 - ABALONE SUSHI
			var line2 = cLines[1];

			// Parse line 3: transaction confirmée  2015
			var line3 = cLines[2];
			if (line3.indexOf('transaction confirmée') > -1 ) {
				line3 = "TRANSACTION_OK"
			}	

			// Parse line 4 : + 16,00€
			var line4 = cLines[3].replace(/€/g, "").replace(/ /g, '').replace(/,/g, '.').replace(/\+/g, '').trim();
			
			var timestamp = new Date(year, mm , dd).getTime();

			var lineData = {
				compte: "EDENRED.TICKET_RESTO",
				date_valeur: timestamp,
				date_operation: timestamp,
				libelle: line2.trim(),
				debit: 0,
				credit: line4,
				status: line3.trim()
			};

			operations.push(lineData);
		}

		return operations;
	});

	store.ops = store.ops.concat(tableContent);

	callback();


}

function exportData(callback) {
	var tableContent = store.ops;

	var data = {
		compte: "EDENRED.TICKET_RESTO",
		format_version: 1,
		date: new Date().getTime(),
		operations: tableContent
	};

	//console.log(JSON.stringify(exportContent));
	fs.write("tmp/edenred.json", JSON.stringify(data, null, 2), function(err) {
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
