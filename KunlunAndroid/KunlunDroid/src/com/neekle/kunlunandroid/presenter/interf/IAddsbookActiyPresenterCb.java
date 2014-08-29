package com.neekle.kunlunandroid.presenter.interf;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xmpp.myWRAP.JID;
import org.xmpp.myWRAP.Roster;
import org.xmpp.myWRAP.RosterItem;
import org.xmpp.myWRAP.Presence.PresenceType;

import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

public interface IAddsbookActiyPresenterCb {

	public void onRecvNickname(XmppFriend xmpFriend);

	public void onRosterItemAdded(XmppFriend xmpFriend);

	public void onRosterItemRemoved(XmppFriend xmpFriend);

	public void onRosterItemUpdated(XmppFriend xmpFriend);

	public void onRecvRoster(Hashtable<String, XmppFriend> hashtable);

	public void onRecvRosterPresence(XmppFriend xmpFriend);

	public void onLoadUserinfoCompleted(XmppFriend xmppFriend);
}
