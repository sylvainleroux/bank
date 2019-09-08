package com.sleroux.bank.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.jdbc.Work;

import com.sleroux.bank.model.operation.Operation;

public class CategorizationProcStockWork implements Work {
	Operation		op;

	List<String>	suggestions	= new ArrayList<>();

	public CategorizationProcStockWork(Operation _o) {
		op = _o;
	}

	@Override
	public void execute(Connection _connection) throws SQLException {
		java.sql.Statement st = _connection.createStatement();
		BigDecimal montant = op.getCredit().subtract(op.getDebit());
		
		String libelle = op.getLibelle().replaceAll("'", "").replaceAll("\"", "") ;
		
		String sql = "CALL get_catego2(\"" + libelle+ "\", '" + op.getCompte() + "'," + montant + ")";
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
