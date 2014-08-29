package com.neekle.kunlunandroid.xmpp.data;

import com.neekle.kunlunandroid.R.string;

public class XmppJid {

	private static final String SPLIT_STR_AFTER_USERNAME = "@";
	private static final String SPLIT_STR_BEFORE_RESOURCE = "/";

	private String full;
	private String bare;
	private String userName;
	private String server;
	private String resource;

	public XmppJid(String jid) {
		generateJidPartValue(jid);
	}

	private void generateJidPartValue(String fullJid) {
		full = fullJid;
		bare = fullJid;
		userName = fullJid;

		if (full == null) {
			return;
		}

		int length = full.length();
		if (length == 0) {
			return;
		}

		int atIndex = full.indexOf(SPLIT_STR_AFTER_USERNAME);
		if (atIndex == -1) {
			return;
		}

		int slashIndex = full.lastIndexOf(SPLIT_STR_BEFORE_RESOURCE);
		if (slashIndex == -1) {
			doIfBareJid(atIndex, length);
		} else {
			doIfFullJid(atIndex, slashIndex, length);
		}

	}

	private void doIfFullJid(int atIndex, int slashIndex, int length) {
		if (slashIndex > 0) {
			bare = full.substring(0, slashIndex);
		}

		if (atIndex > 0) {
			userName = full.substring(0, atIndex);
		}

		int start = slashIndex + 1;
		if ((start > 0) && (length > start)) {
			resource = full.substring(start, length);
		}

		start = atIndex + 1;
		if ((start > 0) && (slashIndex > start)) {
			server = full.substring(start, slashIndex);
		}
	}

	private void doIfBareJid(int atIndex, int length) {
		bare = full;

		if (atIndex > 0) {
			userName = full.substring(0, atIndex);
		}

		resource = null;

		int start = atIndex + 1;
		server = full.substring(start);
	}

	public String getFull() {
		return full;
	}

	public String getBare() {
		return bare;
	}

	public String getUserName() {
		return userName;
	}

	public String getServer() {
		return server;
	}

	public String getResource() {
		return resource;
	}

	public static String getFullJid(String bareJid, String resource) {
		String fullJid = bareJid + SPLIT_STR_BEFORE_RESOURCE + resource;
		return fullJid;
	}

}
