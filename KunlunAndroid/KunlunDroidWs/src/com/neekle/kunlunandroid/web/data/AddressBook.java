package com.neekle.kunlunandroid.web.data;

import java.util.ArrayList;

public class AddressBook {
	private String id;
	private String name;
	private ArrayList<XCard> xCardList;
	private ArrayList<Folder> folderList;  

	public AddressBook() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddressBook(String id, String name, ArrayList<XCard> xCardList,
			ArrayList<Folder> folderList) {
		super();
		this.id = id;
		this.name = name;
		this.xCardList = xCardList;
		this.folderList = folderList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<XCard> getxCardList() {
		return xCardList;
	}

	public void setxCardList(ArrayList<XCard> xCardList) {
		this.xCardList = xCardList;
	}

	public ArrayList<Folder> getFolderList() {
		return folderList;
	}

	public void setFolderList(ArrayList<Folder> folderList) {
		this.folderList = folderList;
	}

}
