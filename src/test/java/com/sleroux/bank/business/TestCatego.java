package com.sleroux.bank.business;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;

import javax.xml.bind.ValidationException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sleroux.bank.model.Operation;
import com.sleroux.bank.util.Config;

public class TestCatego {

	@Test(expected = ValidationException.class)
	public void testCatego() throws SQLException, ValidationException {

		Catego catego = new Catego();

		Operation op = new Operation();
		op.setMontant(new BigDecimal("-120"));
		catego.getCreditsCatego().addAll(Arrays.asList("CREDIT", "REMBOURSEMENT"));
		catego.getDebitsCatego().addAll(Arrays.asList("DEBIT", "LOISIRS"));

		catego.validate(op, "REMBOURSEMENT");

	}

	@Test
	public void testCatego2() throws SQLException, ValidationException {

		Catego catego = new Catego();

		Operation op = new Operation();
		op.setMontant(new BigDecimal("120"));

		catego.getCreditsCatego().addAll(Arrays.asList("CREDIT", "REMBOURSEMENT"));
		catego.getDebitsCatego().addAll(Arrays.asList("DEBIT", "LOISIRS"));

		catego.validate(op, "REMBOURSEMENT");

	}

	@Test
	public void testCatego3() throws SQLException, ValidationException {

		Catego catego = new Catego();

		Operation op = new Operation();
		op.setMontant(new BigDecimal("-120"));

		catego.getCreditsCatego().addAll(Arrays.asList("CREDIT", "REMBOURSEMENT"));
		catego.getDebitsCatego().addAll(Arrays.asList("DEBIT", "LOISIRS"));

		catego.validate(op, "LOISIRS");

	}

	@Test(expected = ValidationException.class)
	public void testCategoLength() throws ValidationException, SQLException {

		Catego catego = new Catego();

		Operation op = new Operation();
		op.setMontant(new BigDecimal("-120"));

		catego.validate(op, "RE");

	}

	@Test(expected = ValidationException.class)
	public void testCategoLength2() throws ValidationException, SQLException {

		Catego catego = new Catego();

		Operation op = new Operation();
		op.setMontant(new BigDecimal("-120"));

		catego.validate(op, "REB");

	}

	@Test
	public void testCategoLength3() throws ValidationException, SQLException {
		Catego catego = new Catego();
		Operation op = new Operation();
		op.setMontant(new BigDecimal("-120"));
		catego.validate(op, "REMB");

	}

	// @Test
	// public void testGetCategoListCredits() throws Exception {
	// OperationDao dao = new OperationDao(DatabaseConnection.getConnection());
	// List<String> credits = dao.getCreditsCatego();
	// Assert.assertTrue(credits.contains("SALAIRE"));
	//
	// }

	@BeforeClass
	public static void doSetup() throws IOException {
		Config.loadProperties();
	}

	// @Test
	// public void testGetCategoListDebits() throws Exception {
	// OperationDao dao = new OperationDao(DatabaseConnection.getConnection());
	// List<String> debits = dao.getDebitsCatego();
	// for (String s : debits) {
	// System.out.println(s);
	// }
	// Assert.assertTrue(debits.contains("COURSES"));
	// }

	@Test(expected = ValidationException.class)
	public void testAddCatego() throws ValidationException, SQLException {
		Catego catego = new Catego();
		Operation op = new Operation();
		op.setMontant(new BigDecimal("-120"));
		catego.validate(op, "AVION");
		Operation op2 = new Operation();
		op2.setMontant(new BigDecimal("120"));
		catego.validate(op2, "AVION");
	}
}
