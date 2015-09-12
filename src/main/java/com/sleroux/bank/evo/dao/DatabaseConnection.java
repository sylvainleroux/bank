package com.sleroux.bank.evo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.sleroux.bank.util.Config;

public class DatabaseConnection {

	public Connection					connection;
	public static DatabaseConnection	instance;

	public static Connection getConnection() throws Exception {
		if (instance == null) {
			instance = new DatabaseConnection();
		}

		return instance.getConnectionPrivate();
	}

	private Connection getConnectionPrivate() throws Exception {
		if (connection == null) {

			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new Exception(e.getMessage(), e);
			}
			try {
				String url = String.format("jdbc:mysql://%s:%s/bank", Config.getDBHost(), Config.getDBPort());
				System.out.println(url);
				connection = (Connection) DriverManager.getConnection(url, Config.getDBUser(), Config.getDBPass());
			} catch (SQLException e) {
				throw new Exception(e.getMessage(), e);
			}

		}
		return connection;
	}

}
