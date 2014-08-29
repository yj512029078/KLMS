package com.neekle.kunlunandroid.web.data;

public class XCard {
	private String id;
	private String name;
	private String jid;
	private String linkItemId;
	private String index;

	public XCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XCard(String id, String name, String jid, String linkItemId,
			String index) {
		super();
		this.id = id;
		this.name = name;
		this.jid = jid;
		this.linkItemId = linkItemId;
		this.index = index;
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

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getLinkItemId() {
		return linkItemId;
	}

	public void setLinkItemId(String linkItemId) {
		this.linkItemId = linkItemId;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
