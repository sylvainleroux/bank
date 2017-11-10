/* global phantom, require, console */
/* jshint -W097 */

var DEBUG = false,
	system = require('system'),
	fs = require('fs'),
	page = require('webpage').create(),
	dateformat = require('./dateformat').dateformat,
	common = require('./common.js'),
	getCredentials = common.getCredentials,
	waitFor = common.waitFor,
	createClickElementInDom = common.createClickElementInDom,
	processSequence = common.processSequence,
	store = {},
	credentials = {},
	debug = function(_message) {
		if (DEBUG) {
			console.log(_message);
		}
	};

processSequence([
	getCredentialsFromSystemKeychain,
	openBankWebsiteHomePage,
	navigateToAuthenticationScreen,
	fillLoginFromAndSubmit,
	readBalance,
	navigateToAccountsList,
	navigateToAccountOperations,
	extractOperationsDetails,
	exportDataToJSONFile,
	exit
], 0);

function dummyStep(callback) {
	console.log("Debug info");
	console.log("Debug info 2");
	setTimeout(function() {
		callback();
	}, 100);
}

page.onConsoleMessage = function(msg, lineNum, sourceId) {
	if (DEBUG)
		console.log('CONSOLE: ' + msg + ' (from line #' + lineNum + ' in "' + sourceId + '")');
};

page.onError = function(msg) {
	if (DEBUG)
		console.log('ERROR: ' + msg);
};

page.settings.loadImages = false;

function getCredentialsFromSystemKeychain(callback) {
	getCredentials("bpo", function(login, password) {
		credentials.login = login;
		credentials.password = password;
		callback();
	});
}


function openBankWebsiteHomePage(callback) {
	page.open('http://www.ouest.banquepopulaire.fr/portailinternet/Pages/default.aspx', function(status) {
		if (status !== "success") {
			console.log("Unable to access network");
		} else {
			debug("> login page loaded");

			waitFor(function() {
				return page.evaluate(function() {
					return document.getElementById("ctl00_logOnLink") != undefined; /* jshint ignore:line */
				});
			}, function() {
				callback();
			});
		}
	});
}

function navigateToAuthenticationScreen(callback) {

	page.evaluate(createClickElementInDom);
	page.evaluate(function() {
		document.getElementById("ctl00_logOnLink").click();
	});

	waitFor(function() {
		return page.evaluate(function() {
			try {
				if (document.querySelector("input[value=Valider]") === undefined) {
					return false;
				}
				// console.log(document.querySelector("input[value=Valider]").offsetWidth);
				return document.querySelector("input[value=Valider]").offsetWidth > 0;
			} catch (e) {
				return false;
			}
		});
	}, function() { callback(); }, null, 10000);
}

function fillLoginFromAndSubmit(callback) {

	page.evaluate(function() {
		var login = document.getElementsByTagName('input')[0];
		//login.css("background-color", "red");
		login.click();
		login.focus();
		return login;
	});
	page.sendEvent("keypress", credentials.login + "");
	page.evaluate(function() {
		var e = document.getElementsByTagName('input')[1];
		//e.css("background-color", "red");
		e.click();
		e.focus();
		return e;
	});
	page.sendEvent("keypress", credentials.password);
	page.evaluate(function() {
		var valider = document.getElementsByTagName('input')[3];
		valider.click();
	});



	waitFor(function() {
			return page.evaluate(function() {
				var a = document.querySelector("a.SoldeCssClass");
				if (a == undefined) { /* jshint ignore:line */
					return false;
				}
				var o = a.offsetWidth;
				if (o == 0) { /* jshint ignore:line */
					// console.log("Not visible yet");
					return false;
				}
				return true;
			});
		}, function() {
			callback();
		},
		null, 30000);
}

