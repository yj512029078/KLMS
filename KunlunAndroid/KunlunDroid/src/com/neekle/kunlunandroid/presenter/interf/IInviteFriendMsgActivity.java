package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.xmpp.data.XmppSingleChatMessage;

public interface IInviteFriendMsgActivity {
	public void test();

	public void getMessage(XmppSingleChatMessage resultMsg,
			XmppSingleChatMessage extraMsg, int style);
}
