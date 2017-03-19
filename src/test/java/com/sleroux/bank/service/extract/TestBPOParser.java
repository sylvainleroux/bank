package com.sleroux.bank.service.extract;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.model.extract.ExtractData;

public class TestBPOParser {
	@Test
	public void testParser() throws JsonParseException, JsonMappingException, IOException {

		String content = "{\"exportType\":\"BPO\",\"content\":[{\"credit\":\"100.00\",\"date_compta\":\"28/02/2017\",\"date_ope\":\"28/02/2017\",\"date_val\":\"28/02/2017\",\"debit\":\"\",\"libelle\":\"VIR LE ROUX SYLVAIN de Le Roux Sylvain\",\"ref\":\"2QCOVLZ\"},{\"credit\":\"\",\"date_compta\":\"23/02/2017\",\"date_ope\":\"22/02/2017\",\"date_val\":\"22/02/2017\",\"debit\":\"91.44\",\"libelle\":\"ECHEANCE PRET DONT CAP 89,58 ASS. 1,86E INT. 0,00 COM. 0,00E\",\"ref\":\"8636053\"},{\"credit\":\"100.00\",\"date_compta\":\"25/01/2017\",\"date_ope\":\"25/01/2017\",\"date_val\":\"25/01/2017\",\"debit\":\"\",\"libelle\":\"VIR LE ROUX SYLVAIN MOB de LE ROUX SYLVAIN\",\"ref\":\"164FEAE\"},{\"credit\":\"\",\"date_compta\":\"24/01/2017\",\"date_ope\":\"23/01/2017\",\"date_val\":\"22/01/2017\",\"debit\":\"91.44\",\"libelle\":\"ECHEANCE PRET DONT CAP 89,58 ASS. 1,86E INT. 0,00 COM. 0,00E\",\"ref\":\"8636053\"},{\"credit\":\"\",\"date_compta\":\"05/01/2017\",\"date_ope\":\"04/01/2017\",\"date_val\":\"31/12/2016\",\"debit\":\"7.80\",\"libelle\":\"FRAIS COMPTE TRIMESTRE\",\"ref\":\"0008945\"},{\"credit\":\"100.00\",\"date_compta\":\"29/12/2016\",\"date_ope\":\"29/12/2016\",\"date_val\":\"29/12/2016\",\"debit\":\"\",\"libelle\":\"VIR LE ROUX SYLVAIN de Le Roux Sylvain\",\"ref\":\"GQR0IWW\"},{\"credit\":\"\",\"date_compta\":\"23/12/2016\",\"date_ope\":\"22/12/2016\",\"date_val\":\"22/12/2016\",\"debit\":\"91.44\",\"libelle\":\"ECHEANCE PRET DONT CAP 89,58 ASS. 1,86E INT. 0,00 COM. 0,00E\",\"ref\":\"8636053\"}],\"balance\":[{\"compte\":\"COMPTE_CHEQUE\",\"solde\":\"181.17\"}]}";

		
		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(df);
		objectMapper.readValue(content, ExtractData.class);

	}

}
