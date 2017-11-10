/* jshint -W041 */
var DEBUG = false,
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
	//closeSecurityAlert,
	navigateToAuthenticationScreen,
	fillLoginForm,
	submitAuthenticationForm,
	waitMainPageLoaded,
	readBalance,
	navigateToDownloadPage,
	fillDownloadForm,
	listDownloadFiles,
	prepareExportCommands,
	exit
], 0);

function getCredentialsFromSystemKeychain(callback) {
	getCredentials("cmb", function(login, password) {
		credentials.login = login;
		credentials.password = password;
		callback();
	});
}

function openBankWebSiteHomePage(callback) {
	page.open('https://www.cmb.fr/banque/assurance/credit-mutuel/web/j_6/accueil', function(status) {
		if (status !== "success") {
			console.log("Unable to access network");
		} else {
			waitFor(function() {
				return page.evaluate(function() {
					return jQuery("#connexion-bouton > a").is(":visible");
				});
			}, function() {
				callback();
			});
		}
	});
}


function closeSecurityAlert(callback) {

	waitFor(function() {
		return page.evaluate(function() {
			return jQuery("a[class=button]:contains('Fermer')").is(":visible");
		});
	}, function() {
		page.evaluate(function() {
			jQuery("a[class=button]:contains('Fermer')").click();
		});
		callback();
	});
}

function navigateToAuthenticationScreen(callback) {

	page.evaluate(function() {
		jQuery("#connexion-bouton > a").click();
	});

	waitFor(function() {
		return page.evaluate(function() {
			return jQuery("#identifiant").is(":visible");
		});
	}, function() {
		callback();
	});
}


function fillLoginForm(callback) {

	page.evaluate(function(login, pass) {
		jQuery("#identifiant").val(login);
		//jQuery("#password").val(pass);
	}, credentials.login, credentials.password);

	page.sendEvent('keypress', page.event.key.Tab);

	page.evaluate(function(login, pass) {
		//jQuery("#identifiant").val(login);
		jQuery("#password").val(pass);
	}, credentials.login, credentials.password);

	callback();

}


function submitAuthenticationForm(callback) {

	page.evaluate(function() {
		document.querySelector("#formPassword > div.wrap-btn.wrap-btn-1x > button").click();
	});

	setTimeout(callback, 1000);
}

function waitMainPageLoaded(callback) {

	waitFor(
		function() {
			return page.evaluate(function() {
				try {
					if (document.querySelectorAll("#principal")[0].offsetHeight > 600) return true;
				} catch (e) {
					// console.log(e);
					return false;
				}
			});
		},
		function() {
			setTimeout(callback, 1000);
		},
		null, 20000
	);
}

function readBalance(callback) {
	store.balance = page.evaluate(function() {
		var res = [];
		var nodes = document.querySelectorAll("div.entete>div>div:nth-child(2)>div:nth-child(2)");
		for (var i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			res.push(node.innerText.trim().replace(/[ € ]*/g, '').replace(',', '.'));
		}

		var result = [];
		var labels = document.querySelectorAll("div.entete>div>div:nth-child(1)>div:first-child");
		for (var j = 0; j < labels.length; j++) {
			var label = labels[j];
			var key = label.innerText.trim();
			result.push({ "compte": key, "solde": res[j] });
		}
		return result;
	});

	callback();
}


function navigateToDownloadPage(callback) {

	// Get JSessionID cookie value from disk
	var content = fs.read('tmp/cmb-cookies');
	var re = /(JSESSIONID=[^;]*;)/g;
	var search = re.exec(content);
	var jsessionID = search[1];
	globalJSessionID = jsessionID;
	if (DEBUG) {
		console.log("Found JSessionID : " + globalJSessionID);
	}

	page.evaluate(function() {
		window.location.hash = "#TelechargementOperationPlace:";
	});


	waitFor(
		function() {
			return page.evaluate(function() {
				try {
					if (document.querySelectorAll("a.gwt-Anchor.label")[0].offsetWidth > 0) return true;
				} catch (e) {
					console.log(e);
					return false;
				}
			});
		},
		function() {
			callback();
		}, null, 8000
	);
}


