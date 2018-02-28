package com.sleroux.bank.service.importer.edenred;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.model.operation.Operation;
import com.sleroux.bank.service.importer.ImportService;
import com.sleroux.bank.service.importer.ImportType;
import com.sleroux.bank.util.Config;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestEndenredImportService {

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

	private DateFormat	format	= DateFormat.getDateInstance(SimpleDateFormat.SHORT);

	@Autowired
	ImportService		importService;

	@Autowired
	IOperationDao		operationDao;

	@Test
	@Transactional
	public void testImportService() {

		String fileName = TestEndenredImportService.class.getResource("Edenred-Export_2018-58-23-2358.json").getFile();
		ImportReport report = importService.importFiles(ImportType.EDENRED, Arrays.asList(fileName));
		List<Operation> operations = operationDao.findAll();

		for (Operation o : operations){
			System.out.println(o);
		}

		Assert.assertEquals(
				"[EDENRED.TICKET_RESTO|27/03/18|27/03/18|13h10 - ABALONE SUSHI QUIMPER,FRA QUIMPER|-16.00]",
				operations.get(0).toString());

		Assert.assertEquals(
				"[EDENRED.TICKET_RESTO|21/01/18|21/01/18|05h48 - LKXJSG|+60.20]",
				operations.get(2).toString());

	}

	@Test
	@Transactional
	public void testImportServiceDeduplicate() throws ParseException {

		DateFormat mdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
		Date d = mdf.parse("29/04/2017");

		Operation o = new Operation();
		o.setCompte("EDENRED.TICKET_RESTO");
		o.setLibelle("15h11 - AVION 0373499 150 ROUTE DE XXXXX 29000 QUIMPER");
		o.setDebit(new BigDecimal("19.00"));
		o.setDateOperation(d);
		o.setDateValeur(d);

		operationDao.create(o);

		String fileName = TestEndenredImportService.class.getResource("Edenred-Export_2017-22-08-0822.csv").getFile();

		ImportReport report = importService.importFiles(ImportType.EDENRED, Arrays.asList(fileName));
		System.out.println(report);

		List<Operation> operations = operationDao.findAll();

		Assert.assertEquals(
				"[EDENRED.TICKET_RESTO|29/04/17|29/04/17|15h11 - AVION 0373499 150 ROUTE DE XXXXX 29000 QUIMPER|-19.00]",
				operations.get(0).toString());
		Assert.assertEquals(16, report.getNbLines());
		Assert.assertEquals(15, report.getNewLines());

	}

}
