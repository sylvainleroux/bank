package com.sleroux.accountchecker.banquepopulaire.util.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class TokenExtractor implements ContentExtractor<String> {

	private final static String	DEFAULT_PATTERN	= "id=\"token\" value=\"([a-z0-9]+)\"";
	public final static String	DEFAULT_NAME	= "token";

	private Pattern				pattern;
	private String				token;
	private String				name;
	private Logger				logger;

	public TokenExtractor() {
		this(DEFAULT_NAME, DEFAULT_PATTERN);
	}

	public TokenExtractor(Logger _logger) {
		this(DEFAULT_NAME, DEFAULT_PATTERN, _logger);
	}

	public TokenExtractor(String _name, String _pattern) {
		this(_name, _pattern, null);
	}

	public TokenExtractor(String _name, String _pattern, Logger _logger) {
		pattern = Pattern.compile(_pattern);
		name = _name;
		logger = _logger;
	}

	@Override
	/**
	 * returns true when content is extracted. The first value found is returned
	 */
	public boolean extract(String _line) {
		if (token != null) {
			logger.debug("already found");
			return true;
		}
		if (logger != null) {
			logger.debug(_line);
		}
		Matcher m = pattern.matcher(_line);
		while (m.find()) {
			if (m.groupCount() > 0)
				token = m.group(1);
		}
		return token != null;
	}

	@Override
	public String getValue() {
		return token;
	}

	@Override
	public String getName() {
		return name;
	}

}
