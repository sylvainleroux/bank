package com.sleroux.accountchecker.banquepopulaire.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sleroux.accountchecker.banquepopulaire.util.extractors.ContentExtractor;
import com.sleroux.accountchecker.banquepopulaire.util.extractors.TokenExtractor;

public abstract class PortalAction {

	private List<ContentExtractor<?>>	extractors	= new ArrayList<>();
	private Properties					properties;

	public abstract void exec() throws Exception;

	protected void addExtractor(ContentExtractor<?> _extractor) {
		extractors.add(_extractor);
	}

	public List<ContentExtractor<?>> getExtractors() {
		return extractors;
	}

	public void setProperties(Properties _props) {
		properties = _props;
	}

	protected String getProperty(String _string) {
		return properties.getProperty(_string);
	}

	protected Properties getProperties() {
		return properties;
	}

	protected String getToken() {
		return properties.getProperty(TokenExtractor.DEFAULT_NAME);
	}

	protected String getTaskOID() {
		return properties.getProperty("taskOID");
	}

}
