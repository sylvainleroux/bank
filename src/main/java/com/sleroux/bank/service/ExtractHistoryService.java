package com.sleroux.bank.service;

import java.util.Date;
import java.util.Locale;

import javax.transaction.Transactional;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IExtractHistoryDao;

@Service
public class ExtractHistoryService {
	@Autowired
	IExtractHistoryDao extractHistoryDao;

	public Date getLastImportDate(int _userID) {
		return extractHistoryDao.getLastExtractDate(_userID);
	}

	@Transactional
	public String getFormattedImportDate(long _userID) {

		PrettyTime p = new PrettyTime(new Locale("en"));
		Date d = extractHistoryDao.getLastExtractDate(_userID);
		if (d == null) {
			return "Never extracted";
		}
		return p.format(d);
	}
}
