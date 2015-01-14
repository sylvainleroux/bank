package com.sleroux.bank.web.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sleroux.bank.web.util.CheckoutThread;

public class COCheckOutStatus {

	@JsonIgnore
	private CheckoutThread	checkoutThread;

	private long			id;

	public String getStatus() {
		return checkoutThread.getStatus();
	}

	public COCheckOutStatus() {
		id = new Date().getTime();
	}

	public long getId() {
		return id;
	}

	public CheckoutThread getCheckoutThread() {
		return checkoutThread;
	}

	public void setCheckoutThread(CheckoutThread _checkoutThread) {
		checkoutThread = _checkoutThread;
	}

}
