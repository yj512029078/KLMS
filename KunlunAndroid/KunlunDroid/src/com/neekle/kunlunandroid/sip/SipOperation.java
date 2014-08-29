package com.neekle.kunlunandroid.sip;

import java.util.ArrayList;

import org.xmpp.myWRAP.Presence.PresenceType;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.SinglePhoneData;
import com.neekle.kunlunandroid.db.DbConstants;
import com.neekle.kunlunandroid.db.TbRecordSinglePhoneController;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.util.NetworkState;
import com.neekle.kunlunandroid.util.TimeOperater;
import com.neekle.kunlunandroid.view.controls.cellPhoneContact.CellPhoneContactView;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppFriendPresenExtra;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class SipOperation {

	public static int getPhoneState(String sendOrRcvTag, boolean isConnect) {
		int phoneState = CellPhoneContactView.PhoneState.OUTGO;

		if (sendOrRcvTag.equals(Constants.MsgSendOrRcvTag.SEND)) {
			phoneState = CellPhoneContactView.PhoneState.OUTGO;
		} else if (sendOrRcvTag.equals(Constants.MsgSendOrRcvTag.SENDING)) {
			phoneState = CellPhoneContactView.PhoneState.OUTGO;
		} else {
			if (isConnect) {
				phoneState = CellPhoneContactView.PhoneState.INCOME;
			} else {
				phoneState = CellPhoneContactView.PhoneState.UNANSWERED;
			}
		}

		return phoneState;
	}

	public static int getMappedStatus(final int swigValue) {
		int result = CellPhoneContactView.UserState.KUNLUN_OFFLINE;

		int available = PresenceType.Available.swigValue();
		int away = PresenceType.Away.swigValue();
		int dnd = PresenceType.DND.swigValue();
		int unavailable = PresenceType.Unavailable.swigValue();

		if (swigValue == available) {
			result = CellPhoneContactView.UserState.KUNLUN_IDLE;
		} else if (swigValue == away) {
			result = CellPhoneContactView.UserState.KUNLUN_LEAVE;
		} else if (swigValue == dnd) {
			result = CellPhoneContactView.UserState.KUNLUN_BUSY;
		} else if (swigValue == unavailable) {
			result = CellPhoneContactView.UserState.KUNLUN_OFFLINE;
		}

		return result;
	}

	public long writeOutgoPhone(String partnerUsername, boolean isAV) {
		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriendByUsername(partnerUsername);

		String fromFullJid = XmppOperation.getMyFullJid();
		String toBareJid = xmppFriend.getFriendJid();
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.SEND;

		SinglePhoneData data = constructSinglePhoneData(xmppFriend,
				fromFullJid, toBareJid, sendOrRcvTag, isAV);
		long rowId = doWriteToSinglePhone(data);
		return rowId;
	}

	public long writeIncomePhone(String partnerUsername, boolean isAV) {
		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriendByUsername(partnerUsername);

		String friendJid = xmppFriend.getFriendJid();
		XmppFriendPresenExtra presenExtra = xmppFriend.getFriendPresenExtra();
		String resource = presenExtra.getPresenceResource();
		String fromFullJid = XmppJid.getFullJid(friendJid, resource);

		String toBareJid = XmppOperation.getMyBareJid();
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;

		SinglePhoneData data = constructSinglePhoneData(xmppFriend,
				fromFullJid, toBareJid, sendOrRcvTag, isAV);
		long rowId = doWriteToSinglePhone(data);
		return rowId;
	}

	private SinglePhoneData constructSinglePhoneData(XmppFriend xmppFriend,
			String fromFullJid, String toBareJid, String sendOrRcvTag,
			boolean isAV) {
		SinglePhoneData data = new SinglePhoneData();

		String myBareJid = XmppOperation.getMyBareJid();
		data.setMyJid(myBareJid);

		data.setFromFullJid(fromFullJid);
		data.setToBareJid(toBareJid);
		data.setSendOrRcvTag(sendOrRcvTag);

		boolean isConnect = false;
		data.setConnect(isConnect);

		String phoneNumber = null;
		if (xmppFriend != null) {
			phoneNumber = xmppFriend.getMobilePhone();
		}
		data.setPhoneNumber(phoneNumber);

		String dbTime = TimeOperater.getCurrentTime();
		data.setDbTime(dbTime);

		String timeStamp = dbTime;
		data.setTimeStamp(timeStamp);

		data.setAV(isAV);

		XmppJid xmppJid = new XmppJid(fromFullJid);
		String fromBareJid = xmppJid.getBare();
		String partnerBareJid = XmppOperation.getPartnerBareJid(fromBareJid,
				toBareJid, sendOrRcvTag);
		data.setPartnerJid(partnerBareJid);

		boolean isStar = false;
		if (xmppFriend != null) {
			isStar = xmppFriend.isStarSign();
		}
		data.setStar(isStar);

		Context context = KunlunApplication.getContext();
		boolean isSelfWifi = NetworkState.isWifiActive(context);
		data.setSelfWifi(isSelfWifi);

		long dbId = DbConstants.DB_DEF_ROW_ID;
		data.setDbId(dbId);

		return data;
	}

	private long doWriteToSinglePhone(SinglePhoneData data) {
		Context context = KunlunApplication.getContext();
		TbRecordSinglePhoneController controller = new TbRecordSinglePhoneController(
				context);

		long rowId = DbConstants.DB_DEF_ROW_ID;
		try {
			controller.open();
			rowId = controller.insert(data);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return rowId;
	}

	public ArrayList<SinglePhoneData> getAllSinglePhone() {
		ArrayList<SinglePhoneData> list = new ArrayList<SinglePhoneData>();

		String myJid = XmppOperation.getMyBareJid();
		list = doReadAllFromSinglePhone(myJid);

		return list;
	}

	public ArrayList<SinglePhoneData> getUnanswerSinglePhone() {
		ArrayList<SinglePhoneData> list = new ArrayList<SinglePhoneData>();

		String myJid = XmppOperation.getMyBareJid();
		list = doReadUnanswerFromSinglePhone(myJid);

		return list;
	}

	private ArrayList<SinglePhoneData> doReadAllFromSinglePhone(String myJid) {
		ArrayList<SinglePhoneData> list = new ArrayList<SinglePhoneData>();

		Context context = KunlunApplication.getContext();
		TbRecordSinglePhoneController controller = new TbRecordSinglePhoneController(
				context);

		try {
			controller.open();
			list = controller.getAllPhoneListDesc(myJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return list;
	}

	public void doDeleteFromSinglePhone(long dbId) {
		Context context = KunlunApplication.getContext();
		TbRecordSinglePhoneController controller = new TbRecordSinglePhoneController(
				context);

		int result = DbConstants.DB_DEF_ROW_ID;
		try {
			controller.open();
			result = controller.delete(dbId);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	private ArrayList<SinglePhoneData> doReadUnanswerFromSinglePhone(
			String myJid) {
		ArrayList<SinglePhoneData> list = new ArrayList<SinglePhoneData>();

		Context context = KunlunApplication.getContext();
		TbRecordSinglePhoneController controller = new TbRecordSinglePhoneController(
				context);

		try {
			controller.open();
			list = controller.getUnanswerPhoneListDesc(myJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return list;
	}

	public void updateConnect(long dbId, Boolean isConnect) {
		doUpdateConnectToSinglePhone(dbId, isConnect);
	}

	private int doUpdateConnectToSinglePhone(long dbId, Boolean isConnect) {
		Context context = KunlunApplication.getContext();
		TbRecordSinglePhoneController controller = new TbRecordSinglePhoneController(
				context);

		int result = DbConstants.DB_DEF_ROW_ID;
		try {
			controller.open();
			result = controller.updateIsConnect(dbId, isConnect);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return result;
	}
}