function fillDownloadForm(callback) {

	page.evaluate(createClickElementInDom);
	page.evaluate(selectAccountsAndSearchForOperations);
	setTimeout(callback, 1000);

}


function listDownloadFiles(callback) {
	var files = [];

	page.onResourceRequested = function(requestData, networkRequest) {
		//console.log('Request (#' + requestData.id + '): ' + JSON.stringify(requestData));
		files.push(requestData.url);
	};

	page.onResourceReceived = function(response) {
		//console.log(JSON.stringify(response));
	};

	waitFor(
		function() {
			return page.evaluate(
				function() {

					try {
						var node = document.querySelectorAll("div.actif>div>a");


						// Text objects referencing found operations
						var textNodes = document.getElementsByClassName("espace libelle_2");

						var visible = true;
						for (var i in node) {
							try {
								if ("object" == typeof node[i]) {
									var height = node[i].offsetHeight;
									console.log("element " + i, height);

									// Check if there is no operations
									if (i > 0) {
										var textNode = textNodes[i - 1];
										//console.log(textNode.innerHTML);

										// Hack size validation
										if (textNode != null && textNode.innerHTML.indexOf("aucune") > -1) {
											height = 100;
										}
									}

									visible = visible && height > 0;
								}
							} catch (e) {
								console.log("ERROR: ", e);
							}
						}

						var returnVal = visible && node.length > 3;
						console.log("SIZE : " + returnVal);
						return returnVal;
					} catch (e) {
						return false;
					}
				});
		},
		function() {

			page.evaluate(function() {
				var a = document.querySelectorAll("div.actif>div>a");
				for (var k in a) {
					if ("object" == typeof a[k]) {
						if (k > 0) {

							// Do not download if no operation
							if (a[k].offsetHeight > 0) {
								console.log("CLICK OBJECT");
								var e = document.createEvent('MouseEvents');
								e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
								a[k].dispatchEvent(e);
							} else {
								console.log("Empty file");
							}
						}
					}
				}
			});
			store.files = files;
			callback();
		}, null,
		5000);

}



function prepareExportCommands(callback) {
	var files = store.files;
	if (DEBUG) {
		console.log("found " + files.length + " files");
		for (var f in files) {
			console.log("  " + files[f]);
		}
	}

	var comptes = ["CMB", "LB", "PEL"];
	var urls = "";
	var counter = 0;
	for (var index in files) {

		if (files[index].indexOf("operationsDownload") > 0) {
			urls += [
				"/usr/bin/curl",
				"-s",
				"'" + files[index] + "'",
				"-H", "'Referer: https://www.cmb.fr/banque/assurance/credit-mutuel/web/yc_8462/prive'",
				"-H", "'Cookie:" + globalJSessionID + "'",
				">",
				"~/Downloads/RELEVE_" + new Date().format("yyyy_mm_dd") + "_" + comptes[counter++] + "_last5weeks" + ".csv"
			].join(" ") + "\n";
		} else {
			//debug("not a download file");
		}
	}

	urls += [
		"echo",
		"'" + JSON.stringify(store.balance) + "'",
		">",
		"~/Downloads/CMB_BALANCE.json"
	].join(" ") + "\n";

	fs.write("tmp/cmb-files", urls, 'w');

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


var selectAccountsAndSearchForOperations = function() {
	// Select all accounts
	for (var i = 0; i < 100; i++) {
		var a = document.getElementById("gwt-uid-" + i);
		if (a != null) { /* jshint ignore:line */
			if (a.getAttribute("type") == "checkbox") {
				a.click();
				break;
			}
		}
	}
	// Click search button
	document.querySelectorAll("a.gwt-Anchor.label")[0].click();
};
