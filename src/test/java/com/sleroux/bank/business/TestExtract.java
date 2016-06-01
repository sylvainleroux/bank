package com.sleroux.bank.business;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.controller.ExtractController;
import com.sleroux.bank.util.Config;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestExtract {

	@Autowired
	ExtractController extractController;
	
	
	@Test
	@Ignore
	public void testExtractCMB() throws Exception {

		Config.loadProperties();
		
		List<String> files = extractController.runExtract();
		
		for (String f : files){
			File file = new File(f);
			Assert.assertTrue(file.isFile());
			Assert.assertTrue(file.length() > 0);
			file.delete();
		}

	}

}