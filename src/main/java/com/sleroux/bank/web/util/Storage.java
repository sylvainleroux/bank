package com.sleroux.bank.web.util;

import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.web.domain.COCheckOutStatus;

public class Storage {
	
	public static Storage			instance;
	private List<COCheckOutStatus>	list	= new ArrayList<>();
	public static Storage getInstance() {
		if (instance == null) {
			instance = new Storage();
		}
		return instance;
	}

	public COCheckOutStatus create() {
		COCheckOutStatus resume = new COCheckOutStatus();
		list.add(resume);
		return resume;
	}

	public COCheckOutStatus get(long _id) {
		for (COCheckOutStatus r : list) {
			if (r.getId() == _id) {
				return r;
			}
		}
		return null;
	}

}
