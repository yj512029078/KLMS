package com.neekle.kunlunandroid.web.data;

/**
 * @author kevin
 * ���ܣ���ѯ�����ݽṹ�嶨�� 
 *
 */
public class TypeSqlResult {
	
	private String table;//��
	private String filter;//������

	public TypeSqlResult(){
		
	}
	
	public TypeSqlResult(String table,String filter){
		
		this.table = table;
		this.filter = filter;
		
	}
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}


}
