package com.neekle.kunlunandroid.web.data;

import java.util.ArrayList;

public class Folder {
	private String id;
	private String name;
	private String index;
	private String parentId;
	private ArrayList<XCard> xCardList;
	private ArrayList<Folder> folderList;

	public Folder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Folder(String id, String name, String index, String parentId,
			ArrayList<XCard> xCardList, ArrayList<Folder> folderList) {
		super();
		this.id = id;
		this.name = name;
		this.index = index;
		this.parentId = parentId;
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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
