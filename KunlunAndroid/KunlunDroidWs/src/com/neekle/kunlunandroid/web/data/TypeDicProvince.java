package com.neekle.kunlunandroid.web.data;

import java.io.Serializable;

public class TypeDicProvince implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -664874211574463862L;
	private String conceptName;
	private int countryID;
	private int id;
	private String name;
	private String regionCode;

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public int getCountryID() {
		return countryID;
	}

	public void setCountryID(int countryID) {
		this.countryID = countryID;
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

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
}
