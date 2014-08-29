package com.neekle.kunlunandroid.web.data;

public class TypeDicTable {

	private String conceptName;
	private String name;

	public TypeDicTable() {
		super();
	}

	public TypeDicTable(String conceptName, String name) {
		super();
		this.conceptName = conceptName;
		this.name = name;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
