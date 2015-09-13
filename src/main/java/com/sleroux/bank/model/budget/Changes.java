package com.sleroux.bank.model.budget;

import java.util.List;

import com.sleroux.bank.evo.model.Budget;

public class Changes {

	private List<Budget>	created;
	private List<Budget>	updated;
	private List<Budget>	removed;

	public List<Budget> getCreated() {
		return created;
	}

	public void setCreated(List<Budget> _created) {
		created = _created;
	}

	public List<Budget> getUpdated() {
		return updated;
	}

	public void setUpdated(List<Budget> _updated) {
		updated = _updated;
	}

	public List<Budget> getRemoved() {
		return removed;
	}

	public void setRemoved(List<Budget> _removed) {
		removed = _removed;
	}

}
