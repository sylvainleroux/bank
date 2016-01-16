package com.sleroux.bank.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;

@Service
@Lazy
public class BudgetDao {

	@Autowired
	DataSource	datasource;

	public void createAll(List<Budget> _list) {

		try {
			Connection conn = datasource.getConnection();
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

		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct catego from bank.budget where credit > 0 and compte = 'COURANT' order by catego");

		List<String> catego = new ArrayList<>();
		while (rs.next()) {
			catego.add(rs.getString("catego"));
		}

		conn.close();

		return catego;
	}

	public List<String> getDebits() throws Exception {

		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct catego from bank.budget where debit > 0 and compte = 'COURANT' order by catego");

		List<String> catego = new ArrayList<>();
		while (rs.next()) {
			catego.add(rs.getString("catego"));
		}

		conn.close();

		return catego;
	}

	public List<Integer> getYears() throws SQLException {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct year from bank.budget order by year");
		List<Integer> list = new ArrayList<Integer>();
		while (rs.next()) {
			list.add(new Integer(rs.getInt("year")));
		}
		conn.close();
		return list;
	}

	public List<Budget> getMonthCredits(Integer _year, int _month) throws SQLException {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("call bank.getMonthCredits(" + _year + "," + _month + ")");
		List<Budget> list = new ArrayList<>();
		while (rs.next()) {
			Budget b = new Budget();
			b.setCatego(rs.getString("catego"));
			b.setCredit(rs.getBigDecimal("credit"));
			list.add(b);
		}
		conn.close();
		return list;
	}

	public List<Budget> getMonthDebits(Integer _year, int _month) throws SQLException {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("call bank.getMonthDebits(" + _year + "," + _month + ")");
		List<Budget> list = new ArrayList<>();
		while (rs.next()) {
			Budget b = new Budget();
			b.setCatego(rs.getString("catego"));
			b.setDebit(rs.getBigDecimal("debit"));
			list.add(b);
		}
		conn.close();
		return list;
	}

	public List<String> getComptesEpargne() throws SQLException {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select distinct compte from budget where compte <> 'COURANT' order by compte");
		List<String> list = new ArrayList<>();
		while (rs.next()) {
			list.add(rs.getString("compte"));
		}
		conn.close();
		return list;

	}

	public Budget getBudgetForCompte(String _compte, Integer _year, int _month) throws SQLException {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select sum(debit) debit,sum(credit) credit from budget where compte = '" + _compte
				+ "' and year= " + _year + " and month = " + _month);
		if (rs != null && rs.next()) {
			Budget b = new Budget();
			b.setCredit(rs.getBigDecimal("credit"));
			b.setDebit(rs.getBigDecimal("debit"));
			conn.close();
			return b;
		}
		conn.close();
		return null;

	}

	public void backupAndReplace(List<Budget> _budgets) {
		try {
			Connection conn = datasource.getConnection();
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
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Changes> getAdded() throws Exception {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select a.id, a.year, a.month, a.catego, a.debit, a.credit, a.compte, a.notes from budget a left join (select * from budget_backup inner join (select max(timestamp) last_backup from budget_backup) t on t.last_backup = timestamp) b on a.year = b.year and a.month = b.month and a.catego = b.catego and a.compte = b.compte where  b.timestamp is null";
		List<Changes> list = new ArrayList<>();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			Changes b = new Changes();
			b.setYear(rs.getInt("year"));
			b.setMonth(rs.getInt("month"));
			b.setCatego(rs.getString("catego"));
			b.setCompte(rs.getString("compte"));
			b.setCredit(rs.getBigDecimal("credit"));
			b.setDebit(rs.getBigDecimal("debit"));
			b.setNotes(rs.getString("notes"));
			list.add(b);
		}
		conn.close();
		return list;
	}

	public List<Changes> getUpdated() throws Exception {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select a.year year, a.month month, a.catego catego, a.compte compte, a.debit debit, a.credit credit, a.notes, b.debit old_debit, b.credit old_credit from budget a inner join budget_backup b on a.year = b.year and a.month = b.month and a.catego = b.catego and a.compte = b.compte inner join (select max(timestamp) last_backup from budget_backup) t on b.timestamp = t.last_backup where a.debit <> b.debit or a.credit <> b.credit;";
		List<Changes> list = new ArrayList<>();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			Changes b = new Changes();
			b.setYear(rs.getInt("year"));
			b.setMonth(rs.getInt("month"));
			b.setCatego(rs.getString("catego"));
			b.setCompte(rs.getString("compte"));
			b.setCredit(rs.getBigDecimal("credit"));
			b.setDebit(rs.getBigDecimal("debit"));
			b.setNotes(rs.getString("notes"));
			b.setOldCredit(rs.getBigDecimal("old_credit"));
			b.setOldDebit(rs.getBigDecimal("old_debit"));
			list.add(b);
		}
		rs.close();
		stmt.close();
		conn.close();
		return list;
	}

	public List<Changes> getDeleted() throws Exception {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select a.year, a.month, a.catego, a.compte, a.debit, a.credit, a.notes from budget_backup a inner join (select max(timestamp) last_backup from budget_backup) t on a.timestamp = t.last_backup left join budget b on a.year = b.year and a.month = b.month and a.catego = b.catego and a.compte = b.compte where  b.id is null";
		List<Changes> list = new ArrayList<>();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			Changes b = new Changes();
			b.setYear(rs.getInt("year"));
			b.setMonth(rs.getInt("month"));
			b.setCatego(rs.getString("catego"));
			b.setCompte(rs.getString("compte"));
			b.setOldCredit(rs.getBigDecimal("credit"));
			b.setOldDebit(rs.getBigDecimal("debit"));
			b.setCredit(null);
			b.setDebit(null);
			b.setNotes(rs.getString("notes"));
			list.add(b);
		}
		rs.close();
		stmt.close();
		conn.close();
		return list;
	}

	public void saveChange(Changes _change) throws Exception {
		Connection conn = datasource.getConnection();
		Statement stmt = conn.createStatement();

		StringBuilder sql = new StringBuilder();
		sql.append("insert into bank.budget_changes (year, month, catego, compte, type, prev_value, new_value) values (");
		sql.append(_change.getYear());
		sql.append(",");
		sql.append(_change.getMonth());
		sql.append(",");
		sql.append("\"");
		sql.append(_change.getCatego());
		sql.append("\"");
		sql.append(",");
		sql.append("\"");
		sql.append(_change.getCompte());
		sql.append("\"");
		sql.append(",");
		sql.append("\"");
		sql.append((_change.isCredit() ? "CREDIT" : "DEBIT"));
		sql.append("\"");
		sql.append(",");
		sql.append(_change.isCredit() ? _change.getOldCredit() : _change.getOldDebit());
		sql.append(",");
		sql.append(_change.isCredit() ? _change.getCredit() : _change.getDebit());
		sql.append(")");

		System.out.println(sql.toString());
		stmt.executeUpdate(sql.toString());

		stmt.close();
		conn.close();
	}

}
