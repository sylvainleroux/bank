package com.sleroux.accountchecker.banquepopulaire.util.notifier;

import org.apache.log4j.Logger;

public class LogNotifier implements Notifier {

	private Logger	logger;

	public LogNotifier(Logger _logger) {
		logger = _logger;
	}

	@Override
	public void startAction(String _name) {
		logger.info(_name);
	}

	@Override
	public void finishAction(String _content) {
		logger.info("\t done. " + _content);
	}

	@Override
	public void finalize() {
		// empty, decoration only
	}

	@Override
	public void fail(Exception _e) {
		logger.warn(_e);
	}

	@Override
	public void initCounter(String _name, int _totalActions) {
		// empty, decoration only
	}

}
