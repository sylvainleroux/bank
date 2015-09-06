package com.sleroux.bank.evo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.sleroux.bank.evo.model.Budget;

public class BudgetDao {

	private Connection	conn;

	public BudgetDao(Connection _connection) {
		conn = _connection;
	}

	public void createAll(List<Budget> _list) {

		try {

			Connection conn = DatabaseConnection.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count(*) FROM bank.operation");
			while (rs.next()) {
				System.out.println(rs.getInt(1));
			}

			StringBuilder sql = new StringBuilder("INSERT INTO bank.budget(year, month, catego, debit, credit) values ");
			boolean first = true;
			for (Budget b : _list) {
				if (!first) {
					sql.append(",");
				}
				first = false;
				sql.append(String.format(Locale.ENGLISH, "(%d,%d,'%s',%.2f,%.2f)", b.getYear(), b.getMonth(), b.getCatego(), b.getDebit(),
						b.getCredit()));
			}

			System.out.println(sql);

			stmt.executeUpdate(sql.toString());

			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> getCredits() throws Exception {

		Connection conn = DatabaseConnection.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct catego from bank.budget where credit > 0 and compte = 'COURANT' order by catego");

		List<String> catego = new ArrayList<>();
		while (rs.next()) {
			catego.add(rs.getString("catego"));
		}

		return catego;
	}

	public List<String> getDebits() throws Exception {

		Connection conn = DatabaseConnection.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct catego from bank.budget where debit > 0 and compte = 'COURANT' order by catego");

		List<String> catego = new ArrayList<>();
		while (rs.next()) {
			catego.add(rs.getString("catego"));
		}

		return catego;
	}

	public List<Integer> getYears() throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct year from bank.budget order by year");
		List<Integer> list = new ArrayList<Integer>();
		while (rs.next()) {
			list.add(new Integer(rs.getInt("year")));
		}
		return list;
	}

	public List<Budget> getMonthCredits(Integer _year, int _month) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("call bank.getMonthCredits(" + _year + "," + _month + ")");
		List<Budget> list = new ArrayList<>();
		while (rs.next()) {
			Budget b = new Budget();
			b.setCatego(rs.getString("catego"));
			b.setCredit(rs.getBigDecimal("credit"));
			list.add(b);
		}

		return list;
	}

	public List<Budget> getMonthDebits(Integer _year, int _month) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("call bank.getMonthDebits(" + _year + "," + _month + ")");
		List<Budget> list = new ArrayList<>();
		while (rs.next()) {
			Budget b = new Budget();
			b.setCatego(rs.getString("catego"));
			b.setDebit(rs.getBigDecimal("debit"));
			list.add(b);
		}

		return list;
	}

	public List<String> getComptesEpargne() throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct compte from budget where compte <> 'COURANT' order by compte");
		List<String> list = new ArrayList<>();
		while (rs.next()) {
			list.add(rs.getString("compte"));
		}
		return list;

	}

	public Budget getBudgetForCompte(String _compte, Integer _year, int _month) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select sum(debit) debit,sum(credit) credit from budget where compte = '" + _compte
				+ "' and year= " + _year + " and month = " + _month);
		if (rs != null && rs.next()) {
			Budget b = new Budget();
			b.setCredit(rs.getBigDecimal("credit"));
			b.setDebit(rs.getBigDecimal("debit"));
			return b;
		}
		return null;

	}

	public void backupAndReplace(List<Budget> _budgets) {
		try {

			Connection conn = DatabaseConnection.getConnection();
			Statement stmt = conn.createStatement();

			// Do backup

			String sql1 = "insert into bank.budget_backup(year, month, catego, debit, credit, notes, compte) select year,month, catego, debit, credit, notes, compte from bank.budget";
			stmt.executeUpdate(sql1);

			stmt.close();

			stmt = conn.createStatement();
			stmt.executeUpdate("truncate table bank.budget");

			stmt = conn.createStatement();

			StringBuilder sql = new StringBuilder("INSERT INTO bank.budget(year, month, catego, debit, credit, compte) values ");
			boolean first = true;
			for (Budget b : _budgets) {
				if (!first) {
					sql.append(",");
				}
				first = false;
				sql.append(String.format(Locale.ENGLISH, "(%d,%d,'%s',%.2f,%.2f,'%s')", b.getYear(), b.getMonth(), b.getCatego(),
						b.getDebit(), b.getCredit(), b.getCompte()));
			}

			System.out.println(sql);
			stmt.executeUpdate(sql.toString());
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
