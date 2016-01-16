package com.sleroux.bank;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TestConfig extends BankConfig {

	@Bean
	@Override
	public DataSource getDataSource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUrl("jdbc:mysql://localhost:3306/bank");
		datasource.setUsername("root");
		datasource.setPassword("");
		return datasource;
	}

}
