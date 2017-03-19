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
	@Column(columnDefinition = "INT")
	protected long	id;

	@Column(length = 20)
	private String	username;

	@Column(length = 40, name = "password_enc")
	private String	passwordEnc;

	public long getId() {
		return id;
	}

	public void setId(long _id) {
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
