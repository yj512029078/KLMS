package com.neekle.kunlunandroid.web.data;

public class TypeDicPubsubNode {

	private String fullName;
	private String nodeName;

	public TypeDicPubsubNode() {
		super();
	}

	public TypeDicPubsubNode(String fullName, String nodeName) {
		super();
		this.fullName = fullName;
		this.nodeName = nodeName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
