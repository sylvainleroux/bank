package com.sleroux.bank.service;

import java.util.Date;
import java.util.Locale;

import javax.transaction.Transactional;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.ExtractHistoryDao;

@Service
public class ExtractHistoryService {
	@Autowired
	ExtractHistoryDao	extractHistoryDao;

	public Date getLastImportDate() {
		return extractHistoryDao.getLastExtractDate();
	}

	@Transactional
	public String getFormattedImportDate() {

		PrettyTime p = new PrettyTime(new Locale("en"));
		Date d = extractHistoryDao.getLastExtractDate();
		return p.format(d);
	}
}
