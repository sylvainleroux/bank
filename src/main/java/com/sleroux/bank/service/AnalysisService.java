package com.sleroux.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.AnalysisDao;
import com.sleroux.bank.model.AnalysisFact;

@Service
public class AnalysisService {

	@Autowired
	AnalysisDao	analysisDao;

	public int getNbFacts() {
		List<AnalysisFact> list = analysisDao.getFacts();
		return list.size();
	}

	public List<AnalysisFact> getFacts() {
		List<AnalysisFact> list = analysisDao.getFacts();

		return list;
	}
}
