package com.neekle.kunlunandroid.presenter.impl;

import java.util.ArrayList;

import org.xmpp.myWRAP.Presence.PresenceType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.neekle.kunlunandroid.adapter.CellPhoneContactAdapter;
import com.neekle.kunlunandroid.adapter.CellPhoneContactAdapter.OnAdapterMenuItemListener;
import com.neekle.kunlunandroid.adapter.CellPhoneContactAdapter.OnAdapterOperationViewClickListener;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.CellPhoneContactData;
import com.neekle.kunlunandroid.data.SinglePhoneData;
import com.neekle.kunlunandroid.presenter.common.FriendOperation;
import com.neekle.kunlunandroid.presenter.interf.ITelephoneAllActivity;
import com.neekle.kunlunandroid.presenter.interf.ITelephoneDialActivity;
import com.neekle.kunlunandroid.screens.ContactDetailActivity;
import com.neekle.kunlunandroid.screens.MessageChatActivity;
import com.neekle.kunlunandroid.screens.TelVideoActivity;
import com.neekle.kunlunandroid.screens.TelVoiceActivity;
import com.neekle.kunlunandroid.sip.SipOperation;
import com.neekle.kunlunandroid.sip.common.SipConstants;
import com.neekle.kunlunandroid.sip.common.SipConstants.TelAction;
import com.neekle.kunlunandroid.view.controls.cellOneContact.CellOneContactView;
import com.neekle.kunlunandroid.view.controls.cellPhoneContact.CellPhoneContactView;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppFriendPresenExtra;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class TelephoneAllActivityPresenter {

	private static final String INTENT_CONTACT_DETAIL_TELEPHONE_KEY = "contact_detail_telephone_key";

	private Context mContext;
	private ITelephoneAllActivity mIView;

	private ArrayList<CellPhoneContactData> mArrayList = new ArrayList<CellPhoneContactData>();
	private CellPhoneContactAdapter mAdapter;

	public TelephoneAllActivityPresenter(ITelephoneAllActivity view,
			Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {
		readDataFromDb();
		updateAdapterAndView();
	}

	private void updateAdapterAndView() {
		if (mAdapter == null) {
			mAdapter = new CellPhoneContactAdapter(mContext, mArrayList);
			mAdapter.setOnAdapterMenuItemClickListener(mOnAdapterMenuItemListener);
			mAdapter.setOnAdapterOperationViewClickListener(mOnAdapterOperationViewClickListener);

			mIView.updateAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private OnAdapterMenuItemListener mOnAdapterMenuItemListener = new OnAdapterMenuItemListener() {

		@Override
		public void onItemClick(int itemId, CellPhoneContactData data) {
			switch (itemId) {
			case CellPhoneContactView.MenuItemClickState.MAKE_PHONE_CALL_CLICK: {
				doOnMakePhoneCallItemClick(data);
				break;
			}
			case CellPhoneContactView.MenuItemClickState.MAKE_VIDEO_CALL_CLICK: {
				doOnMakeVideoCallItemClick(data);
				break;
			}
			case CellPhoneContactView.MenuItemClickState.SEND_MSG_CLICK: {
				doOnSendMsgItemClick(data);
				break;
			}
			case CellPhoneContactView.MenuItemClickState.CONTACT_INFO_CLICK: {
				doOnContactInfoItemClick(data);
				break;
			}
			case CellPhoneContactView.MenuItemClickState.MARK_UNMARK_CLICK: {
				doOnMarkUnmarkItemClick(data);
				break;
			}
			case CellPhoneContactView.MenuItemClickState.DELETE_CLICK: {
				doOnDeleteItemClick(data);
				break;
			}
			default:
				break;
			}
		}
	};

	private OnAdapterOperationViewClickListener mOnAdapterOperationViewClickListener = new OnAdapterOperationViewClickListener() {

		@Override
		public void onWholeViewClick(CellPhoneContactData data) {

		}

		@Override
		public void onPhoneViewClick(CellPhoneContactData data) {
			doOnMakePhoneCallItemClick(data);
		}
	};

	private void doOnMakePhoneCallItemClick(CellPhoneContactData data) {
		String partnerJid = data.getPartnerJid();
		XmppJid xmppJid = new XmppJid(partnerJid);
		String partnerSipUserName = xmppJid.getUserName();

		Class<? extends Activity> targetActivity = TelVoiceActivity.class;
		doSwitchTelVoiceActivity(mContext, targetActivity, partnerSipUserName);
	}

	private void doOnMakeVideoCallItemClick(CellPhoneContactData data) {
		String partnerJid = data.getPartnerJid();
		XmppJid xmppJid = new XmppJid(partnerJid);
		String partnerSipUserName = xmppJid.getUserName();

		Class<? extends Activity> targetActivity = TelVideoActivity.class;
		doSwitchTelVoiceActivity(mContext, targetActivity, partnerSipUserName);
	}

	private void doOnSendMsgItemClick(CellPhoneContactData data) {
		String partnerJid = data.getPartnerJid();
		XmppJid xmppJid = new XmppJid(partnerJid);
		String bareJid = xmppJid.getBare();

		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmpFriend = xmppData.getFriend(bareJid);

		doSwitchMessageChatActivity(mContext, MessageChatActivity.class,
				xmpFriend);
	}

	private void doOnContactInfoItemClick(CellPhoneContactData data) {
		String partnerJid = data.getPartnerJid();
		XmppJid xmppJid = new XmppJid(partnerJid);
		String partnerBareJid = xmppJid.getBare();
		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriend(partnerBareJid);

		// 要注意的是，联系人详情更新数据后，这边不会立刻反应，这个后面是需要修改的。因为我们采用的方案是不finish的跳转
		Class<? extends Activity> targetActivity = ContactDetailActivity.class;
		doSwitchContactDetailActivity(mContext, targetActivity, xmppFriend);
	}

	private void doOnMarkUnmarkItemClick(CellPhoneContactData data) {
		// 注意：这里其实是有BUG的，这里的data的star状态应该要和联系人保持同步，后面改掉
		String partnerJid = data.getPartnerJid();
		XmppJid xmppJid = new XmppJid(partnerJid);
		String partnerBareJid = xmppJid.getBare();

		boolean isStar = data.isStar();
		isStar = !isStar;

		data.setStar(isStar);
		updateAdapterAndView();

		XmppOperation xmppOperation = new XmppOperation();
		xmppOperation.updateXmpFriendAndWriteToDb(partnerBareJid, isStar);
	}

	private void doOnDeleteItemClick(CellPhoneContactData data) {
		long dbId = data.getDbId();

		mArrayList.remove(data);
		updateAdapterAndView();

		SipOperation sipOperation = new SipOperation();
		sipOperation.doDeleteFromSinglePhone(dbId);
	}

	private void readDataFromDb() {
		ArrayList<CellPhoneContactData> list = getCellPhoneContactDataList();
		mArrayList = list;
	}

	private ArrayList<CellPhoneContactData> getCellPhoneContactDataList() {
		ArrayList<CellPhoneContactData> list = new ArrayList<CellPhoneContactData>();

		SipOperation sipOperation = new SipOperation();
		ArrayList<SinglePhoneData> dataList = sipOperation.getAllSinglePhone();
		for (SinglePhoneData singlePhoneData : dataList) {
			CellPhoneContactData data = getCellPhoneContactData(singlePhoneData);
			list.add(data);
		}

		return list;
	}

	private CellPhoneContactData getCellPhoneContactData(
			SinglePhoneData singlePhoneData) {
		CellPhoneContactData data = new CellPhoneContactData();

		String partnerJid = singlePhoneData.getPartnerJid();
		XmppJid xmppJid = new XmppJid(partnerJid);
		String partnerBareJid = xmppJid.getBare();
		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriend(partnerBareJid);

		XmppFriendPresenExtra presenExtra = xmppFriend.getFriendPresenExtra();
		int presenceState = presenExtra.getPresenceState();
		int userState = SipOperation.getMappedStatus(presenceState);
		data.setUserState(userState);

		String showName = xmppFriend.getShowName();
		data.setName(showName);

		boolean isStar = singlePhoneData.isStar();
		data.setStar(isStar);

		boolean isPhoneWifi = singlePhoneData.isSelfWifi();
		data.setPhoneWifi(isPhoneWifi);

		String sendOrRcvTag = singlePhoneData.getSendOrRcvTag();
		boolean isConnect = singlePhoneData.isConnect();
		int phoneState = SipOperation.getPhoneState(sendOrRcvTag, isConnect);
		data.setPhoneState(phoneState);

		String phoneNumber = xmppFriend.getMobilePhone();
		data.setPhoneNumber(phoneNumber);

		String dbTime = singlePhoneData.getDbTime();
		data.setTime(dbTime);

		String myJid = singlePhoneData.getMyJid();
		data.setMyJid(myJid);

		String fromFullJid = singlePhoneData.getFromFullJid();
		data.setFromFullJid(fromFullJid);

		String toBareJid = singlePhoneData.getToBareJid();
		data.setToBareJid(toBareJid);

		data.setPartnerJid(partnerJid);

		long dbId = singlePhoneData.getDbId();
		data.setDbId(dbId);

		return data;
	}

	public void doSwitchTelVoiceActivity(Context context,
			Class<? extends Activity> targetActivity, String partnerSipUserName) {
		Bundle bundle = new Bundle();
		bundle.putString(SipConstants.INTENT_SIP_USERNAME_PARTNER_KEY,
				partnerSipUserName);
		String telAction = TelAction.OUTGOING;
		bundle.putString(SipConstants.INTENT_TEL_ACTION_KEY, telAction);

		switchActivityWithNoFinish(context, targetActivity, bundle);
	}

	public void doSwitchTelVideoActivity(Context context,
			Class<? extends Activity> targetActivity, String partnerSipUserName) {
		Bundle bundle = new Bundle();
		bundle.putString(SipConstants.INTENT_SIP_USERNAME_PARTNER_KEY,
				partnerSipUserName);
		String telAction = TelAction.OUTGOING;
		bundle.putString(SipConstants.INTENT_TEL_ACTION_KEY, telAction);

		switchActivityWithNoFinish(context, targetActivity, bundle);
	}

	private void doSwitchMessageChatActivity(Context context,
			Class<? extends Activity> targetActivity, XmppFriend xmpFriend) {
		// Bundle bundle = new Bundle();
		// bundle.putParcelable(INTENT_Telephone_MESSAGE_CHAT_KEY, xmpFriend);
		//
		// switchActivity(context, targetActivity, bundle);
	}

	public void doSwitchContactDetailActivity(Context context,
			Class<? extends Activity> targetActivity, XmppFriend xmppFriend) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_CONTACT_DETAIL_TELEPHONE_KEY, xmppFriend);

		switchActivityWithNoFinish(context, targetActivity, bundle);
	}

	public void doSwitchMainActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	private void switchActivityWithNoFinish(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		switchActivityWithNoFinish(context, targetActivity, bundle);

		Activity activity = (Activity) context;
		activity.finish();
	}

}
