package com.sleroux.bank.business.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.filter.Filter;
import com.sleroux.bank.model.filter.FilterCollection;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.persistence.dao.filter.FiltersDaoImpl;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.formats.OperationFormater;

public class Catego extends BusinessServiceAbstract {

	private Logger						logger		= Logger.getLogger(Catego.class);

	private final static List<String>	DEBITS		= Arrays.asList("MAISON", "IMPOTS", "EAU", "ELEC", "GAZ", "TELEPHONE", "ASS.VOITURE",
															"RETRAIT", "COURSES", "CARBURANT", "RESTO", "LOISIRS", "FRAIS.BANQUE",
															"EQUIP.MAISON", "HABILLEMENT", "SANTE", "ENT.VOITURE", "ASSURANCES",
															"NOTES.FRAIS", "CADEAUX", "VACANCES", "AUTRES.DEBIT", "PLMT.PEL", "PLMT.LDD",
															"PLMT.LIVRETA", "SALAIRE", "BONUS", "VIR.LDD", "VIR.LIVRETA", "REG.CHARGES",
															"REM.CAUTION", "EMPRUNT", "REMB.SANTE", "REMBOURSEMENT", "AUTRES.CREDIT");
	private FilterCollection			filters;
	private HashMap<String, String>		categoDB	= new HashMap<String, String>();

	public void run() {
		Book book = getBookDao().getBook();
		Year lastYear = book.getYears().get(book.getYears().size() - 1);
		boolean lazzyPrintHeaderFlag = true;
		for (Year currentYear : book.getYears()) {
			for (Operation op : currentYear.getOperations()) {
				String catego = op.getCatego();
				String libelle = op.getLibelle();
				// Do not use the line if the value is 0 or null
				if (currentYear.getYear() == lastYear.getYear() && (catego == null || catego.equals(""))) {
					if (op.getMontant() == null || op.getMontant().doubleValue() == 0) {
						continue;
					}
					if (lazzyPrintHeaderFlag) {
						lazzyPrintHeaderFlag = false;
						ConsoleAppHeader.printAppHeader("CATEGORIZE");
					}
					// Search catego from existing
					try {
						if (searchCatego(op)) {
							System.out.println(OperationFormater.toStringLight(op));
							System.out.println("Found : " + op.getCatego());
							boolean valid = validateCatego();
							if (!valid) {
								displayAvailableCatego();
								System.out.println(OperationFormater.toStringLight(op));
								while (categoRequests(op))
									;
							}
						} else {
							displayAvailableCatego();
							System.out.println(OperationFormater.toStringLight(op));
							while (categoRequests(op))
								;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					ConsoleAppHeader.printLine();

				} else {
					// Build catego DB
					if (libelle != null && !libelle.equals(""))
						addEntry(libelle, catego);
				}
			}
		}
		getBookDao().saveBook(book);
		// TODO close file properly
	}

	private void displayAvailableCatego() {
		ConsoleAppHeader.printLine();
		for (int i = 1; i < 21; i++) {
			System.out.printf("%2s.%-15s%2s.%-15s%2s.%-15s\n", i, getCatego(i), i + 8, getCatego(i + 8), i + 16, getCatego(i + 16));
		}
		ConsoleAppHeader.printLine();
	}

	private boolean validateCatego() {
		System.out.print("Is catego valid ? (Y/n) >");
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();
			if (s.equals("")) {
				s = "Y";
			}
			return s.toUpperCase().equals("Y");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean categoRequests(Operation _op) {
		System.out.print(">");
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();
			try {
				int value = Integer.parseInt(s);
				String catego = getCatego(value);
				_op.setCatego(catego);
				System.out.println("Apply category : " + catego);
				return false;
			} catch (NumberFormatException e) {
				System.out.println("Incorrect value");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private String getCatego(int _i) {
		int i = _i - 1;
		String s = "";
		if (i < DEBITS.size()) {
			s = DEBITS.get(i);
		}
		return s;
	}

	public static Collection<? extends String> getCategories() {
		return DEBITS;
	}

	public void addEntry(String _libelle, String _catego) {
		categoDB.put(_libelle, _catego);
	}

	public boolean searchCatego(Operation _op) throws Exception {
		// Init filters
		if (filters == null) {
			try {
				filters = new FiltersDaoImpl().getAll();
			} catch (Exception e) {
				throw new Exception("Search catego failed", e);
			}
		}
		String libelle = _op.getLibelle();
		if (libelle.startsWith("PRLV") || libelle.startsWith("VIR")) {
			Pattern p = Pattern.compile("^([^0-9]*)");
			Matcher m = p.matcher(libelle);
			m.find();
			String compare = m.group(1);
			if (compare == null) {
				return false;
			}
			for (String s : categoDB.keySet()) {
				if (s.contains(compare)) {
					_op.setCatego(categoDB.get(s));
					return true;
				}
			}
		}
		// Exclude numbers for matching for debit cards payments
		if (libelle.startsWith("CARTE")) {
			Pattern p = Pattern.compile("CARTE (?:COMMISSION FACTURE )?[0-9]{6} CB:\\*[0-9]*(.*)");
			Matcher m = p.matcher(libelle);
			m.find();
			String compare = m.group(1);
			for (String s : categoDB.keySet()) {
				if (s.contains(compare)) {
					_op.setCatego(categoDB.get(s));
					return true;
				}
			}
		}

		// Execute config rules
		boolean categoFound = false;
		for (Filter filter : filters.getFilters()) {
			Logger.getRootLogger().debug(filter.getStartsWith());
			if (filter.getStartsWith() != null) {

				boolean validFilter = true;
				validFilter = checkStartsWith(filter, _op) && validFilter;
				validFilter = checkContains(filter, _op) && validFilter;
				validFilter = checkReference(filter, _op) && validFilter;

				if (validFilter) {
					_op.setCatego(filter.getCatego());
					return true;
				}
			}
		}
		return categoFound;
	}

	private boolean checkReference(Filter _filter, Operation _op) {
		if (_filter.getReference() == null || _filter.getReference().size() == 0) {
			// This rule not have to be evaluated
			return true;
		}
		if (_op.getReference() == null || _op.getReference().equals("")) {
			logger.debug("Empty reference, filter can't be evaluated" + _filter);
			return false;
		}
		boolean contains = false;
		for (String s : _filter.getReference()) {
			contains = contains || _op.getReference().contains(s);
		}
		return contains;

	}

	private boolean checkContains(Filter _filter, Operation _op) {
		if (_filter.getContains() == null || _filter.getContains().size() == 0) {
			// This rule not have to be evaluated
			return true;
		}
		if (_op.getLibelle() == null || _op.getLibelle().equals("")) {
			logger.debug("Empty libelle, filter can't be evaluated");
			return false;
		}
		boolean contains = false;
		for (String s : _filter.getContains()) {
			contains = contains || _op.getLibelle().contains(s);
		}
		return contains;
	}

	private boolean checkStartsWith(Filter _filter, Operation _op) {
		if (_filter.getStartsWith() == null || _filter.getStartsWith().size() == 0) {
			// This rule not have to be evaluated
			return true;
		}
		if (_op.getLibelle() == null || _op.getLibelle().equals("")) {
			logger.debug("Empty libelle, filter can't be evaluated");
			return false;
		}
		boolean found = false;
		for (String s : _filter.getStartsWith()) {
			found = found || _op.getLibelle().startsWith(s);
		}
		return found;
	}
}
