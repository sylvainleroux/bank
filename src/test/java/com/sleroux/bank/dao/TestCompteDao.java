package com.sleroux.bank.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.Compte;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestCompteDao {
	@Autowired
	ICompteDao compteDao;

	@Test
	@Transactional
	public void testGetAccounts() {

		Compte compte = new Compte();

		compte.setNom("BANK.ACCOUNT_NAME");
		compte.setType("CHECKING");
		compteDao.create(compte);

		List<Compte> list = compteDao.findAll();

		Assert.assertEquals("List of account size", 1, list.size());
	}

	@Test(expected = Exception.class)
	@Transactional
	public void testIncorrectType() {

		Compte compte = new Compte();

		compte.setNom("BANK.ACCOUNT_NAME");
		compte.setType(null);
		compteDao.create(compte);

		List<Compte> list = compteDao.findAll();

		Assert.assertEquals("List of account size", 1, list.size());
	}
}
