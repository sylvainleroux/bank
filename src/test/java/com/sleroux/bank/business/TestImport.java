package com.sleroux.bank.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.dao.IBalanceDao;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.operation.Operation;
import com.sleroux.bank.service.importer.ImportService;
import com.sleroux.bank.service.importer.ImportType;
import com.sleroux.bank.util.Config;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestImport {

	@BeforeClass
	public static void loadConfig() {
		try {
			Config.loadProperties();

			Config.setArchiveImportFiles(false);
			Config.setDeleteImportFile(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Autowired
	ImportService	importService;

	@Autowired
	IOperationDao	operationDao;

	@Autowired
	IBalanceDao		balanceDao;

	@Test
	@Transactional
	public void testImportCMB() throws Exception {

		List<String> files = new ArrayList<>();
		String f = TestImport.class.getResource("RELEVE_2016_01_18_CMB_last5weeks.csv").getFile();
		files.add(new File(f).toString());

		importService.importFiles(ImportType.CMB, files);

		List<Operation> list = operationDao.findAll();

		Assert.assertEquals("[CMB.COMPTE_CHEQUE|15/01/16|15/01/16|PRLV PayPal Europe S.a.r.l. et C|-12.86]",
				list.get(0).toString());
		Assert.assertEquals("[CMB.COMPTE_CHEQUE|23/12/15|22/12/15|CARTE 22/12 IKEA XXXXXXX0060/ DUPLICATE(2)|-75.40]",
				list.get(11).toString());

	}

	@Test
	@Transactional
	public void testImportBalances() throws Exception {

		List<String> files = new ArrayList<>();

		String f2 = TestImport.class.getResource("CMB_BALANCE.json").getFile();
		files.add(new File(f2).toString());

		importService.updateBalances(files);

		Assert.assertEquals(2, balanceDao.findAll().size());
	}

}
