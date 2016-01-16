package com.sleroux.bank.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sleroux.bank.model.CalcResult;

@Repository
public class CalcDao {

	@Autowired
	DataSource	dataSource;

	public List<CalcResult> getCalcForMonth(int _year, int _month) throws SQLException {

		Connection conn = dataSource.getConnection();

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
		conn.close();
		return crlist;

	}
}
