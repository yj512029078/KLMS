package com.neekle.kunlunandroid.web.data.friend;

public class TypeAddsbook {

	private String id;
	private int isPublic;
	private String name;
	private String ownerEntid;

	public TypeAddsbook() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TypeAddsbook(String id, int isPublic, String name, String ownerEntid) {
		super();
		this.id = id;
		this.isPublic = isPublic;
		this.name = name;
		this.ownerEntid = ownerEntid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerEntid() {
		return ownerEntid;
	}

	public void setOwnerEntid(String ownerEntid) {
		this.ownerEntid = ownerEntid;
	}

}
