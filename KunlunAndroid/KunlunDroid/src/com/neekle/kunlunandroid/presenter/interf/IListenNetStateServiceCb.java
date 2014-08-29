package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.xmpp.data.XmppConnectionError;

public interface IListenNetStateServiceCb {
	public void onInvaildJID();

	public void onInvaildPassword();

	public void onTcpConnFailed(XmppConnectionError errorInfo);

	public void onAuthFailed();

	public void onLoginSuccess();
}
