package com.sleroux.accountchecker.banquepopulaire;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sleroux.accountchecker.AccountChecker;
import com.sleroux.accountchecker.banquepopulaire.config.CyberplusConfig;
import com.sleroux.accountchecker.banquepopulaire.config.Payloads;
import com.sleroux.accountchecker.banquepopulaire.util.ExtractUtils;
import com.sleroux.accountchecker.banquepopulaire.util.PortalAction;
import com.sleroux.accountchecker.banquepopulaire.util.PostData;
import com.sleroux.accountchecker.banquepopulaire.util.TokenHashKey;
import com.sleroux.accountchecker.banquepopulaire.util.extractors.ContentExtractor;
import com.sleroux.accountchecker.banquepopulaire.util.extractors.HashExtractor;
import com.sleroux.accountchecker.banquepopulaire.util.extractors.TokenExtractor;
import com.sleroux.accountchecker.banquepopulaire.util.notifier.LogNotifier;
import com.sleroux.accountchecker.banquepopulaire.util.notifier.Notifier;
import com.sleroux.bank.model.fileimport.ExtractDocument;

public class Cyberplus extends CyberplusConfig implements AccountChecker {

	private Logger				logger			= Logger.getLogger(Cyberplus.class);

	private Notifier			notifier		= new LogNotifier(logger);
	private SimpleDateFormat	format			= new SimpleDateFormat("dd/MM/yyyy");
	private Properties			props			= new Properties();
	private ExtractDocument		extract;
	private ExtractUtils		extractUtils	= new ExtractUtils();

	public Cyberplus(Notifier _notifier) throws Exception {
		notifier = _notifier == null ? notifier : _notifier;
	}

	public Cyberplus() {
		// Empty
	}

	@Override
	public void authenticate(String _login, String _password) throws Exception {
		props.setProperty("login", _login);
		props.setProperty("password", _password);

		try {
			notifier.initCounter("login", 3);
			authentication();
		} catch (Exception e) {
			notifier.fail(e);
			throw e;
		} finally {
			notifier.finalize();
		}
	}

	@Override
	public ExtractDocument getLastOperations(String _mainAccountID, String _codeBanque, Date _start, Date _end) throws Exception {
		props.setProperty("mainAccountID", _mainAccountID);
		props.setProperty("codeBanque", _codeBanque);
		props.setProperty("dateStart", format.format(_start));
		props.setProperty("dateEnd", format.format(_end));
		try {
			notifier.initCounter("export", 5);
			return exportFile();
		} catch (Exception e) {
			notifier.fail(e);
			throw e;
		} finally {
			notifier.finalize();
		}
	}

	private void authentication() throws Exception {
		// Avoid maintenance warning page
		extractUtils.writeDejaVuCookie();

		doPortalAction("Get session cookies and token", new PortalAction() {

			@Override
			public void exec() throws Exception {
				addExtractor(new TokenExtractor("amAuthCookie", "AMAuthCookie=([^\"]*)"));
				extractUtils.callAndExtract(URL_LOGIN + PARAM_REALM, null, getExtractors());
			}
		});

		doPortalAction("Post authentication data", new PortalAction() {

			@Override
			public void exec() throws Exception {
				PostData postData = Payloads.login(getProperty("login"), getProperty("password"));
				addExtractor(new TokenExtractor("loginToken", "saveToken\\(\"([a-z0-9]+)"));
				extractUtils.callAndExtract(URL_LOGIN + PARAM_LOGIN + getProperty("amAuthCookie"), postData, getExtractors());
			}
		});

		doPortalAction("Load cyberplus portal", new PortalAction() {

			@Override
			public void exec() throws Exception {
				addExtractor(new TokenExtractor("portalToken", "([a-z0-9]{32})"));
				extractUtils.callAndExtract(URL_INTEGRATED_PORTAL, null, getExtractors());
			}
		});

	}

	private ExtractDocument exportFile() throws Exception {
		doPortalAction("Initialize export action", new PortalAction() {

			@Override
			public void exec() throws Exception {
				addExtractor(new TokenExtractor());
				addExtractor(new TokenExtractor("taskOID", "name=\"taskOID\" value=\"([a-z0-9]+)\""));
				addExtractor(new HashExtractor());
				extractUtils.callAndExtract(URL_START_DOWNLOAD + getProperty("portalToken"), null, getExtractors());
			}

		});
		doPortalAction("Select CSV export format", new PortalAction() {

			@Override
			public void exec() throws Exception {
				addExtractor(new TokenExtractor());
				addExtractor(new HashExtractor());
				PostData postData = Payloads.exportSelectFormat(getTaskOID(), getToken());
				extractUtils.callAndExtract(URL_CONTINUE, postData, getExtractors());
			}
		});
		doPortalAction("Select CSV options", new PortalAction() {

			@Override
			public void exec() throws Exception {
				addExtractor(new TokenExtractor());
				addExtractor(new HashExtractor());
				PostData postData = Payloads.exportSelectOptions(getTaskOID(), getToken());
				extractUtils.callAndExtract(URL_CONTINUE, postData, getExtractors());
			}
		});
		doPortalAction("Select account and date range", new PortalAction() {

			@Override
			public void exec() throws Exception {
				addExtractor(new TokenExtractor());
				addExtractor(new HashExtractor());
				addExtractor(new TokenExtractor("documentID", "DocumentUtils\\.download\\(.([a-z0-9]*)"));
				String dateStart = getProperty("dateStart");
				String dateEnd = getProperty("dateEnd");
				String codeBanque = getProperty("codeBanque");
				String account = getProperty("mainAccountID");
				PostData postData = Payloads.exportSelectAccount(getTaskOID(), getToken(), dateStart, dateEnd, codeBanque, account);
				extractUtils.callAndExtract(URL_CONTINUE, postData, getExtractors());
			}
		});
		doPortalAction("Download last operations", new PortalAction() {

			@Override
			public void exec() throws Exception {
				extract = extractUtils.downloadDocument(URL_DOWNLOAD + getProperty("documentID"));
			}
		});
		return extract;
	}

	private void doPortalAction(String _name, PortalAction _portalAction) throws Exception {
		_portalAction.setProperties(props);
		notifier.startAction(_name);
		try {
			_portalAction.exec();
			HashExtractor hashExtractor = null;
			for (ContentExtractor<?> extractor : _portalAction.getExtractors()) {
				if (extractor instanceof HashExtractor) {
					hashExtractor = (HashExtractor) extractor;
				} else {
					props.setProperty(extractor.getName(), extractor.getValue().toString());
				}
			}
			if (hashExtractor != null) {
				String token = TokenHashKey.getTokenHashKey(props.getProperty(TokenExtractor.DEFAULT_NAME), hashExtractor.getValue());
				props.setProperty(TokenExtractor.DEFAULT_NAME, token);
			}
			notifier.finishAction("completed");
		} catch (Exception e) {
			throw new Exception(_name + " failed", e);
		}
	}

	public void setNotifier(Notifier _notifier) {
		notifier = _notifier;
	}

}
