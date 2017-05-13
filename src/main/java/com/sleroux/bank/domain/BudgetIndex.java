package com.sleroux.bank.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sleroux.bank.model.budget.Budget;

@SuppressWarnings("serial")
public class BudgetIndex {

	private final static String SOLDE_INIT = "SOLDE_INIT";

	public List<String> getCredits(String _compte) {
		Stream<List<String>> stream = map.values().stream().map(y -> y.getCredits(_compte));
		return stream.reduce(new ArrayList<String>(), (a, b) -> {
			for (String s : b) {
				if (!a.contains(s)) {
					a.add(s);
				}
			}
			return a;
		}).stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());

	}

	public List<String> getDebits(String _compte) {
		Stream<List<String>> stream = map.values().stream().map(y -> y.getDebits(_compte));
		return stream.reduce(new ArrayList<String>(), (a, b) -> {
			for (String s : b) {
				if (!a.contains(s)) {
					a.add(s);
				}
			}
			return a;
		}).stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());
	}

	public class Year extends LinkedHashMap<Integer, Month> {

		public HashMap<String, BigDecimal> reduce() {
			return this.values().stream().map(m -> m.reduce()).reduce(new HashMap<String, BigDecimal>(), (a, b) -> {

				for (java.util.Map.Entry<String, BigDecimal> month : b.entrySet()) {
					if (a.containsKey(month.getKey())) {
						a.put(month.getKey(), a.get(month.getKey()).add(month.getValue()));
					} else {
						a.put(month.getKey(), month.getValue());
					}
				}
				return a;
			});
		}

		public List<String> getCredits(String _compte) {
			return this.values().stream().map(m -> m.getCredits(_compte)).collect(ArrayList::new, List::addAll,
					List::addAll);
		}

		public List<String> getDebits(String _compte) {
			return this.values().stream().map(m -> m.getDebits(_compte)).collect(ArrayList::new, List::addAll,
					List::addAll);
		}
	}

	private class Month extends LinkedHashMap<String, Compte> {

		public HashMap<String, BigDecimal> reduce() {
			return (HashMap<String, BigDecimal>) this.entrySet().stream()
					.collect(Collectors.toMap(e -> (String) e.getKey(), e -> (BigDecimal) e.getValue().reduce()));
		}

		public void addSoldesInit(HashMap<String, BigDecimal> _reduced) {
			_reduced.forEach((key, value) -> {
				if (this.containsKey(key)) {
					this.get(key).addSoldeInit(value);
				} else {
					Compte c = new Compte();
					c.put(SOLDE_INIT, createSoldeInit(value));
					this.put(key, c);
				}
			});
		}

		public List<String> getCredits(String _compte) {
			return this.entrySet().stream().filter(e -> e.getKey().equals(_compte)).map(e -> {
				return e.getValue();
			}).map(Compte::getCredits).collect(ArrayList::new, List::addAll, List::addAll);
		}

		public List<String> getDebits(String _compte) {
			return this.entrySet().stream().filter(e -> e.getKey().equals(_compte)).map(e -> {
				return e.getValue();
			}).map(Compte::getDebits).collect(ArrayList::new, List::addAll, List::addAll);
		}

	}

	private class Compte extends LinkedHashMap<String, Budget> {

		public BigDecimal reduce() {
			return this.values().stream().map(b -> b.getCredit().subtract(b.getDebit())).reduce(BigDecimal.ZERO,
					(a, b) -> a.add(b));
		}

		public void addSoldeInit(BigDecimal _value) {

			if (_value.intValue() == 0) {
				return;
			}

			Budget soldeInit = this.get(SOLDE_INIT);
			if (soldeInit == null) {
				this.put(SOLDE_INIT, createSoldeInit(_value));
			} else {
				this.put(SOLDE_INIT, augmentSoldeInit(soldeInit, _value));
			}
		}

		public List<String> getCredits() {
			return this.values().stream().filter(budget -> budget.getCredit().compareTo(BigDecimal.ZERO) > 0)
					.map(budget -> budget.getCatego()).collect(Collectors.toList());
		}

		public List<String> getDebits() {
			return this.values().stream().filter(budget -> budget.getDebit().compareTo(BigDecimal.ZERO) > 0)
					.map(budget -> budget.getCatego()).collect(Collectors.toList());
		}

	}

	private Budget createSoldeInit(BigDecimal _value) {
		Budget b = new Budget();
		b.setCatego(SOLDE_INIT);
		if (_value.compareTo(BigDecimal.ZERO) > 0) {
			b.setCredit(_value);
		} else {
			b.setDebit(_value);
		}

		return b;
	}

	private Budget augmentSoldeInit(Budget _soldeInit, BigDecimal _value) {
		if (_value.compareTo(BigDecimal.ZERO) > 0) {
			_soldeInit.setCredit(_soldeInit.getCredit().add(_value));
		} else {
			_soldeInit.setDebit(_soldeInit.getDebit().add(_value));
		}
		return _soldeInit;
	}

	public HashMap<Integer, Year> map = new LinkedHashMap<>();

	public BudgetIndex(List<Budget> _db) {

		for (Budget b : _db) {
			
			Year y = map.get(b.getYear());
			if (y == null) {
				y = new Year();
				map.put(b.getYear(), y);
			}

			Month m = y.get(b.getMonth());
			if (m == null) {
				m = new Month();
				y.put(b.getMonth(), m);
			}

			Compte c = m.get(b.getCompte());
			if (c == null) {
				c = new Compte();
				m.put(b.getCompte(), c);
			}

			c.put(b.getCatego(), b);

		}
	}

	public Budget find(Budget _b) {
		Year y = map.get(_b.getYear());
		if (y != null) {
			Month m = y.get(_b.getMonth());
			if (m != null) {
				Compte c = m.get(_b.getCompte());
				if (c != null) {
					return c.get(_b.getCatego());
				}
			}
		}

		return null;
	}

	public List<Budget> find(int _year, int _month, String _compte) {
		Year y = map.get(_year);
		if (y != null) {
			Month m = y.get(_month);
			if (m != null) {
				Compte c = m.get(_compte);
				if (c != null) {
					return new ArrayList<>(c.values());
				}
			}
		}
		return new ArrayList<>();
	}

	public List<Integer> getYears() {
		return new ArrayList<>(map.keySet());
	}

	public void firstYear(int _firstYear) {

		HashMap<String, BigDecimal> reduced = map.entrySet().stream().filter(e -> e.getKey() < _firstYear)
				.map(e -> e.getValue()).map(y -> y.reduce()).reduce(new HashMap<String, BigDecimal>(), (a, b) -> {
					for (java.util.Map.Entry<String, BigDecimal> month : b.entrySet()) {
						if (a.containsKey(month.getKey())) {
							a.put(month.getKey(), a.get(month.getKey()).add(month.getValue()));
						} else {
							a.put(month.getKey(), month.getValue());
						}
					}
					return a;
				});

		for (Iterator<Map.Entry<Integer, Year>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, Year> entry = it.next();
			if (entry.getKey() < _firstYear) {
				it.remove();
			} else {
				// Restore solde init
				Year y = entry.getValue();
				Iterator<Map.Entry<Integer, Month>> it2 = y.entrySet().iterator();
				if (it2.hasNext()) {
					Month first = it2.next().getValue();
					first.addSoldesInit(reduced);
				}
				break;
			}
		}

	}

}