function defineFindFrames() {
	findFrames = function(frameID, mwin) {
		var i, f;
		// Search iframes
		var list = mwin.document.getElementsByTagName("iframe");
		for (i = 0; i < list.length; i++) {
			f = list[i];
			if (f.id == frameID) {
				return f;
			}
			res = findFrames(frameID, f.contentWindow);
			if (res !== null) {
				return res;
			}
		}
		// Search frames
		list = mwin.document.getElementsByTagName("frame");
		for (i = 0; i < list.length; i++) {
			f = list[i]; // warning!
			if (f.id == frameID) {
				return f;
			}
			res = findFrames(frameID, f.contentWindow);
			if (res !== null) {
				return res;
			}
		}
		return null;
	};
}



function readBalance(callback) {

	var solde = page.evaluate(function() {
		return document.querySelector("a.SoldeCssClass").title;
	});

	solde = solde.replace(" EUR", "");
	solde = solde.replace("Solde : ", "");
	solde = solde.replace(',', '.');

	store.solde = solde;

	callback();
}

function navigateToAccountsList(callback) {

	page.evaluate(createClickElementInDom);
	page.evaluate(function() {
		document.querySelector("a[title=Comptes]").click();
	});

	waitFor(function() {
		page.render("tmp/toto.png");
		page.evaluate(defineFindFrames);
		return page.evaluate(function() {
			try {
				var frame = findFrames("applicationPanel", top);
				var mdoc = frame.contentDocument;
				return (mdoc.getElementById("btn46") != undefined); /* jshint ignore:line */
			} catch (e) {
				//console.log(e);
				return false;
			}
		});
	}, function() {
		callback();
	}, null, 20000);
}


function navigateToAccountOperations(callback) {

	setTimeout(function() {
		page.evaluate(function() {
			var frame = findFrames("applicationPanel", top);
			var mdoc = frame.contentDocument;
			var button = mdoc.getElementById("fld400");

			try {
				var e = mdoc.createEvent('MouseEvents');
				e.initMouseEvent('click', true, true, frame.contentWindow, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
				button.dispatchEvent(e);
			} catch (ex) {
				debug(ex);
			}

		});

		waitFor(function() {
			page.evaluate(defineFindFrames);
			return page.evaluate(function() {
				try {
					var frame = findFrames("applicationPanel", top);
					var mdoc = frame.contentDocument;
					return (mdoc.getElementById("btn1") != undefined); /* jshint ignore:line */
				} catch (e) {
					return false;
				}

			});

		}, function() {
			callback();
		});

	}, 1000);


}



function defineExtractOperations() {
	extractOperations = function($doc) {
		var table = $doc.getElementById("tbl1");
		var tBody = table.tBodies[0];
		var operations = [];
		var extract = function(nodes, index) {
			var node = nodes[index];
			var value = node.innerText;
			if (value != undefined) { /* jshint ignore:line */
				value = value.trim().replace(/\n/g, ' ');
			}
			return value || "";
		};

		for (i = 0; i < tBody.childNodes.length; i++) {
			tr = tBody.childNodes[i];
			//console.log(tr);
			tds = tr.childNodes;
			var op = {};
			op.date_compta = extract(tds, 1);
			op.libelle = extract(tds, 3);
			op.ref = extract(tds, 5);
			op.date_ope = extract(tds, 7);
			op.date_val = extract(tds, 9);
			op.debit = extract(tds, 11).replace(',', '.');
			op.credit = extract(tds, 13).replace(',', '.');

			operations.push(op);
		}

		return operations;
	};
}



function extractOperationsDetails(callback) {

	page.evaluate(defineFindFrames);
	page.evaluate(defineExtractOperations);
	var ops = page.evaluate(function() {
		var frame = findFrames("applicationPanel", top);
		return extractOperations(frame.contentDocument);
	});
	store.operations = ops;
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





function exportDataToJSONFile(callback) {

	var exportContent = {
		exportType: "bpo",
		content: store.operations
	};

	var balance = [{ "compte": "BPO", "solde": store.solde }];

	// console.log(JSON.stringify(exportContent));
	fs.write("tmp/BPO.json", JSON.stringify(exportContent), function(err) {
		console.log(err);
	});

	fs.write("tmp/BPO_BALANCE.json", JSON.stringify(balance), function(err) {
		console.log(err);
	});



	callback();
}
