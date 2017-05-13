package com.sleroux.bank.model.compte;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "compte")
public class Compte implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int		id;

	private String	nom;

	private String	type;

	public int getId() {
		return id;
	}

	public void setId(int _id) {
		id = _id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String _nom) {
		nom = _nom;
	}

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		type = _type;
	}

	@Override
	public String toString() {
		return "Compte [id=" + id + ", nom=" + nom + ", type=" + type + "]";
	}
	
	

}
