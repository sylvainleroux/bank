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
@Table(name = "user")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int	id;

	@Column(length = 20)
	private String	username;

	@Column(length=40, name= "password_enc")
	private String passwordEnc;
	
	@Column(name="cmb_login")
	private String cmbLogin;

	public String getCmbLogin() {
		return cmbLogin;
	}

	public void setCmbLogin(String _cmbLogin) {
		cmbLogin = _cmbLogin;
	}

	public int getId() {
		return id;
	}

	public void setId(int _id) {
		id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String _username) {
		username = _username;
	}

	public String getPasswordEnc() {
		return passwordEnc;
	}

	public void setPasswordEnc(String _passwordEnc) {
		passwordEnc = _passwordEnc;
	}
	
}


