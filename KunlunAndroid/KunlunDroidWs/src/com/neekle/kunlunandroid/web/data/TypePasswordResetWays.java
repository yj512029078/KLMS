package com.neekle.kunlunandroid.web.data;

import java.io.Serializable;

import com.neekle.kunlunandroid.web.data.common.TypeReturn;

public class TypePasswordResetWays implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8088217458624112454L;
	private int byEmail;
	private int byMobile;
	private int byQa;
	private TypeReturn ret;

	public TypePasswordResetWays(int byEmail, int byMobile, int byQa,
			TypeReturn typeReturnObject) {
		this.byEmail = byEmail;
		this.byMobile = byMobile;
		this.byQa = byQa;
		this.ret = typeReturnObject;
	}

	public int getByEmail() {
		return byEmail;
	}

	public void setByEmail(int byEmail) {
		this.byEmail = byEmail;
	}

	public int getByMobile() {
		return byMobile;
	}

	public void setByMobile(int byMobile) {
		this.byMobile = byMobile;
	}

	public int getByQa() {
		return byQa;
	}

	public void setByQa(int byQa) {
		this.byQa = byQa;
	}

	public TypeReturn getRet() {
		return ret;
	}

	public void setRet(TypeReturn ret) {
		this.ret = ret;
	}
}
