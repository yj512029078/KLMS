package com.neekle.kunlunandroid.web.data;

import java.io.Serializable;

public class TypeDicCountry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6723914803561920482L;
	private String a2Code;
	private String a3Code;
	private String conceptName;
	private String fullName;
	private int id;
	private String name;
	private String numCode;

	public String getA2Code() {
		return a2Code;
	}

	public void setA2Code(String a2Code) {
		this.a2Code = a2Code;
	}

	public String getA3Code() {
		return a3Code;
	}

	public void setA3Code(String a3Code) {
		this.a3Code = a3Code;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumCode() {
		return numCode;
	}

	public void setNumCode(String numCode) {
		this.numCode = numCode;
	}

}
