package com.neekle.kunlunandroid.xmpp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.util.Log;

import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppFriendPresenExtra;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class XmppData {

	private volatile static XmppData mSingleton;
	// 不需要同步，因为我们一般是设置一次，然后后面不会再修改
	private String mJidString;
	private String mPasswdString;
	// 不需要同步，因为我们一般是设置一次，然后后面不会再修改
	private XmppJid mJid;
	private Hashtable<String, XmppFriend> mAllFriendHashtable = new Hashtable<String, XmppFriend>();
	private Hashtable<String, XmppFriend> mHuddleHashtable = new Hashtable<String, XmppFriend>();

	private XmppData() {

	}

	public static XmppData getSingleton() {
		if (mSingleton == null) {
			synchronized (XmppData.class) {
				if (mSingleton == null) {
					mSingleton = new XmppData();
				}
			}
		}

		return mSingleton;
	}

	public synchronized void setAccountString(String jidString,
			String passwdString) {
		boolean isChange = judgeIfLoginJidChange(mJidString, jidString);
		// isChange = true;
		if (isChange) {
			int size = mAllFriendHashtable.size();
			Log.i("sizeinfo", "jidString: " + jidString + "  mJidString: "
					+ mJidString);
			Log.i("sizeinfo", "pre clear:" + size + "");
			clearAllFriend();
			Log.i("sizeinfo", "after clear:" + size + "");
		}

		this.mJidString = jidString;
		this.mPasswdString = passwdString;
		this.mJid = new XmppJid(mJidString);
	}

	private boolean judgeIfLoginJidChange(String preJidString,
			String nowJidString) {
		boolean isChange = false;

		String preBareJidString = null;
		if (preJidString != null) {
			XmppJid preBareJid = new XmppJid(preJidString);
			preBareJidString = preBareJid.getBare();
		}

		String nowBareJidString = null;
		if (nowJidString != null) {
			XmppJid nowBareJid = new XmppJid(nowJidString);
			nowBareJidString = nowBareJid.getBare();
		}

		if (preBareJidString == null) {
			isChange = true;
		} else if (nowBareJidString == null) {
			isChange = true;
		} else if (!preBareJidString.equals(nowBareJidString)) {
			isChange = true;
		}

		return isChange;
	}

	public synchronized XmppJid getJid() {
		return mJid;
	}

	public synchronized String getJidString() {
		return mJidString;
	}

	public synchronized String getPasswdString() {
		return mPasswdString;
	}

	public void addFriend(Map<String, XmppFriend> map) {
		synchronized (mAllFriendHashtable) {
			mAllFriendHashtable.putAll(map);

			Set<Entry<String, XmppFriend>> set = map.entrySet();
			Iterator<Entry<String, XmppFriend>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Entry<String, XmppFriend> entry = iterator.next();
				String jid = entry.getKey();
				boolean isHuddle = XmppOperation.isHuddle(jid);

				if (isHuddle) {
					XmppFriend value = entry.getValue();
					mHuddleHashtable.put(jid, value);
				}
			}

		}
	}

	public void addFriend(ArrayList<XmppFriend> list) {
		synchronized (mAllFriendHashtable) {
			if (list == null) {
				return;
			}

			int size = list.size();
			for (int i = 0; i < size; i++) {
				XmppFriend friend = list.get(i);
				String friendJid = friend.getFriendJid();
				// 此处huddle列表数据也会更新，因为共享数据
				mAllFriendHashtable.put(friendJid, friend);
			}

		}
	}

	public void addFriend(XmppFriend friend) {
		synchronized (mAllFriendHashtable) {
			String friendJid = friend.getFriendJid();
			// 此处huddle列表数据也会更新，因为共享数据
			mAllFriendHashtable.put(friendJid, friend);
		}
	}

	public XmppFriend getFriend(String jid) {
		synchronized (mAllFriendHashtable) {
			XmppFriend xmpFriend = mAllFriendHashtable.get(jid);
			return xmpFriend;
		}
	}

	public XmppFriend getFriendByUsername(String compareUsername) {
		synchronized (mAllFriendHashtable) {
			String jid = null;
			Set<Entry<String, XmppFriend>> set = mAllFriendHashtable.entrySet();
			Iterator<Entry<String, XmppFriend>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Entry<String, XmppFriend> entry = iterator.next();
				jid = entry.getKey();
				XmppJid xmppJid = new XmppJid(jid);
				String username = xmppJid.getUserName();
				if (username.equals(compareUsername)) {
					break;
				}
			}

			XmppFriend xmpFriend = mAllFriendHashtable.get(jid);
			return xmpFriend;
		}
	}

	public Hashtable<String, XmppFriend> getAllFriends() {
		synchronized (mAllFriendHashtable) {
			return mAllFriendHashtable;
		}
	}

	public void removeFriend(String jid) {
		synchronized (mAllFriendHashtable) {
			mAllFriendHashtable.remove(jid);
		}
	}

	public void removeFriend(XmppFriend xmpFriend) {
		String jid = xmpFriend.getFriendJid();

		synchronized (mAllFriendHashtable) {
			mAllFriendHashtable.remove(jid);
		}
	}

	public void clearAllFriend() {
		synchronized (mAllFriendHashtable) {
			mAllFriendHashtable.clear();
			mHuddleHashtable.clear();
		}
	}

	public Hashtable<String, XmppFriend> getCopiedXmpFriends() {
		synchronized (mAllFriendHashtable) {
			Hashtable<String, XmppFriend> copiedHashtable = new Hashtable<String, XmppFriend>();

			Set<Entry<String, XmppFriend>> set = mAllFriendHashtable.entrySet();
			Iterator<Entry<String, XmppFriend>> iterator = set.iterator();

			while (iterator.hasNext()) {
				Entry<String, XmppFriend> entry = iterator.next();
				XmppFriend data = entry.getValue();
				String friendJid = data.getFriendJid();
				XmppFriend copiedXmpFriend = (XmppFriend) data.deepCopy();
				copiedHashtable.put(friendJid, copiedXmpFriend);
			}

			return copiedHashtable;
		}
	}

	public Hashtable<String, XmppFriend> getHuddles() {
		synchronized (mHuddleHashtable) {
			return mHuddleHashtable;
		}
	}

	public void clearHuddle() {
		synchronized (mHuddleHashtable) {
			mHuddleHashtable.clear();
		}
	}

	public void removeHuddle(String jid) {
		synchronized (mHuddleHashtable) {
			mHuddleHashtable.remove(jid);
		}
	}

}
