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
import com.sleroux.bank.controller.ImportController;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.service.ImportType;
import com.sleroux.bank.util.Config;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestImport {

	@BeforeClass
	public static void loadConfig() {
		try {
			Config.loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Autowired
	ImportController	importController;

	@Autowired
	IOperationDao		operationDao;

	@Test
	@Transactional
	public void testImportCMB() throws Exception {

		List<String> files = new ArrayList<>();
		String f = TestImport.class.getResource("RELEVE_2016_01_18_CMB_last5weeks.csv").getFile();
		files.add(new File(f).toString());

		importController.importFiles(ImportType.CMB, files);

		List<Operation> list = operationDao.findAll();

		Assert.assertEquals("[CMB|15/01/16|15/01/16|PRLV PayPal Europe S.a.r.l. et C|-12.86]", list.get(0).toString());
		Assert.assertEquals("[CMB|23/12/15|22/12/15|CARTE 22/12 IKEA XXXXXXX0060/ DUPLICATE(2)|-75.40]", list.get(11).toString());

	}

}
