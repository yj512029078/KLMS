package com.neekle.kunlunandroid.xmpp;

import org.xmpp.myWRAP.JID;
import org.xmpp.myWRAP.StringList;
import org.xmpp.myWRAP.XmppStack;

class MyXmppStack {
	private volatile static MyXmppStack mSingleton;
	private XmppStack mXmppStack;

	public static MyXmppStack getSingleton() {
		if (mSingleton == null) {
			synchronized (MyXmppStack.class) {
				if (mSingleton == null) {
					mSingleton = new MyXmppStack();
				}
			}
		}

		return mSingleton;
	}

	public void login(String jidString, String pwd, String hostIp, int port) {
		JID jid = new JID(jidString);
		mXmppStack = new XmppStack(jid, pwd, hostIp, port);

		MyXmppCallback mxc = MyXmppCallback.getSingleton();
		mxc.setXmppStack(mXmppStack);
		mXmppStack.registerXmppCallback(mxc);
		mXmppStack.login();
	}

	public void logout() {
		if (mXmppStack != null) {
			mXmppStack.logout();
			mXmppStack = null;
		}
	}

	// public void sendChatMessage(JID to_, String msg_, String xhtml_,
	// String subject_, String thread_) {
	// mXmppStack.sendChatMessage(to_, msg_, xhtml_, subject_, thread_);
	// }

	// public void sendChatMessage(JID to_, String msg_, String xhtml_,
	// String subject_, String thread_) {
	// mXmppStack.sendChatMessage(id_, to_, msg_, xhtml_, subject_, thread_);
	// }
}
