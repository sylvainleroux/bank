package com.sleroux.bank.model.extract;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "extract")
public class ExtractHistory  implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long	id;

	@Column(name = "extract_date")
	private Date	extractDate;

	public long getId() {
		return id;
	}

	public void setId(long _id) {
		id = _id;
	}

	public Date getExtractDate() {
		return extractDate;
	}

	public void setExtractDate(Date _extractDate) {
		extractDate = _extractDate;
	}

}
