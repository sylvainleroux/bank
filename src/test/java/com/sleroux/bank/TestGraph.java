package com.sleroux.bank;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sleroux.bank.presenter.calc.ConsoleMonthBudgetPresenter;

public class TestGraph {

	@Test
	public void testGraph() {
		assertEquals(ConsoleMonthBudgetPresenter.graph(1), "#...................");
		assertEquals(ConsoleMonthBudgetPresenter.graph(5), "#...................");
		assertEquals(ConsoleMonthBudgetPresenter.graph(6), "##..................");
		assertEquals(ConsoleMonthBudgetPresenter.graph(0), "....................");
		assertEquals(ConsoleMonthBudgetPresenter.graph(102), "#################+2%");
		assertEquals(ConsoleMonthBudgetPresenter.graph(101), "#################+1%");
		assertEquals(ConsoleMonthBudgetPresenter.graph(100), "####################");
		assertEquals(ConsoleMonthBudgetPresenter.graph(110), "################+10%");
		assertEquals(ConsoleMonthBudgetPresenter.graph(210), "###############+110%");
		// TODO
		assertEquals(ConsoleMonthBudgetPresenter.graph(Integer.MAX_VALUE), "########+2147483547%");
	}
}
