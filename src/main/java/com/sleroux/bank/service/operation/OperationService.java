package com.sleroux.bank.service.operation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.operation.Operation;

@Service
public class OperationService {

	@Autowired
	IOperationDao operationDao;

	public List<Operation> getOperations(int _year, int _month) {
		return operationDao.findByYearMonth(_year, _month);
	}

	@Transactional
	public List<Operation> findByCategoYearMonth(Integer _year, Integer _month, String _catego, String _compte) {
		return operationDao.findByCategoYearMonth(_year, _month, _catego, _compte);
	}
}
