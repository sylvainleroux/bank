package com.sleroux.bank.business.extract;

import java.util.Collections;
import java.util.List;

import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.fileimport.ExtractOperation;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.persistence.dao.extract.ExtractOperationTransformer;
import com.sleroux.bank.util.EqualsUtil;
import com.sleroux.bank.util.formats.OperationFormater;

public class MergeExtract {

	private Year						year;
	private int							nbUpdates	= 0;
	private ExtractOperationTransformer	transformer	= new ExtractOperationTransformer();

	public MergeExtract(Book _book) {
		year = _book.getYears().get(_book.getYears().size() - 1);
	}

	public void merge(ExtractDocument _extract) {
		List<Operation> ops = year.getOperations();
		List<ExtractOperation> extractOps = _extract.getOperations();
		Collections.reverse(extractOps);
		for (ExtractOperation o : extractOps) {
			Operation existingOp = contains(ops, o);
			Operation newOp = transformer.transform(o);
			if (existingOp != null) {
				newOp.setCatego(existingOp.getCatego());
				newOp.setMonth(existingOp.getMonth());
				newOp.setMonthAdjusted(existingOp.getMonthAdjusted());
				ops.remove(existingOp);
				System.out.println("DUPLICATE " + OperationFormater.toString(newOp));
			} else {
				System.out.println("+NEW LINE " + OperationFormater.toString(newOp));
				newOp.setMonth(year.getLastMonthBank());
				newOp.setMonthAdjusted(year.getLastMonthAdjusted());
				nbUpdates++;
			}
			ops.add(newOp);
		}
	}

	private Operation contains(List<Operation> _ops, ExtractOperation _o) {
		for (Operation operation : _ops) {
			if (opEquals(operation, _o)) {
				return operation;
			}
		}
		return null;
	}

	public int nbNewLines() {
		return nbUpdates;
	}

	public boolean opEquals(Operation o, ExtractOperation mop) {
		return EqualsUtil.areEqual(o.getAccountID(), mop.getAccountID()) && EqualsUtil.areEqual(o.getDateCompta(), mop.getDateCompta())
				&& EqualsUtil.areEqual(o.getDateOperation(), mop.getDateOperation())
				&& EqualsUtil.areEqual(o.getLibelle(), mop.getLibelle()) && EqualsUtil.areEqual(o.getReference(), mop.getReference())
				&& EqualsUtil.areEqual(o.getDateValeur(), mop.getDateValeur()) && EqualsUtil.areEqual(o.getMontant(), mop.getMontant());
	}

}
