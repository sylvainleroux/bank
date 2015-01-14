package com.sleroux.bank;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sleroux.bank.business.app.Catego;
import com.sleroux.bank.model.filter.FilterCollection;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.persistence.dao.filter.FiltersDao;
import com.sleroux.bank.persistence.dao.filter.FiltersDaoImpl;
import com.sleroux.bank.util.Config;

public class TestFilters {

	@BeforeClass
	public static void loadConfig() {
		try {
			Config.loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetFilters() {
		FiltersDao filtersDao = new FiltersDaoImpl();
		FilterCollection collection;
		try {
			collection = filtersDao.getAll();
			Assert.assertNotNull(collection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCatego() {
		Catego catego = new Catego();
		catego.addEntry("PRLV VEOLIA EAU CGE OUES 00000000000000000000 N.EMETTEUR: 000000", "EAU");
		Operation o = new Operation();
		o.setLibelle("PRLV VEOLIA EAU CGE OUES 00000000000000000000 N.EMETTEUR: 000000");
		try {
			Assert.assertTrue(catego.searchCatego(o));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals("EAU", o.getCatego());
	}

	@Test
	public void testPret() {
		Catego catego = new Catego();
		Operation o = new Operation();
		o.setLibelle("ECHEANCE PRET DONT CAP     00,00 ASS.    0,00E INT.     0,00 COM.    0,00E");
		o.setReference("TEST123");
		try {
			Assert.assertTrue(catego.searchCatego(o));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals("PRETS", o.getCatego());
	}

	@Test
	public void testSalary() {
		Catego catego = new Catego();
		Operation o = new Operation();
		o.setLibelle("VIR DE      X XXX,XXEUR /ROC/XXXXXX//SALARY ALENTY SAS");
		try {
			Assert.assertTrue(catego.searchCatego(o));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals("SALAIRE", o.getCatego());
	}
	
	@Ignore
	@Test
	public void testCustomFilter(){
		Catego catego = new Catego();
		Operation o = new Operation();
		o.setLibelle("COTIS EQUIPAGE PRIVILEGE");
		try {
			Assert.assertTrue(catego.searchCatego(o));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals("FRAIS.BANQUE", o.getCatego());
	}

}
