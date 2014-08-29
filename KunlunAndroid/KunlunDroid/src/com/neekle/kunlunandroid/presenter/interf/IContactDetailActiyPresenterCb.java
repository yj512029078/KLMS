package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

public interface IContactDetailActiyPresenterCb {
	public void testPresenter();

	public void onLoadUserinfoCompleted(XmppFriend xmppFriend);
}
