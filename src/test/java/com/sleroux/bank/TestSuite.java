package com.sleroux.bank;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.sleroux.bank.business.TestCalc;
import com.sleroux.bank.business.TestExtract;
import com.sleroux.bank.business.TestImport;
import com.sleroux.bank.dao.TestBudgetDao;
import com.sleroux.bank.dao.TestOperationDao;
import com.sleroux.bank.model.statement.TestYear;
import com.sleroux.bank.service.TestCatego;
import com.sleroux.bank.service.TestCategoValidation;

@RunWith(Suite.class)
@SuiteClasses({ 
	// Controllers
	TestCalc.class,
	TestExtract.class,
	TestImport.class, 
	// Services
	TestCatego.class,
	TestCategoValidation.class, 
	// Dao
	TestOperationDao.class, 
	TestBudgetDao.class, 
	// Misc
	TestEncrypt.class, 
	TestGraph.class,
	TestYear.class
})
public class TestSuite {

}
