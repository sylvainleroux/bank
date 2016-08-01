package com.sleroux.bank.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.formats.OperationFormater;

@Service
public class CategoService {

	private final static int	MINIMUM_CATEGO_NAME_LENGTH	= 4;

	private List<String>		debitsCatego				= new ArrayList<>();
	private List<String>		creditsCatego				= new ArrayList<>();

	@Autowired
	IOperationDao				operationDao;

	public void run() throws Exception {

		List<Operation> operations = operationDao.findUncategorized();
		debitsCatego = operationDao.getCategoriesDebit();
		creditsCatego = operationDao.getCategoriesCredit();

		if (operations.size() == 0) {
			System.out.println("No operation to categorize");
			return;
		}

		for (Operation o : operations) {
			System.out.println(OperationFormater.toStringLight(o));

			List<String> suggest = getCategoSuggestionsFor(o);

			if (suggest.size() > 0) {
				System.out.println("[Enter:" + suggest.get(0) + "], [m] for more suggestions, or type catego");
			} else {
				System.out.println("No suggestion, enter catego:");
			}

			while (categoRequests(o, suggest))
				;

			Calendar c = Calendar.getInstance();
			c.setTime(o.getDateValeur());

			int opYear = c.get(Calendar.YEAR);
			int opMonth = c.get(Calendar.MONTH) + 1;

			// Shift salaire to next month
			if (o.getCatego().equals("SALAIRE")) {
				if (c.get(Calendar.DAY_OF_MONTH) >= 20) {
					opMonth++;
					if (opMonth > 12) {
						opMonth = 1;
						opYear++;
					}
				}
			}

			// Shift VIR.* to next month
			if (o.getCatego().startsWith("PLMT.")) {
				if (c.get(Calendar.DAY_OF_MONTH) >= 20) {
					System.out.println("### "
							+ "Shift to following month ?");
					if (validateYesNo()) {
						opMonth++;
						if (opMonth > 12) {
							opMonth = 1;
							opYear++;
						}
					}
				}

			}

			o.setYear(opYear);
			o.setMonthAdjusted((short) opMonth);

			operationDao.update(o);

			ConsoleAppHeader.printLine();

		}

		System.out.println("Categorization completed");

	}

	public List<String> getCategoSuggestionsFor(Operation o) {
		return operationDao.getSuggestionsFor(o);
	}

	private boolean validateYesNo() {
		String r = null;
		while (r == null) {
			 System.out.println("y/n >");

			try {
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String s = bufferRead.readLine();

				if (s.equals("y") || s.equals("Y")) {
					return true;
				}

				if (s.equals("n") || s.equals("N")) {
					return false;
				}
				
				System.out.println("Wrong value");

			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		return false;
	}

	private boolean categoRequests(Operation _op, List<String> _suggest) throws SQLException {
		System.out.print(">");
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();

			if (s.equals("")) {
				_op.setCatego(_suggest.get(0));
			} else if (s.equals("m")) {
				System.out.println("display more");
				for (int i = 1; i < _suggest.size(); i++) {
					System.out.println("- " + _suggest.get(i));
				}
				return true;
			} else {
				validate(_op, s);
				_op.setCatego(s);
			}

			System.out.println("Apply category : " + _op.getCatego());
			return false;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void validate(Operation _op, String _categoName) throws ValidationException, SQLException {

		if (_categoName.length() < MINIMUM_CATEGO_NAME_LENGTH) {
			throw new ValidationException("Catego name is too short");
		}

		if (_op.getCredit().compareTo(BigDecimal.ZERO) > 0) {
			if (debitsCatego.contains(_categoName)) {
				throw new ValidationException("This catego already exists for DEBITS operations");
			}
			if (creditsCatego.contains(_categoName)) {
				creditsCatego.add(_categoName);
			}

		} else {
			if (creditsCatego.contains(_categoName)) {
				throw new ValidationException("This catego already exists for CREDITS operations");
			}
			if (!creditsCatego.contains(_categoName)) {
				debitsCatego.add(_categoName);
			}
		}

	}

	public List<String> getDebitsCatego() {
		return debitsCatego;
	}

	public void setDebitsCatego(List<String> _debitsCatego) {
		debitsCatego = _debitsCatego;
	}

	public List<String> getCreditsCatego() {
		return creditsCatego;
	}

	public void setCreditsCatego(List<String> _creditsCatego) {
		creditsCatego = _creditsCatego;
	}

	public int getNonCategorized() {
		return operationDao.findUncategorized().size();
	}

}
