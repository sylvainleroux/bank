package com.sleroux.cyberplusclient;

import java.util.Calendar;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.sleroux.accountchecker.AccountChecker;
import com.sleroux.accountchecker.banquepopulaire.Cyberplus;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.fileimport.ExtractOperation;
import com.sleroux.bank.util.Config;

public class TestExtract {

	@Test
	public void testAuthentication() throws Exception {
		Logger.getLogger(Cyberplus.class).setLevel(Level.INFO);
		Config.loadProperties();
		String login = Config.getLogin();
		String password = Config.getPassword();
		if (login == null || password == null) {
			System.out.println("No credentials, unable to test");
			return;
		}
		AccountChecker cyberplus = new Cyberplus();
		cyberplus.authenticate(Config.getLogin(), Config.getPassword());
		Calendar today = Calendar.getInstance();
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.add(Calendar.DATE, -7);
		ExtractDocument extract = cyberplus.getLastOperations(Config.getMainAccountID(), Config.getCodeBanque(), lastWeek.getTime(),
				today.getTime());
		for (ExtractOperation o : extract.getOperations()) {
			System.out.println(o.toString());
		}
		Assert.assertNotNull(extract);
	}

}
