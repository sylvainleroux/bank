package com.sleroux.bank.evo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

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
}
