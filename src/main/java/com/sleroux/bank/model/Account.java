package com.sleroux.bank.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(columnDefinition="INT")
	private long	id;

	private String	type;

	@Column(columnDefinition = "INT", name = "user_id")
	private long	userID;

	private String	login;

	@Column(name = "password_enc")
	private String	passwordEnc;

	public long getId() {
		return id;
	}

	public void setId(long _id) {
		id = _id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String _login) {
		login = _login;
	}

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		type = _type;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long _userID) {
		userID = _userID;
	}

	public String getPasswordEnc() {
		return passwordEnc;
	}

	public void setPasswordEnc(String _passwordEnc) {
		passwordEnc = _passwordEnc;
	}

}
