package com.neekle.kunlunandroid.web.data;

import java.io.Serializable;

public class TypeDicCity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2828156391562600859L;

	private int id;
	private String name;
	private String region_code;
	private int province_id;
	private String concept_name;

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

	public String getRegion_code() {
		return region_code;
	}

	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}

	public int getProvince_id() {
		return province_id;
	}

	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}

	public String getConcept_name() {
		return concept_name;
	}

	public void setConcept_name(String concept_name) {
		this.concept_name = concept_name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
