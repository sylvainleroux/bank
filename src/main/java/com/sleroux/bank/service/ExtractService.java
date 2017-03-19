package com.sleroux.bank.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.dao.IExtractHistoryDao;
import com.sleroux.bank.model.extract.ExtractData;
import com.sleroux.bank.util.Config;

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

	public void launchExtract(String _type, String _login, String _password) {

		String token = authTokenService.createAuthencationToken(_login, _password);

		switch (_type) {
		case EXTRACT_TYPE_BPO:
			extract("script/bpo.js", token);
			break;
		case EXTRACT_TYPE_CMB:
			extract("script/cmb.js", token);
			break;
		default:
			logger.error("Unknow type [" + _type + "] in extractService");
		}

	}

	private void extract(String _command, String _token) {

		ProcessBuilder builder = new ProcessBuilder("lib/phantomjs", _command, _token);
		builder.directory(new File(CRAWLER_HOME));
		builder.redirectErrorStream(true);
		boolean first = true;
		String data = null;
		try {
			Process p = builder.start();
			Scanner s = new Scanner(p.getInputStream());
			while (s.hasNextLine()) {
				String line = s.nextLine();
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

			extractData.getFiles().forEach((file) -> {

				try {
					URL url = new URL(file);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("Cookie", "JSESSIONID=" + extractData.getCookie());
					connection.setRequestProperty("Referer", extractData.getReferer());

					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null)
						System.out.println(inputLine);
					in.close();

					System.out.println(connection.getHeaderField("Content-Disposition"));

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public List<String> getFilesCMB() {
		// Scan Downloads directory
		List<String> foundFiles = new ArrayList<String>();
		File f = new File(Config.getExtractDownloadPath());
		String[] files = f.list();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i];
			if (filename.startsWith("RELEVE_")) {
				foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
			}
		}
		return foundFiles;
	}

	public List<String> getFilesBPO() {
		// Scan Downloads directory
		List<String> foundFiles = new ArrayList<String>();
		File f = new File(Config.getExtractDownloadPath());
		String[] files = f.list();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i];
			if (filename.startsWith("BPO.")) {
				foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
			}
		}
		return foundFiles;
	}

	public List<String> getBalanceFiles() {
		List<String> foundFiles = new ArrayList<String>();
		File f = new File(Config.getExtractDownloadPath());
		String[] files = f.list();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i];
			if (filename.contains("_BALANCE")) {
				foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
			}
		}
		return foundFiles;
	}

}
