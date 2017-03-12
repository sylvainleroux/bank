package com.sleroux.bank.domain;

import org.springframework.stereotype.Repository;

@Repository
public class SessionData {
	private String	username;
	private String	password;
	private String	cmbLogin;
	private int		userID;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int _userID) {
		userID = _userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String _username) {
		username = _username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String _password) {
		password = _password;
	}

	public void setCmbLogin(String _cmbLogin) {
		cmbLogin = _cmbLogin;
	}

	public String getCmbLogin() {
		return cmbLogin;
	}

}
