package com.sleroux.bank.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map.Entry;
import java.util.Properties;

public class Config {

	public final static String	CONFIG_FILE	= ".bank";
	private static Properties	properties	= new Properties();

	public static String setup() throws Exception {
		Properties prop = new Properties();
		prop.load(Config.class.getResourceAsStream("default.properties"));
		File configFile = getConfigFile();
		if (configFile.exists()) {
			throw new Exception("Config file allready exists, will not be overwritten.");
		} else {
			saveProperties(prop, configFile);
		}
		return configFile.getAbsolutePath();
	}

	private static void saveProperties(Properties prop, File configFile) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(configFile);
		prop.store(out, "Config file for Bank");
		out.close();
	}

	public static void loadProperties() throws IOException {
		properties.clear();
		// Load default
		properties.load(Config.class.getResourceAsStream("default.properties"));

		// Load custom
		Properties p2 = new Properties();
		try {
			p2.load(new FileInputStream(getConfigFile()));
		} catch (IOException e) {
			System.err.println("Custom config file not found, run bank configure");
		}

		for (Entry<Object, Object> entry : p2.entrySet()) {
			String key = entry.getKey().toString();
			if ("VERSION".equals(key)) {
				continue;
			}
			if (properties.containsKey(key)) {
				properties.remove(key);
			}
			properties.put(key, entry.getValue());

		}
	}

	public static String getExtractDownloadPath() {
		return getProperty("EXTRACT_DOWNLOAD_PATH");
	}

	public static String getMainDocumentPath() {
		return getProperty("MAIN_DOCUMENT_PATH");
	}

	public static String getMainDocumentName() {
		return getProperty("MAIN_DOCUMENT_NAME");
	}

	public static String getMainDocumentBackupPath() {
		return getProperty("MAIN_DOCUMENT_BACKUP_PATH");
	}

	public static String getTempDir() {
		return getProperty("TEMP_DIR");
	}

	public static String getEncryptedPassword() {
		return getProperty("PASSWORD");
	}

	public static String getEncryptedLogin() {
		return getProperty("USERNAME");
	}

	public static String getPassword() {
		try {
			return Encryption.decrypt(getEncryptedPassword());
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getLogin() {
		String encrypted = getEncryptedLogin();
		if (encrypted == null || "EMPTY".equals(encrypted)) {
			return "EMPTY";
		}
		try {
			return Encryption.decrypt(encrypted);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getProperty(String _key) {
		String value = properties.getProperty(_key);
		if (value == null) {
			System.err.println("Unknown property : " + _key);
			return null;
		}
		if (value.startsWith("~")) {
			try {
				value = getUserHomeDir() + value.substring(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	private static File getConfigFile() {
		try {
			return new File(getUserHomeDir() + File.separator + CONFIG_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getUserHomeDir() throws IOException {
		String home = System.getProperty("user.home");
		if (home == null || home.equals("")) {
			throw new IOException("User home not found");
		}
		return home;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static void updateCredentials(String login, String password) {
		try {
			properties.setProperty("USERNAME", Encryption.encrypt(login));
			properties.setProperty("PASSWORD", Encryption.encrypt(password));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		try {
			saveProperties(properties, getConfigFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getVersion() {
		return getProperty("VERSION");
	}

	public static String getCodeBanque() {
		return getProperty("CODE_BANQUE");
	}

	public static String getMainAccountID() {
		return getProperty("MAIN_ACCOUNT_ID");
	}

	public static String getFilterFileName() {
		return getProperty("FILTER_FILE_NAME");
	}

	public static String getBudgetDocument() {
		return getProperty("BUDGET_DOCUMENT");
	}

	public static String getBudgetDocumentTemplate() {
		return getProperty("BUDGET_DOCUMENT_TEMPLATE");
	}

	public static String getDBHost() {
		return getProperty("DB_HOST");
	}

	public static String getDBPort() {
		return getProperty("DB_PORT");
	}

	public static String getDBUser() {
		return getProperty("DB_USERNAME");
	}

	public static String getDBPass() {
		return getProperty("DB_PASSWORD");
	}

	public static String getImportCommandPath() {
		return getProperty("IMPORT_COMMAND_PATH");
	}

	public static String getImportCommandCMB() {
		return getProperty("IMPORT_COMMAND_CMB");
	}

	public static String getImportCommandBPO() {
		return getProperty("IMPORT_COMMAND_BPO");
	}

	public static String getImportCommandEdenred() {
		return getProperty("IMPORT_COMMAND_EDENRED");
	}

	// Prefix imported files with IMPORTED_
	public static boolean getArchiveImportFiles() {

		return properties.get("ARCHIVE_IMPORT_FILES").equals("true");
	}

	public static void setArchiveImportFiles(Boolean _b) {
		properties.setProperty("ARCHIVE_IMPORT_FILES", _b.toString());
	}

	public static boolean deleteImportFile() {
		return properties.get("DELETE_IMPORT_FILES").equals("true");
	}

	public static void setDeleteImportFile(Boolean _b) {
		properties.setProperty("DELETE_IMPORT_FILES", _b.toString());

	}

}
