package com.sleroux.bank.evo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.evo.model.CalcResult;
import com.sleroux.bank.evo.model.Balance;

public class CalcDao {

	private Connection	conn;

	public CalcDao(Connection _connection) {
		conn = _connection;
	}

	public List<CalcResult> getCalcForMonth(int _year, int _month) throws SQLException {
		Statement s = conn.createStatement();
		ResultSet rs = s
				.executeQuery("select catego, is_credit, if (is_credit, ops_credit, ops_debit) ops, if (is_credit, bud_credit, bud_debit) bud from diff where year = "
						+ _year + " and month = " + _month + " order by catego");

		List<CalcResult> crlist = new ArrayList<>();
		while (rs.next()) {
			CalcResult cr = new CalcResult();
			cr.setCredit(rs.getBoolean("is_credit"));
			cr.setCatego(rs.getString("catego"));
			cr.setBud(rs.getBigDecimal("bud"));
			cr.setOps(rs.getBigDecimal("ops"));

			crlist.add(cr);
		}

		rs.close();
		s.close();
		return crlist;

	}

	public List<Balance> getBalances() throws SQLException {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from bank.soldes;");
		List<Balance> balances = new ArrayList<>();
		while (rs.next()) {
			Balance balance = new Balance();
			balance.setAccount(rs.getString("compte"));
			balance.setBalance(rs.getBigDecimal("solde"));
			balances.add(balance);
		}
		rs.close();
		s.close();
		return balances;
	}
}
