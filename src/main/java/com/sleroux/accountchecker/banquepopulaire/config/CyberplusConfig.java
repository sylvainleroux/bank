package com.sleroux.accountchecker.banquepopulaire.config;

public class CyberplusConfig {

	// Configuration : Banque Populaire de l'Ouest (BPO)
	// https://www.ibps.ouest.banquepopulaire.fr/auth/UI/Login?realm=ENTAO-ENV00030
	// Portal & authentication
	protected final static String	BASE_URL				= "https://www.ibps.ouest.banquepopulaire.fr";
	protected final static String	URL_LOGIN				= BASE_URL + "/auth/UI/Login";
	protected final static String	URL_INTEGRATED_PORTAL	= BASE_URL + "/cyber/ibp/ate/portal/integratedInternet.jsp";
	protected final static String	PARAM_REALM				= "?realm=ENTAO-ENV00030";
	protected final static String	PARAM_LOGIN				= "?AMAuthCookie=";
	// Exports
	protected final static String	URL_START				= BASE_URL + "/cyber/internet/StartTask.do";
	protected final static String	URL_START_DOWNLOAD		= URL_START + "?taskInfoOID=telechargementOp&token=";
	protected final static String	URL_CONTINUE			= BASE_URL + "/cyber/internet/ContinueTask.do";
	protected final static String	URL_DOWNLOAD			= BASE_URL + "/cyber/internet/DownloadDocument.do?documentId=";

}
