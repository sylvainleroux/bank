package com.sleroux.bank.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.jdbc.Work;

import com.sleroux.bank.model.Operation;

public class CategorizationProcStockWork implements Work {
	Operation		op;

	List<String>	suggestions	= new ArrayList<>();

	public CategorizationProcStockWork(Operation _o) {
		op = _o;
	}

	@Override
	public void execute(Connection _connection) throws SQLException {
		java.sql.Statement st = _connection.createStatement();
		
		String sql = "CALL get_catego(\"" + op.getLibelle().replaceAll("'", "").replaceAll("\"", "") + "\")";
		System.out.println(sql);
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			suggestions.add(rs.getString(1));
		}
		rs.close();
		st.close();
	}

	public List<String> getSuggestions() {
		return suggestions;
	}

}
