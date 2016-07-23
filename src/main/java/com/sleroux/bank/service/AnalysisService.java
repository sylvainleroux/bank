package com.sleroux.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.AnalysisDao;
import com.sleroux.bank.domain.AlertType;
import com.sleroux.bank.model.AnalysisFact;

@Service
public class AnalysisService {

	@Autowired
	AnalysisDao analysisDao;

	public int getNbFacts(int _year, int _month) {

		return getFacts(_year, _month).size();
	}

	public List<AnalysisFact> getFacts(int _year, int _month) {

		List<AnalysisFact> list = analysisDao.getFacts();
		List<AnalysisFact> filterd = new ArrayList<>();

		for (AnalysisFact a : list) {
			doAnalysis(a, _year, _month);
			if (a.getReason() == AlertType.NO_ALERT) {
				continue;
			}
			filterd.add(a);

		}

		return filterd;
	}

	protected void doAnalysis(AnalysisFact a, int _year, int _month) {

		// Debits

		if (notBudgetedDebit(a)) {
			a.setReason(AlertType.DEBIT_NOT_BUDGETED);
		}
		if (burnedBudgetDebit(a)) {
			a.setReason(AlertType.DEBIT_BURNED);
		}
		if (coolDebit(a)) {
			a.setReason(AlertType.DEBIT_OVER_ESTIMATE);
			if (a.getYear() == _year && a.getMonth() == _month) {
				a.setReason(AlertType.NO_ALERT);
			}
		}

		// Credits

		if (notBudgetedCredit(a)) {
			a.setReason(AlertType.CREDIT_NOT_BUDGETED);
		}
		if (burnedBudgetCredit(a)) {
			a.setReason(AlertType.CREDIT_BURNED);
		}
		if (coolCredit(a)) {
			a.setReason(AlertType.CREDIT_OVER_ESTIMATE);
			if (a.getYear() == _year && a.getMonth() == _month) {
				a.setReason(AlertType.NO_ALERT);
			}

		}

		// Inconsistencies

		if (categoHasDebitAndCredit(a)) {
			a.setReason(AlertType.CATEGO_CONFLICT);
		}

	}

	private boolean coolDebit(AnalysisFact _a) {
		boolean r = true;
		r = r && isNotNull(_a.getDebit_ops());
		r = r && isNotNull(_a.getDebit_bud());
		r = r && isUnset(_a.getCredit_ops());
		r = r && isUnset(_a.getCredit_bud());
		r = r && _a.getDebit_ops().compareTo(_a.getDebit_bud()) < 0;
		return r;
	}

	private boolean coolCredit(AnalysisFact _a) {
		boolean r = true;
		r = r && isUnset(_a.getDebit_ops());
		r = r && isUnset(_a.getDebit_bud());
		r = r && isNotNull(_a.getCredit_ops());
		r = r && isNotNull(_a.getCredit_bud());
		r = r && _a.getCredit_ops().compareTo(_a.getCredit_bud()) < 0;
		return r;
	}

	private boolean categoHasDebitAndCredit(AnalysisFact _a) {
		boolean r1, r2, r3, r4;
		{
			boolean r = true;
			r = r && isPositiveNotNull(_a.getDebit_ops());
			r = r && isPositiveNotNull(_a.getCredit_ops());
			r1 = r;
		}
		{
			boolean r = true;
			r = r && isPositiveNotNull(_a.getDebit_bud());
			r = r && isPositiveNotNull(_a.getCredit_bud());
			r2 = r;
		}

		{
			boolean r = true;
			r = r && isPositiveNotNull(_a.getDebit_ops());
			r = r && isPositiveNotNull(_a.getCredit_bud());
			r3 = r;
		}

		{
			boolean r = true;
			r = r && isPositiveNotNull(_a.getDebit_bud());
			r = r && isPositiveNotNull(_a.getCredit_ops());
			r4 = r;
		}

		return r1 || r2 || r3 || r4;
	}

	private boolean burnedBudgetCredit(AnalysisFact _a) {
		boolean r = true;
		r = r && isUnset(_a.getDebit_ops());
		r = r && isUnset(_a.getDebit_bud());
		r = r && isPositiveNotNull(_a.getCredit_ops());
		r = r && isPositiveNotNull(_a.getCredit_bud());
		r = r && _a.getCredit_ops().compareTo(_a.getCredit_bud()) > 0;
		return r;
	}

	private boolean notBudgetedCredit(AnalysisFact _a) {
		boolean r = true;
		r = r && isUnset(_a.getDebit_ops());
		r = r && isUnset(_a.getDebit_bud());
		r = r && isPositiveNotNull(_a.getCredit_ops());
		r = r && isUnset(_a.getCredit_bud());
		return r;
	}

	private boolean burnedBudgetDebit(AnalysisFact _a) {
		boolean r = true;
		r = r && isPositiveNotNull(_a.getDebit_ops());
		r = r && isPositiveNotNull(_a.getDebit_bud());
		r = r && isUnset(_a.getCredit_ops());
		r = r && isUnset(_a.getCredit_bud());
		r = r && _a.getDebit_ops().compareTo(_a.getDebit_bud()) > 0;
		return r;
	}

	private boolean notBudgetedDebit(AnalysisFact _a) {
		boolean r = true;
		r &= isPositiveNotNull(_a.getDebit_ops());
		r &= isUnset(_a.getCredit_ops());
		r &= isUnset(_a.getCredit_bud());
		r &= isUnset(_a.getDebit_bud());
		return r;
	}

	protected boolean isUnset(BigDecimal _credit_ops) {
		if (_credit_ops == null) {
			return true;
		}

		if (_credit_ops.setScale(2).equals(BigDecimal.ZERO.setScale(2))) {
			return true;
		}

		return false;
	}

	protected boolean isPositiveNotNull(BigDecimal _n) {
		if (_n == null) {
			return false;
		}
		if (_n.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}

		return true;
	}

	protected boolean isNotNull(BigDecimal _n) {
		if (_n == null) {
			return false;
		}
		if (_n.compareTo(BigDecimal.ZERO) < 0) {
			return false;
		}

		return true;
	}
}
