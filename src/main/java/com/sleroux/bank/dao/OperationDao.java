package com.sleroux.bank.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.sleroux.bank.model.Operation;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.fileimport.ExtractOperation;

public class OperationDao {

	@Autowired
	HibernateTemplate	hibernateTemplate;

	@Autowired
	DataSource			dataSource;

	public List<Operation> getAll() {
		return hibernateTemplate.loadAll(Operation.class);
	}

	public Integer create(Operation _operation) {
		hibernateTemplate.save(_operation);
		return 0;
	}

	private Logger	logger	= Logger.getLogger(OperationDao.class);

	public void doBackup() throws SQLException {

		Connection conn = getConnection();
		Statement s = conn.createStatement();
		StringBuilder sql = new StringBuilder("insert into operation_backup");
		sql.append("(compte, date_operation, date_valeur, libelle, montant, catego, year, month_bank, month_adjusted)");
		sql.append(" ");
		sql.append("select compte, date_operation, date_valeur, libelle, montant, catego, year, month_bank, month_adjusted from operation");

		s.executeUpdate(sql.toString());
		s.close();
		conn.close();

	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public void insertIngnoreOperations(ExtractDocument _doc) throws Exception {

		Connection conn = getConnection();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Statement stmt = null;
		for (ExtractOperation o : _doc.getOperations()) {
			stmt = conn.createStatement();
			String sql = "INSERT IGNORE into operation (compte, date_operation, date_valeur, libelle, montant) ";
			sql += String.format(Locale.ENGLISH, "values ('%s','%s','%s','%s', '%.2f') ", o.getAccountID(), format.format(o.getDateOperation()),
					format.format(o.getDateValeur()), o.getLibelle().replaceAll("'", ""), o.getMontant());
			logger.info(sql);
			stmt.executeUpdate(sql);
			stmt.close();
		}
		conn.close();

	}

	public List<Operation> getNotCategorized() throws SQLException {

		Connection conn = getConnection();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from operation where catego is null");
		List<Operation> ops = new ArrayList<>();
		while (rs.next()) {
			ops.add(resultSetToObject(rs));
		}
		s.close();
		conn.close();

		return ops;
	}

	public List<String> getSuggestionsFor(String _libelle) throws SQLException {

		Connection conn = getConnection();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("call get_catego('" + _libelle + "')");
		List<String> suggests = new ArrayList<>();
		while (rs.next()) {
			suggests.add(rs.getString("catego"));
		}
		s.close();
		conn.close();
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

	public void saveOperation(Operation _o) throws SQLException {
		Connection conn = getConnection();
		Statement s = conn.createStatement();
		String sql = String.format("update bank.operation set catego='%s', year=%d, month_adjusted=%d where id=%d", _o.getCatego(), _o.getYear(),
				_o.getMonthAdjusted(), _o.getId());
		logger.debug(sql);
		s.executeUpdate(sql);
		s.close();
		conn.close();

	}

	public List<String> getDebitsCatego() throws SQLException {
		Connection conn = getConnection();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select distinct catego from bank.operation where montant < 0");
		List<String> suggests = new ArrayList<>();
		while (rs.next()) {
			suggests.add(rs.getString("catego"));
		}
		rs.close();
		s.close();
		conn.close();

		return suggests;
	}

	public List<String> getCreditsCatego() throws SQLException {
		Connection conn = getConnection();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select distinct catego from bank.operation where montant > 0");
		List<String> suggests = new ArrayList<>();
		while (rs.next()) {
			suggests.add(rs.getString("catego"));
		}
		rs.close();
		s.close();
		conn.close();
		return suggests;
	}

}
