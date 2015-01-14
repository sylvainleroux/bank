package com.sleroux.accountchecker.banquepopulaire.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.sleroux.accountchecker.banquepopulaire.util.extractors.ContentExtractor;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.persistence.storage.extract.OperationBuilder;

public class ExtractUtils {

	private final static String	USER_AGENT		= "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:17.0) Gecko/17.0 Firefox/17.0";

	private Logger				logger			= Logger.getLogger(ExtractUtils.class);
	private CookieManager		cookieManager	= new CookieManager();

	public ExtractUtils() {
		CookieHandler.setDefault(cookieManager);
		HttpURLConnection.setFollowRedirects(true);
	}

	public void callAndExtract(String _url, PostData _postData, List<ContentExtractor<?>> _extractors) throws Exception {
		HttpURLConnection con = (HttpURLConnection) new URL(_url).openConnection();
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setConnectTimeout(5000);
		if (_postData == null) {
			con.setRequestMethod("GET");
		} else {
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
			writer.write(_postData.getQueryString());
			writer.flush();
		}
		// Check response code
		if (200 != con.getResponseCode()) {
			throw new Exception("Connection failed with code : " + con.getResponseCode());
		}
		// Execute extractors
		InputStream in = con.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			boolean completed = true;
			for (ContentExtractor<?> extractor : _extractors) {
				completed = extractor.extract(line) && completed;
			}
			if (completed) {
				logger.debug("all completed");
				break;
			}
		}
		// Check extracted values are null or empty
		for (ContentExtractor<?> extractor : _extractors) {
			if (extractor.getValue() == null || extractor.getValue().equals("")) {
				throw new Exception(extractor.getName() + "extracted value is null or empty");
			}
		}
	}

	public ExtractDocument downloadDocument(String _url) throws Exception {
		HttpURLConnection con = (HttpURLConnection) new URL(_url).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setConnectTimeout(5000);
		// Check response
		if (200 != con.getResponseCode()) {
			throw new Exception("Connection failed with code : " + con.getResponseCode());
		}
		// System.out.println(con.getResponseCode());
		ExtractDocument document = new ExtractDocument();
		InputStreamReader r = new InputStreamReader(con.getInputStream(), "ISO-8859-1");
		CSVReader reader = new CSVReader(r, ';');
		try {
			String[] nextLine;
			boolean firstLine = true;
			while ((nextLine = reader.readNext()) != null) {
				if (firstLine) {
					firstLine = false;
					continue;
				}
				try {
					document.addOperation(OperationBuilder.createOperation(nextLine));
				} catch (Exception e) {
					throw new Exception("Unable to parse Operation", e);
				}
			}
		} finally {
			reader.close();
		}
		document.setFilename("direct");
		return document;
	}

	/** Set cookie to avoid maintenance warning page */
	public void writeDejaVuCookie() {
		HttpCookie cookie = new HttpCookie("dejaVu", "true");
		cookie.setMaxAge(new Date().getTime() + 3600);
		cookie.setDomain(".www.ibps.ouest.banquepopulaire.fr");
		cookieManager.getCookieStore().add(null, cookie);
	}

}
