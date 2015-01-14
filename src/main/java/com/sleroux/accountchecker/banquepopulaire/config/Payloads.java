package com.sleroux.accountchecker.banquepopulaire.config;

import com.sleroux.accountchecker.banquepopulaire.util.PostData;

public class Payloads extends CyberplusConfig {

	public static PostData login(String _login, String _password) {
		return new PostData().add("IDToken1", _login).add("IDToken2", _password)
				.add("goto", "aHR0cHM6Ly93d3cuaWJwcy5vdWVzdC5iYW5xdWVwb3B1bGFpcmUuZnI6NDQzL2N5YmVyL2ludGVybmV0L0xvZ2luLmRv")
				.add("encoded", "true");
	}

	public static PostData exportSelectFormat(String _taskOID, String _token) {
		return new PostData().add("dialogActionPerformed", "SELECTION_FORMAT").add("taskOID", _taskOID)
				.add("taskInfoOID", "telechargementOp")
				.add("screenName", "ibp.cp.qbel.ecrtlc10.caracteristiques.screen_HTML_INTERNET:Telechgt.Caracteristiques")
				.add("validationStrategy", "AV")
				.add("formModified", "true")
				.add("token", _token)
				.add("attribute($SEL_$lst32)", "3")
				.add("attribute($SEL_$lst32_hidden)", "lst32$listeTypeOperations$typeOperationSelectionnee$")
				.add("attribute($SEL_$lst33)", "1")
				.add("attribute($SEL_$lst33_hidden)", "lst33$listeFormatsFichier$formatFichierSelectionne$")
				.add("attribute($SEL_$lst31)", "OFX_LOGICIEL_3")
				.add("attribute($SEL_$lst31_hidden)", "lst31$listeOpt1OFX$opt1OFXSelectionnee$");
	}

	public static PostData exportSelectOptions(String _taskOID, String _token) {
		return new PostData()
				.add("dialogActionPerformed", "SUIVANT")
				.add("taskOID", _taskOID)
				.add("taskInfoOID", "telechargementOp")
				.add("screenName", "ibp.cp.qbel.ecrtlc10.caracteristiques.screen_HTML_INTERNET:Telechgt.Caracteristiques")
				.add("validationStrategy", "AV")
				.add("formModified", "true")
				.add("token", _token)
				.add("attribute($SEL_$lst32)", "3")
				.add("attribute($SEL_$lst32_hidden)", "lst32$listeTypeOperations$typeOperationSelectionnee$")
				.add("attribute($SEL_$lst33)", "1")
				.add("attribute($SEL_$lst33_hidden)", "lst33$listeFormatsFichier$formatFichierSelectionne$")
				.add("attribute($SEL_$lst34)", "CSV_DATE_2")
				.add("attribute($SEL_$lst34_hidden)", "lst34$listeOpt1CSV$opt1CSVSelectionnee$")
				.add("attribute($SEL_$lst35)", "CSV_SEPARATEUR_CHAMP_4")
				.add("attribute($SEL_$lst35_hidden)", "lst35$listeOpt2CSV$opt2CSVSelectionnee$")
				.add("attribute($SEL_$lst36)", "CSV_SEPARATEUR_DECIMALE_1")
				.add("attribute($SEL_$lst36_hidden)", "lst36$listeOpt3CSV$opt3CSVSelectionnee$");
	}

	public static PostData exportSelectAccount(
			String _taskOID, 
			String _token, 
			String _dateStart,
			String _dateEnd, 
			String _codeBanque,
			String _account
			) {
		return new PostData()
				.add("dialogActionPerformed", "TELECHARGER")
				.add("taskOID", _taskOID)
				.add("taskInfoOID", "telechargementOp")
				.add("screenName", "ibp.cp.qbel.ecrtlc10.selectioncomptes.screen_HTML_INTERNET:Telechgt.SelectionComptes")
				.add("validationStrategy", "AV")
				.add("formModified", "true")
				.add("token", _token)
				.add("attribute($SEL_$tbl10_hidden)", "tbl10$listeContratsSelectionnes$")
				.add("listeInfosContrats[0].dateDernierTelechgt", _dateStart)
				.add("listeInfosContrats[0].dateFinTelechgt", _dateEnd)
				.add("attribute($SEL_$tbl10)", "codeBanque=" + _codeBanque + "&identifiant=CPT" + _account)
				.add("attribute($SEL_$tbl11_hidden)", "tbl11$listeContratsEpargneSelectionnes$");
	}
}
