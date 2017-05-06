package com.sleroux.bank.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;

import javax.xml.bind.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.operation.Operation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestCategoValidation {

	@Autowired
	CategoService	catego;

	@Autowired
	IOperationDao	operationDao;

	@Test
	public void testCatego3() throws SQLException, ValidationException {

		Operation op = new Operation();
		op.setDebit(new BigDecimal("120"));

		catego.getCreditsCatego().addAll(Arrays.asList("CREDIT", "REMBOURSEMENT"));
		catego.getDebitsCatego().addAll(Arrays.asList("DEBIT", "LOISIRS"));

		catego.validate(op, "LOISIRS");

	}

	@Test(expected = ValidationException.class)
	public void testCategoLength() throws ValidationException, SQLException {

		Operation op = new Operation();
		op.setDebit(new BigDecimal("120"));

		catego.validate(op, "RE");

	}

	@Test(expected = ValidationException.class)
	public void testCategoLength2() throws ValidationException, SQLException {

		Operation op = new Operation();
		op.setDebit(new BigDecimal("120"));

		catego.validate(op, "REB");

	}

	@Test
	public void testCategoLength3() throws ValidationException, SQLException {
		Operation op = new Operation();
		op.setDebit(new BigDecimal("120"));
		catego.validate(op, "REMB");

	}

}
