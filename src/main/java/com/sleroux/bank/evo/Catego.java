package com.sleroux.bank.evo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.ValidationException;

import org.apache.log4j.Logger;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.evo.dao.OperationDao;
import com.sleroux.bank.evo.model.Operation;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.formats.OperationFormater;

public class Catego extends BusinessServiceAbstract {

	private Logger				logger						= Logger.getLogger(Catego.class);

	private OperationDao		operationDao;

	private final static int	MINIMUM_CATEGO_NAME_LENGTH	= 4;

	private List<String>		debitsCatego				= new ArrayList<>();
	private List<String>		creditsCatego				= new ArrayList<>();

	public OperationDao getOperationDao() {
		return operationDao;
	}

	public void setOperationDao(OperationDao _operationDao) {
		operationDao = _operationDao;
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

	@Override
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Categorize");

		operationDao = new OperationDao(DatabaseConnection.getConnection());

		List<Operation> operations = operationDao.getNotCategorized();
		creditsCatego = operationDao.getCreditsCatego();
		debitsCatego = operationDao.getDebitsCatego();

		for (Operation o : operations) {
			System.out.println(OperationFormater.toStringLight(o));

			List<String> suggest = operationDao.getSuggestionsFor(o.getLibelle());

			if (suggest.size() > 0) {
				System.out.println("[Enter:" + suggest.get(0) + "], [m] for more suggestions, or type catego");
			}else{
				System.out.println("No suggestion, enter catego:");
			}
			
			
			while (categoRequests(o, suggest))
				;

			Calendar c = Calendar.getInstance();
			c.setTime(o.getDateValeur());

			int opYear = c.get(Calendar.YEAR);
			int opMonth = c.get(Calendar.MONTH) + 1;

			// Shift salaire
			if (o.getCatego().equals("SALAIRE")) {
				if (c.get(Calendar.DAY_OF_MONTH) >= 20) {
					opMonth++;
					if (opMonth > 12) {
						opMonth = 1;
						opYear++;
					}
				}
			}

			o.setYear(opYear);
			o.setMonthAdjusted(opMonth);

			operationDao.saveOperation(o);

			ConsoleAppHeader.printLine();

		}

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

	protected void validate(Operation _op, String _categoName) throws ValidationException, SQLException {

		if (_categoName.length() < MINIMUM_CATEGO_NAME_LENGTH) {
			throw new ValidationException("Catego name is too short");
		}

		if (_op.getMontant().compareTo(BigDecimal.ZERO) > 0) {
			logger.debug("credit");
			if (debitsCatego.contains(_categoName)) {
				throw new ValidationException("This catego already exists for DEBITS operations");
			}
			if (creditsCatego.contains(_categoName)) {
				creditsCatego.add(_categoName);
			}

		} else {
			logger.debug("debit");
			for (String c : creditsCatego) {
				System.out.println(c);
			}
			if (creditsCatego.contains(_categoName)) {
				throw new ValidationException("This catego already exists for CREDITS operations");
			}
			if (!creditsCatego.contains(_categoName)) {
				debitsCatego.add(_categoName);
			}
		}

	}

}
