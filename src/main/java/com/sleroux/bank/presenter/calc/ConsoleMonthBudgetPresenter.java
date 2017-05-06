package com.sleroux.bank.presenter.calc;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sleroux.bank.model.calc.BudgetCalc;

public class ConsoleMonthBudgetPresenter implements MonitorInterface {

	private int			columCatego	= 22;
	private int			columnDebit	= 12;
	private int			columnGraph	= 20;

	private PrintStream	out			= System.out;

	public ConsoleMonthBudgetPresenter(PrintStream _outStream) {
		out = _outStream;
	}

	@Override
	public void printCalc(BudgetCalc _budgetCalc) {

		_budgetCalc.entrySet().stream().forEach(entry -> {

			String compte = entry.getKey();
			System.out.println("");
			prTable("┌", "┬", "┐");

			prHeader(Arrays.asList(compte, "   BUDGET", "   ACTUEL", "       GRAPH"));

			entry.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey((a, b) -> -1)).forEachOrdered(en -> {
				prTable("├", "┼", "┤");

				en.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e -> {
					pr(e.getKey(), e.getValue().getOperation(), e.getValue().getBudget(), en.getKey());

				});

			});
			prTable("└", "┴", "┘");

		});
	}

	private void prHeader(List<String> _list) {

		String format = "│%-" + columCatego + "s│%-" + columnDebit + "s│%-" + columnDebit + "s│%-" + columnGraph
				+ "s│\n";
		String[] params = (String[]) _list.toArray();
		out.printf(format, params[0], params[1], params[2], params[3]);
	}

	private void prTable(String _left, String _intersection, String _right) {
		out.printf(
				String.format(
						_left + "%0" + columCatego + "d" + _intersection + "%0" + columnDebit + "d" + _intersection
								+ "%0" + columnDebit + "d" + _intersection + "%0" + columnGraph + "d" + _right + "\n",
						0, 0, 0, 0).replaceAll("0", "─"));
	}

	private void pr(String _catego, BigDecimal operation, BigDecimal budget, Boolean _isCredit) {

		double ratioDouble = operation.doubleValue() * 100 / budget.doubleValue();
		// Calc ratio
		String ratio = graph(ratioDouble);
		out.printf("│%-" + (columCatego - 1) + "s%s|%" + (columnDebit) + ".2f│%" + (columnDebit) + ".2f│%s│\n", _catego,
				_isCredit ? "+" : "-", budget, operation, ratio);
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
