package com.sleroux.accountchecker;

import java.util.Date;

import com.sleroux.bank.model.fileimport.ExtractDocument;

public interface AccountChecker {

	public void authenticate(String _login, String _password) throws Exception;

	public ExtractDocument getLastOperations(String _mainAccountID, String _codeBanque, Date _start, Date _end) throws Exception;

}
