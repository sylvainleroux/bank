package com.sleroux.bank;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.sleroux.bank.domain.BudgetIndex;
import com.sleroux.bank.model.Budget;

public class TestBuildIndex {

	@Test
	public void testBuildIndex() {

		Budget a = new Budget();
		a.setYear(2017);
		a.setMonth(5);
		a.setCompte("Test");
		a.setCatego("TEST_CAT");

		Budget b = new Budget();
		b.setYear(2017);
		b.setMonth(5);
		b.setCompte("Test");
		b.setCatego("TEST_CAT2");

		BudgetIndex index = new BudgetIndex(Arrays.asList(a, b));

		Budget c = index.find(b);

		Assert.assertTrue(b == c);

	}

	@Test
	public void testReduce() {

		Budget a = new Budget();
		a.setYear(2017);
		a.setMonth(5);
		a.setCompte("Test");
		a.setCatego("TEST_CAT");
		a.setCredit(new BigDecimal("10"));

		Budget b = new Budget();
		b.setYear(2017);
		b.setMonth(5);
		b.setCompte("Test");
		b.setCatego("TEST_CAT2");
		b.setCredit(new BigDecimal("34"));

		Budget c = new Budget();
		c.setYear(2017);
		c.setMonth(5);
		c.setCompte("Test3");
		c.setCatego("TEST_CAT2");
		c.setCredit(new BigDecimal("100"));

		Budget d = new Budget();
		d.setYear(2017);
		d.setMonth(6);
		d.setCompte("Test3");
		d.setCatego("TEST_CAT3");
		d.setCredit(new BigDecimal("100"));

		BudgetIndex index = new BudgetIndex(Arrays.asList(a, b, c, d));

		HashMap<String, BigDecimal> m = index.map.get(2017).reduce();

		m.entrySet().forEach(e -> {
			System.out.println(e.getKey() + " : " + e.getValue());
		});

	}

	@Test
	public void testCredits() {

		Budget a = new Budget();
		a.setYear(2017);
		a.setMonth(5);
		a.setCompte("Test");
		a.setCatego("TEST_CAT");
		a.setCredit(new BigDecimal("10"));

		Budget b = new Budget();
		b.setYear(2017);
		b.setMonth(5);
		b.setCompte("Test");
		b.setCatego("TEST_CAT2");
		b.setCredit(new BigDecimal("34"));

		Budget c = new Budget();
		c.setYear(2017);
		c.setMonth(5);
		c.setCompte("Test3");
		c.setCatego("TEST3_CAT2");
		c.setCredit(new BigDecimal("100"));

		Budget d = new Budget();
		d.setYear(2017);
		d.setMonth(6);
		d.setCompte("Test3");
		d.setCatego("TEST3_CAT3");
		d.setCredit(new BigDecimal("100"));

		BudgetIndex index = new BudgetIndex(Arrays.asList(a, b, c, d));
		Assert.assertEquals(2, index.getCredits("Test").size());
		Assert.assertEquals(2, index.getCredits("Test3").size());
		Assert.assertEquals(0, index.getDebits("Test3").size());

	}

}
