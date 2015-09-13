package com.sleroux.bank.evo.dao;

import java.math.BigDecimal;

import org.junit.Test;

import com.sleroux.bank.model.budget.Changes;
import com.sleroux.bank.util.Config;

public class BudgetDaoTest {

	@Test
	public void testCreateChange() throws Exception {

		Config.loadProperties();
		BudgetDao budgetDao = new BudgetDao(DatabaseConnection.getConnection());
		
		Changes changes = new Changes();
		changes.setYear(2015);
		changes.setMonth(9);
		changes.setCatego("SPORT");
		changes.setCompte("NOCOMPTE");
		changes.setNotes("avion");
		changes.setCredit(new BigDecimal("100000"));
		changes.setOldCredit(new BigDecimal("100"));
		budgetDao.saveChange(changes);

	}

}
