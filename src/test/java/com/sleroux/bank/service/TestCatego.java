package com.sleroux.bank.service;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.testutils.OperationHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestCatego {

	@Autowired
	CategoService	categoService;

	@Autowired
	IOperationDao	operationDao;

	@Test
	@Transactional
	public void runTest() {

		Operation op1 = OperationHelper.createDebitOperation();
		op1.setLibelle("CARREFOUR CITY QUIMPER");
		op1.setCatego("COURSES");
		operationDao.create(op1);
		
		Operation test = OperationHelper.createDebitOperation();
		test.setLibelle("CARREFOUR");

		List<String> suggestions = categoService.getCategoSuggestionsFor(test);
		Assert.assertEquals("COURSES", suggestions.get(0));

	}
	
	@Test
	@Transactional
	public void runTest2() {

		Operation op1 = OperationHelper.createDebitOperation();
		op1.setLibelle("AMAZON SARL");
		op1.setCatego("LOISIRS");
		operationDao.create(op1);
		
		Operation op2 = OperationHelper.createDebitOperation();
		op2.setLibelle("AMAZON POP");
		op2.setCatego("LOISIRS");
		operationDao.create(op2);
		
		Operation op3 = OperationHelper.createDebitOperation();
		op3.setLibelle("AMAZON AVION");
		op3.setCatego("COURSES");
		operationDao.create(op3);
		
		Operation op4 = OperationHelper.createDebitOperation();
		op4.setLibelle("AMAZON UK");
		op4.setCatego("LOISIRS");
		operationDao.create(op4);
		
		Operation test = OperationHelper.createDebitOperation();
		test.setLibelle("AMAZON FR");
		
		List<String> suggestions = categoService.getCategoSuggestionsFor(test);
		Assert.assertEquals("LOISIRS", suggestions.get(0));

	}

}
