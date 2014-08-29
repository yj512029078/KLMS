package com.neekle.kunlunandroid.xmpp.data;

import org.xmpp.myWRAP.JID;
import org.xmpp.myWRAP.Message;

import com.neekle.kunlunandroid.xmpp.common.XmppConstants;
import com.neekle.kunlunandroid.xmpp.common.XmppConstants.MessageType;

public class XmppMessage {

	private String lang;
	private String body;
	private String subject;
	private String thread;
	private String id;
	private XmppJid from;
	private XmppConstants.MessageType subtype;

	public XmppMessage() {

	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public XmppJid getFrom() {
		return from;
	}

	public void setFrom(XmppJid from) {
		this.from = from;
	}

	public MessageType getSubtype() {
		return subtype;
	}

	public void setSubtype(MessageType subtype) {
		this.subtype = subtype;
	}

}
