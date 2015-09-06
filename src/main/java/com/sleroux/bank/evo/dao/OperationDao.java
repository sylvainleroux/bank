package com.sleroux.bank.evo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.sleroux.bank.evo.model.Operation;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.fileimport.ExtractOperation;

public class OperationDao {

	private Connection	conn;

	private Logger		logger	= Logger.getLogger(OperationDao.class);

	public OperationDao(Connection _conn) {
		conn = _conn;
	}

	public void doBackup() throws SQLException {

		Statement s = conn.createStatement();
		StringBuilder sql = new StringBuilder("insert into backup_operation");
		sql.append("(compte, date_operation, date_valeur, libelle, montant, catego, year, month_bank, month_adjusted)");
		sql.append(" ");
		sql.append("select compte, date_operation, date_valeur, libelle, montant, catego, year, month_bank, month_adjusted from operation");

		s.executeUpdate(sql.toString());
		s.close();

	}

	public void insertIngnoreOperations(ExtractDocument _doc) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Statement stmt = null;
		for (ExtractOperation o : _doc.getOperations()) {
			stmt = conn.createStatement();
			String sql = "INSERT IGNORE into operation (compte, date_operation, date_valeur, libelle, montant) ";
			sql += String.format(Locale.ENGLISH, "values ('%s','%s','%s','%s', '%.2f') ", o.getAccountID(),
					format.format(o.getDateOperation()), format.format(o.getDateValeur()), o.getLibelle(), o.getMontant());
			logger.info(sql);
			stmt.executeUpdate(sql);
			stmt.close();
		}

	}

	public List<Operation> getNotCategorized() throws SQLException {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from operation where catego is null");
		List<Operation> ops = new ArrayList<>();
		while (rs.next()) {
			ops.add(resultSetToObject(rs));
		}
		s.close();
		return ops;
	}
	

	public List<String> getSuggestionsFor(String _libelle) throws SQLException {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("call get_catego('" + _libelle + "')");
		List<String> suggests = new ArrayList<>();		
		while (rs.next()){
			suggests.add(rs.getString("catego"));
		}
		s.close();
		return suggests;
	}

	private Operation resultSetToObject(ResultSet rs) throws SQLException {
		Operation o = new Operation();
		o.setId(rs.getInt("id"));
		o.setCompte(rs.getString("compte"));
		o.setDateOperation(rs.getDate("date_operation"));
		o.setDateValeur(rs.getDate("date_valeur"));
		o.setLibelle(rs.getString("libelle"));
		o.setMontant(rs.getBigDecimal("montant"));
		o.setCatego(rs.getString("catego"));
		o.setYear(rs.getInt("year"));
		o.setMonthBank(rs.getInt("month_bank"));
		o.setMonthAdjusted(rs.getInt("month_adjusted"));
		return o;
	}

}
