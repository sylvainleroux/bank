package com.sleroux.bank.service.importer;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.dao.IBalanceDao;
import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.model.balance.AccountBalance;
import com.sleroux.bank.service.importer.bpo.BPOImportService;
import com.sleroux.bank.service.importer.cmb.CMBImportService;
import com.sleroux.bank.service.importer.edenred.EndenredImportService;

@Service
public class ImportService {

	private Logger			logger	= Logger.getLogger(ImportService.class);

	private ObjectMapper	mapper	= new ObjectMapper();

	@Autowired
	ApplicationContext		ctx;

	@Autowired
	private IBalanceDao		balanceDao;

	public ImportReport importFiles(ImportType _type, List<String> _files) {

		ImportReport report = new ImportReport();

		if (_type == ImportType.CMB) {
			ctx.getBean(CMBImportService.class).importFiles(_files, report);
		}

		if (_type == ImportType.BPO) {
			ctx.getBean(BPOImportService.class).importFiles(_files, report);
		}
		
		if (_type == ImportType.EDENRED){
			ctx.getBean(EndenredImportService.class).importFiles(_files, report);
		}

		return report;

	}

	@Transactional
	public void updateBalances(List<String> _balanceFiles) {
		for (String f : _balanceFiles) {
			logger.info("Import [" + f + "]");
			try {
				List<AccountBalance> balances = mapper.readValue(new File(f),
						new TypeReference<List<AccountBalance>>() {
						});

				for (AccountBalance b : balances) {
					if (b.getCompte().equals("COMPTE CHEQUES 1")) {
						b.setCompte("CMB.COMPTE_CHEQUE");
					}

					if (b.getCompte().equals("LIVRET BLEU")) {
						b.setCompte("CMB.LIVRET_CMB");
					}

					balanceDao.update(b);
				}
				
				// Delete file after import
				new File(f).delete();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
