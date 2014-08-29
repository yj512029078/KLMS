package com.neekle.kunlunandroid.data;

public class ContactDetailMoreData {
	private String tittle;
	private String content;

	public ContactDetailMoreData() {
		super();
	}

	public ContactDetailMoreData(String tittle, String content) {
		super();
		this.tittle = tittle;
		this.content = content;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
