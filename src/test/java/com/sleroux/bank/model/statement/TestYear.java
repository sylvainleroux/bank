package com.sleroux.bank.model.statement;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class TestYear {

	@Test
	public void testGetLastOperationWithNonNullDate() {
		Operation a = new Operation();
		Operation b = new Operation();
		a.setDateOperation(Calendar.getInstance().getTime());
		Year year = new Year();
		year.getOperations().add(a);
		year.getOperations().add(b);
		Operation c = year.getLastOperationWithNonNullDate();
		Assert.assertEquals(a, c);
	}

}
