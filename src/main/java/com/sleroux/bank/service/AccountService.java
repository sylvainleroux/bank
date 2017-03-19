package com.sleroux.bank.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IAccountDao;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.Account;
import com.sleroux.bank.util.Encryption;

@Service
public class AccountService {

	@Autowired
	IAccountDao	accountDao;

	@Autowired
	SessionData	sessionData;

	@Transactional
	public List<Account> getAccountsByUserID(long _userID) {
		return accountDao.findByUserID(_userID);
	}

	@Transactional
	public void addAccount(String _login, String _password, String _type, long _userID) {

		Account account = new Account();
		account.setLogin(_login);
		account.setType(_type);
		account.setUserID(_userID);

		String encryptionKey;
		try {
			encryptionKey = Encryption.sha1(sessionData.getPassword() + "panda2017");
			account.setPasswordEnc(Encryption.mysqlAesEncrypt(_password, encryptionKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		accountDao.create(account);
	}

}
