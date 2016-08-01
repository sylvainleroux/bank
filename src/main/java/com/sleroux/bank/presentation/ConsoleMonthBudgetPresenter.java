package com.sleroux.bank.presentation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BudgetMonth;
import com.sleroux.bank.model.calc.MonthAdjusted;

public class ConsoleMonthBudgetPresenter implements MonitorInterface {

	private MonthAdjusted				monthAdjusted;
	private BudgetMonth					monthBudget;
	//
	private BudgetKeys					keyList;
	private final static List<String>	discalifiedKeys	= Arrays.asList("LOYER", "AUTO", "CAUTIONS");

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sleroux.bank.presentation.MonitorInterface#print()
	 */
	@Override
	public void print(MonthAdjusted _monthAdjusted, BudgetMonth _currentMonthBudget, BudgetKeys _list) {
		keyList = _list;
		monthBudget = _currentMonthBudget;
		monthAdjusted = _monthAdjusted;
		// CREDIT┌ ┐
		System.out.printf("┌───────────────┬──────────┬──────────┬──────────┬────────────────────┐\n");
		System.out.printf("│%-15s│%-10s│%-10s│%-10s│%-20s│\n",
				(monthAdjusted.getMonth()) + "/" + monthAdjusted.getYear().getYear(), "  PREVIS.", "  ACTUEL",
				"   DIFF", "       GRAPH");
		System.out.printf("├───────────────┼──────────┼──────────┼──────────┼────────────────────┤\n");
		BigDecimal totalCredit = BigDecimal.ZERO;
		for (String s : keyList.getCredit().keySet()) {
			totalCredit = printLine(s, monthBudget.getCredits()).add(totalCredit);
		}
		// DEBIT
		System.out.printf("├───────────────┼──────────┼──────────┼──────────┼────────────────────┤\n");
		BigDecimal totalDebit = BigDecimal.ZERO;
		for (String s : keyList.getDebit().keySet()) {
			totalDebit = printLine(s, monthBudget.getDebits()).add(totalDebit);
		}
		System.out.printf("├───────────────┼──────────┼──────────┼──────────┼────────────────────┤\n");
		double graphValueCredit = totalCredit.doubleValue() * 100 / monthBudget.getTotalCredit().doubleValue();
		System.out.printf("│%-15s│%10.2f│%10.2f│%10.2f│%s│\n", "CREDIT", monthBudget.getTotalCredit(), totalCredit,
				monthBudget.getTotalCredit().subtract(totalCredit), graph(graphValueCredit));
		double graphValueDebit = totalDebit.doubleValue() * 100 / monthBudget.getTotalDebit().doubleValue();
		System.out.printf("│%-15s│%10.2f│%10.2f│%10.2f│%s│\n", "DEBIT", monthBudget.getTotalDebit(), totalDebit,
				monthBudget.getTotalDebit().subtract(totalDebit), graph(graphValueDebit));
		System.out.printf("├───────────────┼──────────┼──────────┼──────────┼────────────────────┤\n");
		System.out.printf("│%-15s│%10.2f│%10.2f│%10.2f│                    │\n", "SOLDE",
				_currentMonthBudget.getEstimatedEndOfMonthBalance(), monthAdjusted.getBalance(),
				monthAdjusted.getBalance().subtract(_currentMonthBudget.getEstimatedEndOfMonthBalance()));
		System.out.printf("└───────────────┴──────────┴──────────┴──────────┴────────────────────┘\n");
	}

	private BigDecimal printLine(String s, HashMap<String, BigDecimal> budget) {
		BigDecimal reportValue = null;
		BigDecimal budgetValue = null;
		BigDecimal rep = BigDecimal.ZERO;
		if (monthAdjusted.getMonthReport().containsKey(s)) {
			reportValue = monthAdjusted.getMonthReport().get(s);
			if (reportValue != null) {
				rep = monthAdjusted.getMonthReport().get(s);
				if (rep.compareTo(BigDecimal.ZERO) < 0) {
					rep = rep.negate();
				}
			}
		}
		BigDecimal bud = BigDecimal.ZERO;
		if (budget.containsKey(s)) {
			budgetValue = budget.get(s);
			if (budgetValue != null)
				bud = budgetValue;
		}
		if (discalifiedKeys.contains(s)) {
			if (budgetValue != null && budgetValue.intValue() != 0)
				System.err.println("Discalified budget :" + s + " " + budgetValue);
			if (reportValue != null && reportValue.intValue() != 0)
				System.err.println("Discalified debit : " + s + " " + reportValue);
			return BigDecimal.ZERO;
		}
		String ratio = "";
		BigDecimal diff = BigDecimal.ZERO;
		if (budgetValue != null && budgetValue.compareTo(BigDecimal.ZERO) != 0) {
			if (reportValue == null)
				reportValue = new BigDecimal("0");
			if (reportValue.compareTo(BigDecimal.ZERO) < 0) {
				reportValue = reportValue.negate();
			}
			double ratioDouble = reportValue.doubleValue() * 100 / budgetValue.doubleValue();
			diff = budgetValue.add(reportValue.negate());
			// Calc ratio
			ratio = graph(ratioDouble);
		}
		if (rep.equals(BigDecimal.ZERO) && bud.equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		System.out.printf("│%-15s│%10.2f│%10.2f│%10.2f│%s│\n", s, bud, rep, diff, ratio);
		if (reportValue == null) {
			reportValue = BigDecimal.ZERO;
		}
		return reportValue;
	}

	public static String graph(double ratioDouble) {
		String graph = "";
		int nbChars = (int) Math.ceil(ratioDouble / 5);
		int explodedByInt = (int) ratioDouble - 100;
		String explodedBy = "";
		if (explodedByInt > 0) {
			explodedBy = "+" + explodedByInt + "%";
		}
		int i = 0;
		for (; i < nbChars; i++) {
			if (i == 20)
				graph += "";
			if (i == (20 - explodedBy.length())) {
				graph += explodedBy;
				i += explodedBy.length();
				break;
			}
			char c = '#';
			graph += c;
		}
		for (; i < 20; i++) {
			graph += ".";
		}
		for (; i < 21; i++) {
			graph += "";
		}
		return graph;
	}
}
