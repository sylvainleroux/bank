package com.sleroux.bank.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.dao.IExtractHistoryDao;
import com.sleroux.bank.model.extract.ExtractData;
import com.sleroux.bank.model.extract.ExtractedOperation;
import com.sleroux.bank.model.extract.ExtractedReport;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class ExtractService {
	public final static String	EXTRACT_TYPE_CMB	= "CMB";
	public final static String	EXTRACT_TYPE_BPO	= "BPO";

	private final static String	CRAWLER_HOME		= "/Users/sleroux/ideo/bank-crawler/";

	@Autowired
	IExtractHistoryDao			extractHistoryDao;

	@Autowired
	AuthTokenService			authTokenService;

	private Logger				logger				= Logger.getLogger(this.getClass());

	public List<ExtractedReport> extractData(String _type, String _login, String _password) {

		switch (_type) {
		case EXTRACT_TYPE_BPO:
			return extractBPO(_login, _password);

		case EXTRACT_TYPE_CMB:
			return extractCMB(_login, _password);

		default:
			logger.error("Unknow type [" + _type + "] in extractService");
			return null;
		}

	}

	public List<ExtractedReport> extractCMB(String _login, String _password) {
		List<ExtractedReport> list = new ArrayList<>();

		// Create authentication token
		String token = authTokenService.createAuthencationToken(_login, _password);

		// Launch phantomjs extract
		ExtractData extractData = extract("scripts/cmb.js", token);

		// Download extract files
		extractData.getFiles().forEach(file -> {

			ExtractedReport extractReport = new ExtractedReport();

			URL url;
			try {
				url = new URL(file);

				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("Cookie", "JSESSIONID=" + extractData.getCookie());
				connection.setRequestProperty("Referer", extractData.getReferer());

				InputStreamReader in = new InputStreamReader(connection.getInputStream(), "ISO-8859-1");
				CSVReader reader = new CSVReader(in, ';');

				String[] nextLine;
				boolean firstLine = true;
				while ((nextLine = reader.readNext()) != null) {
					// Skip headers
					if (firstLine) {
						firstLine = false;
						continue;
					}
					try {
						extractReport.getOperations().add(createOperationCMB(nextLine));
					} catch (Exception e) {
						logger.error(e);
						continue;
					}
				}

				String account = getAccountName(connection.getHeaderField("Content-Disposition"));

				extractReport.setCompte(account);
				extractReport.setType("CMB");

				reader.close();
				in.close();

				list.add(extractReport);

			} catch (IOException e) {
				e.printStackTrace();
			}

		});

		return list;
	}

	public static String getAccountName(String _contentDisposition) {
		Pattern p = Pattern.compile("([^_]*)_[^_]*$");
		Matcher m = p.matcher(_contentDisposition);
		m.find();
		return m.group(1);
	}

	public ExtractedOperation createOperationCMB(String[] _nextLine) throws Exception {

		DecimalFormat formatter = new DecimalFormat("#0,00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);

		ExtractedOperation operation = new ExtractedOperation();

		operation.setDateOperation(parseDate(_nextLine[0]));
		operation.setDateValeur(parseDate(_nextLine[1]));
		operation.setLibelle(_nextLine[2]);

		if (_nextLine[3] != null && !_nextLine[3].equals("")) {
			try {
				Number d = formatter.parse(_nextLine[3]);
				BigDecimal value = new BigDecimal(d.toString());
				operation.setDebit(value);
			} catch (ParseException e) {
				throw new Exception(
						"Unable to parse operation amount for debit [" + _nextLine[3] + "|" + _nextLine[4] + "]", e);
			}
		} else if (_nextLine[4] != null) {
			try {
				Number d = formatter.parse(_nextLine[4]);
				BigDecimal value = new BigDecimal(d.toString());
				operation.setCredit(value);
			} catch (ParseException e) {
				throw new Exception("Unable to parse operation amount", e);
			}
		}

		return operation;

	}

	public Date parseDate(String string) throws Exception {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		try {
			return (Date) formatter.parse(string);
		} catch (ParseException e) {
			throw new Exception("Unable to parse date");
		}
	}

	public List<ExtractedReport> extractBPO(String _login, String _password) {
		List<ExtractedReport> list = new ArrayList<>();
		String token = authTokenService.createAuthencationToken(_login, _password);
		ExtractData extractData = extract("scripts/bpo.js", token);
		return list;
	}

	private ExtractData extract(String _command, String _token) {

		ProcessBuilder builder = new ProcessBuilder("lib/phantomjs", _command, _token);
		builder.directory(new File(CRAWLER_HOME));
		builder.redirectErrorStream(true);
		boolean first = true;
		String data = null;
		try {
			Process p = builder.start();
			Scanner s = new Scanner(p.getInputStream());
			StringBuffer rawOutput = new StringBuffer();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				rawOutput.append(line);
				if (line.startsWith("// ")) {
					line = line.substring(3);
					if (line.contains("[Done]")) {
						System.out.print(" (" + line.replaceAll("\t\\[Done\\] in ", "") + ")");
					} else {
						if (first) {
							first = false;
						} else {
							System.out.print("\n");
						}
						System.out.print(line);
					}
				} else {
					// Data here
					data = line;
				}

			}
			s.close();
			System.out.print("\n");

			int result = p.waitFor();

			logger.debug("Import complete, finished with code " + result);

			ObjectMapper mapper = new ObjectMapper();
			final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			mapper.setDateFormat(df);
			ExtractData extractData = mapper.readValue(data, ExtractData.class);
			extractData.setRawOutput(rawOutput.toString());

			return extractData;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

}
