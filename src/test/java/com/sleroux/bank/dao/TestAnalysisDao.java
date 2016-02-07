package com.sleroux.bank.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.AnalysisFact;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.testutils.OperationHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestAnalysisDao {

	@Autowired
	AnalysisDao		analysisDao;

	@Autowired
	IOperationDao	operationDao;

	@Test
	@Transactional
	public void testAnalysis() {

		Operation o = OperationHelper.createDebitOperation();
		o.setCatego("CATEGO1");

		operationDao.create(o);

		List<AnalysisFact> list = analysisDao.getFacts();
		System.out.println(list.size());

	}

}
