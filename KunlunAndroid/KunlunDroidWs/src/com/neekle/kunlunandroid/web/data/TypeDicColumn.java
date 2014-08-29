package com.neekle.kunlunandroid.web.data;

public class TypeDicColumn {

	private String conceptName;
	private String name;
	private String tableName;

	public TypeDicColumn() {
		super();
	}

	public TypeDicColumn(String conceptName, String name, String tableName) {
		super();
		this.conceptName = conceptName;
		this.name = name;
		this.tableName = tableName;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
