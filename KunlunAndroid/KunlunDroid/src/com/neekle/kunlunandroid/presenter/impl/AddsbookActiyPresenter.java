package com.neekle.kunlunandroid.presenter.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.widget.Filter;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.CellOneContactAdapter;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.AddressBookData;
import com.neekle.kunlunandroid.data.CellOneContactData;
import com.neekle.kunlunandroid.db.TbAddressbookController;
import com.neekle.kunlunandroid.presenter.common.FriendOperation;
import com.neekle.kunlunandroid.presenter.common.WebservicePresenter;
import com.neekle.kunlunandroid.presenter.interf.IAddsbookActivity;
import com.neekle.kunlunandroid.presenter.interf.IAddsbookActiyPresenterCb;
import com.neekle.kunlunandroid.screens.AddsbookOrgActivity;
import com.neekle.kunlunandroid.screens.ContactDetailActivity;
import com.neekle.kunlunandroid.screens.HuddleChatActvity;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.screens.MessageChatActivity;
import com.neekle.kunlunandroid.screens.TelVideoActivity;
import com.neekle.kunlunandroid.screens.TelVoiceActivity;
import com.neekle.kunlunandroid.sip.common.SipConstants;
import com.neekle.kunlunandroid.sip.common.SipConstants.TelAction;
import com.neekle.kunlunandroid.util.BitmapOperator;
import com.neekle.kunlunandroid.util.DisplayUtil;
import com.neekle.kunlunandroid.util.pinyin.PinYinUtil;
import com.neekle.kunlunandroid.view.controls.cellOneContact.CellOneContactView;
import com.neekle.kunlunandroid.view.controls.cellOneContact.CellOneContactView.CONTACT_CONTROL_DEFAULT_MENUITEM;
import com.neekle.kunlunandroid.view.controls.cellOneContact.CellOneContactView.CONTACT_CONTROL_ORGANIZATION_MENUITEM;
import com.neekle.kunlunandroid.view.specials.AlphabetListView;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeAddsbookView;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.MyXmppCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.XmppService;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppFriendPresenExtra;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class AddsbookActiyPresenter implements IAddsbookActiyPresenterCb,
		ISoapReceivedCallback {

	private static final int IMAGE_WIDTH_DIP = 40;
	private static final int IMAGE_HEIGHT_DIP = 40;

	private static final int MSG_FRIEND_METHOD_GET_LIST_COMPLETED = 30;
	private static final int MSG_ROSTER_DATA_COMPLETED = 31;
	private static final int MSG_ROSTER_PRESENCE_DATA_COMPLETED = 32;
	private static final int MSG_DB_COMPLETED = 34;
	private static final int MSG_ROSTER_ITEM_ADDED = 35;
	private static final int MSG_ROSTER_ITEM_REMOVED = 36;
	private static final int MSG_ROSTER_ITEM_UPDATED = 37;
	private static final int MSG_REV_NICK_NAME = 38;
	private static final int MSG_WS_lOGIN_COMPLETED = 39;
	private static final int MSG_WS_GET_ADDSBOOK_LISTVIEW_COMPLETED = 40;

	private static final String INTENT_ROOM_ID = "roomId";
	private static final String MESSAGE_TO_PASS = "message_to_pass";
	private static final String INTENT_ADDSBOOK_AND_ORG_ADDSBOOK_ID = "addsbook_id";
	private static final String INTENT_ADDSBOOK_AND_ORG_ADDSBOOK_CONTENT = "addsbook_content";
	private static final String INTENT_ADDSBOOK_MESSAGE_CHAT_KEY = "addsbook_message_chat_key";
	private static final String INTENT_ADDSBOOK_CONTACT_INFO_KEY = "addsbook_contact_info_key";

	private Hashtable<String, AddressBookData> mAddressbookDataMap = new Hashtable<String, AddressBookData>();
	private Hashtable<String, Boolean> mXmppHuddleShowStateMap = new Hashtable<String, Boolean>();
	private Hashtable<String, XmppFriend> mXmppAddsContactMap = new Hashtable<String, XmppFriend>();
	private List<CellOneContactData> mAddsbookAllDataList = new ArrayList<CellOneContactData>();
	private List<CellOneContactData> mAddsbookHuddleDataList = new ArrayList<CellOneContactData>();
	private CellOneContactAdapter mAddsbookAdapter;

	private Bitmap mDefOrganizationAvatarBp;
	private Bitmap mDefPhotoBp;
	private Context mContext;
	private MyXmppCallback mMyXmppCallback;
	private XmppService mXmppService;
	private XmppOperation mXmppOperation;
	private IAddsbookActivity mIView;
	private WebservicePresenter mWebsPresenter;

	private boolean mIsShowAllFriends;

	public AddsbookActiyPresenter(IAddsbookActivity view, Context context) {
		mIView = view;
		mContext = context;

		mXmppService = XmppService.getSingleton();
		mMyXmppCallback = MyXmppCallback.getSingleton();
		mMyXmppCallback.setAddsbookActiyCallback(this);
		mXmppOperation = new XmppOperation();

		mWebsPresenter = new WebservicePresenter(context);
		mWebsPresenter.setCallback(this);
	}

	public void init(String sessionid) {
		String myBareJid = XmppOperation.getMyBareJid();
		ArrayList<AddressBookData> list = readFromDb(myBareJid);
		doIfFriendAvailble(list);

		doWsRequestLogin();

		fillAddsbookDataAndUpdateView();
	}

	private void fillAddsbookDataAndUpdateView() {
		XmppData xmppData = XmppData.getSingleton();
		Hashtable<String, XmppFriend> copiedHashtable = xmppData
				.getCopiedXmpFriends();
		doIfAddedFriendAvailble(copiedHashtable);
	}

	private void filterTypedFriends() {
		List<CellOneContactData> huddleShowedList = new ArrayList<CellOneContactData>();
		List<CellOneContactData> huddleNonShowedList = new ArrayList<CellOneContactData>();

		int size = mAddsbookHuddleDataList.size();
		for (int i = 0; i < size; i++) {
			CellOneContactData data = mAddsbookHuddleDataList.get(i);
			String jid = data.getContactJid();

			boolean isShow = mXmppHuddleShowStateMap.get(jid);
			if (isShow) {
				huddleShowedList.add(data);
			} else {
				huddleNonShowedList.add(data);
			}
		}

		if (mIsShowAllFriends) {
			mAddsbookAllDataList.addAll(huddleNonShowedList);
			updateHuddleShowState(mXmppHuddleShowStateMap, huddleNonShowedList,
					true);
		} else {
			mAddsbookAllDataList.removeAll(huddleShowedList);
			updateHuddleShowState(mXmppHuddleShowStateMap, huddleShowedList,
					false);
		}
	}

	private void updateHuddleShowState(
			Hashtable<String, Boolean> huddleStateTable,
			List<CellOneContactData> list, boolean isShow) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			CellOneContactData data = list.get(i);
			String jid = data.getContactJid();

			huddleStateTable.put(jid, isShow);
		}
	}

	public void doSearchFilter(Editable s) {
		Filter filter = mAddsbookAdapter.getFilter();
		filter.filter(s.toString());
	}

	public void setIsShowAllFriends(boolean isShowAll) {
		mIsShowAllFriends = isShowAll;
	}

	// 普通朋友和组过滤
	public void doAddsbookFilter(boolean isViewShowAll) {
		mIsShowAllFriends = isViewShowAll;
		filterAndSortFriendsAndUpdateView();
	}

	public CellOneContactAdapter.OnCellOneContactBtnAdapterListener //
	mOnCellOneContactBtnAdapterListener = new CellOneContactAdapter.OnCellOneContactBtnAdapterListener() {

		@Override
		public void onBtnPhoneClickInterface() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBtnMsgChatClickInterface() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBtnExpandInterface() {
			// TODO Auto-generated method stub

		}
	};

	private CellOneContactAdapter.OnCellOneContactViewAdapterListener mOnCellOneContactViewAdapterListener = new CellOneContactAdapter.OnCellOneContactViewAdapterListener() {

		@Override
		public void onClick(CellOneContactData cellOneContactData) {
			int addressbookStyle = CellOneContactView.CONTACT_CONTROL.CONTACT_CONTROL_ADDRESSBOOK
					.ordinal();

			int controlStyle = cellOneContactData.getmControlStyle();
			if (controlStyle == addressbookStyle) {
				String addsbookId = cellOneContactData.getContactJid();

				String fileContent = Constants.EMPTY_STRING;
				AddressBookData data = mAddressbookDataMap.get(addsbookId);
				if (data != null) {
					fileContent = data.getFileContent();
				}

				Class<? extends Activity> targetActivity = AddsbookOrgActivity.class;
				doSwitchAddsbookOrgActivity(mContext, targetActivity,
						addsbookId, fileContent);
			}
		}
	};

	private CellOneContactAdapter.OnCellOneContactMenuAdapterListener mOnCellOneContactMenuAdapterListener = new CellOneContactAdapter.OnCellOneContactMenuAdapterListener() {

		@Override
		public void onAdapterContactMenuItemsInterface(int style, int item,
				CellOneContactData cellOneContactData) {
			doOnMenuItemClick(style, item, cellOneContactData);
		}
	};

	private void doOnMenuItemClick(int style, int item,
			CellOneContactData cellOneContactData) {
		int friendStyle = CellOneContactView.CONTACT_CONTROL.CONTACT_CONTROL_DEFAULT
				.ordinal();
		int groupStyle = CellOneContactView.CONTACT_CONTROL.CONTACT_CONTROL_ORGANIZATION
				.ordinal();
		int contactInfo = CONTACT_CONTROL_DEFAULT_MENUITEM.ITEM_CONTACT_INFORMATION
				.ordinal();

		if (style == friendStyle) {
			int sendMsg = CONTACT_CONTROL_DEFAULT_MENUITEM.ITEM_CONTACT_SENDMSG
					.ordinal();
			int starLabel = CONTACT_CONTROL_DEFAULT_MENUITEM.ITEM_CONTACT_STARLABEL
					.ordinal();
			int deleteContact = CONTACT_CONTROL_DEFAULT_MENUITEM.ITEM_CONTACT_DELCONTACT
					.ordinal();
			int callContact = CONTACT_CONTROL_DEFAULT_MENUITEM.ITEM_CONTACT_CALL
					.ordinal();
			int videoContact = CONTACT_CONTROL_DEFAULT_MENUITEM.ITEM_CONTACT_VIDEO
					.ordinal();

			if (item == contactInfo) {
				doToContactDetail(cellOneContactData);
			} else if (item == sendMsg) {
				doToSendMsg(cellOneContactData);
			} else if (item == starLabel) {
				doToStarFriend(cellOneContactData);
			} else if (item == deleteContact) {
				doToRmFriend(cellOneContactData);
			} else if (item == callContact) {
				doToCallFriend(cellOneContactData);
			} else if (item == videoContact) {
				doToVideoFriend(cellOneContactData);
			}
		} else if (style == groupStyle) {
			int sendMsg = CONTACT_CONTROL_ORGANIZATION_MENUITEM.ITEM_ORG_SENDMSG
					.ordinal();
			// 看不懂 ？？
			int starLabel = CONTACT_CONTROL_ORGANIZATION_MENUITEM.ITEM_ORG_BOTHSIGN
					.ordinal();
			// 暂时不做
			int deleteContact = CONTACT_CONTROL_ORGANIZATION_MENUITEM.ITEM_ORG_DELETE
					.ordinal();

			if (item == sendMsg) {
				doToSendMsg(cellOneContactData);
			}
		}

	}

	private void doToContactDetail(CellOneContactData cellOneContactData) {
		String jid = cellOneContactData.getContactJid();
		XmppFriend xmpFriend = getContactInfo(mXmppAddsContactMap, jid);

		Class<? extends Activity> targetActivity = ContactDetailActivity.class;
		doSwitchContactDetailActivity(mContext, targetActivity, xmpFriend);
	}

	private void doToSendMsg(CellOneContactData cellOneContactData) {
		String jid = cellOneContactData.getContactJid();
		XmppFriend xmpFriend = getContactInfo(mXmppAddsContactMap, jid);

		Class<? extends Activity> targetActivity = null;
		boolean isHuddle = XmppOperation.isHuddle(jid);
		if (isHuddle) {
			targetActivity = HuddleChatActvity.class;
			doSwitchHuddleChatActivity(mContext, targetActivity, jid);
		} else {
			targetActivity = MessageChatActivity.class;
			doSwitchMessageChatActivity(mContext, targetActivity, xmpFriend);
		}

	}

	private void doToStarFriend(CellOneContactData data) {
		boolean isStar = false;
		int catlog = data.getFriendPinyinCatlog();
		if (catlog == Constants.FriendPinyinCatlog.FRIEND_STAR) {
			catlog = Constants.FriendPinyinCatlog.FRIEND_NORMAL;
		} else {
			catlog = Constants.FriendPinyinCatlog.FRIEND_STAR;
			isStar = true;
		}

		String friendJid = data.getContactJid();
		XmppFriend xmppFriend = mXmppAddsContactMap.get(friendJid);
		xmppFriend.setStarSign(isStar);
		mXmppOperation.updateXmpFriendAndWriteToDb(friendJid, isStar);

		data.setFriendPinyinCatlog(catlog);
		// 重新排序，然后更新View显示
		sortFriendsAndUpdateView();
	}

	private void doToRmFriend(CellOneContactData data) {
		// 关于网络不好的问题，暂时忽略,后面需要考虑 *******
		String jid = data.getContactJid();

		doDeleteFriend(jid);
		mXmppService.deleteFriend(jid);
	}

	private void doToCallFriend(CellOneContactData data) {
		// 关于网络不好的问题，暂时忽略,后面需要考虑 *******
		String jid = data.getContactJid();
		XmppJid xmppJid = new XmppJid(jid);
		String partnerSipUserName = xmppJid.getUserName();

		Class<? extends Activity> targetActivity = TelVoiceActivity.class;
		doSwitchTelVoiceActivity(mContext, targetActivity, partnerSipUserName);
	}

	private void doToVideoFriend(CellOneContactData data) {
		// 关于网络不好的问题，暂时忽略,后面需要考虑 *******
		String jid = data.getContactJid();
		XmppJid xmppJid = new XmppJid(jid);
		String partnerSipUserName = xmppJid.getUserName();

		Class<? extends Activity> targetActivity = TelVideoActivity.class;
		doSwitchTelVideoActivity(mContext, targetActivity, partnerSipUserName);
	}

	private XmppFriend getContactInfo(Hashtable<String, XmppFriend> hashMap,
			String jid) {
		XmppFriend xmpFriend = hashMap.get(jid);
		return xmpFriend;
	}

	private ArrayList<AddressBookData> getMappedAddressBookData(
			Hashtable<String, AddressBookData> addressMap,
			ArrayList<TypeAddsbookView> rawList) {
		ArrayList<AddressBookData> counterList = new ArrayList<AddressBookData>();

		int rawSize = rawList.size();
		for (int i = 0; i < rawSize; i++) {
			TypeAddsbookView typeAddsbookView = rawList.get(i);
			String rawAddsbookId = typeAddsbookView.getAddbookId();

			AddressBookData counterData = addressMap.get(rawAddsbookId);
			if (counterData == null) {
				counterData = new AddressBookData();
			}

			feedData(counterData, typeAddsbookView);
			counterList.add(counterData);
		}

		return counterList;
	}

	private void feedData(AddressBookData feedDestiData,
			TypeAddsbookView feedRawData) {
		String addressbookId = feedRawData.getAddbookId();
		String displayName = feedRawData.getDisplayName();
		String friendJid = feedRawData.getFriendJid();
		int isPublic = feedRawData.getIsPublic();

		feedDestiData.setAddbookId(addressbookId);

		String myBareJid = XmppOperation.getMyBareJid();
		feedDestiData.setMyJid(myBareJid);

		// 由于WS报文对于通讯录属于自己的情况，friendJid 为 null;因此这里做映射转换
		if ((friendJid == null) || (friendJid.equals(Constants.EMPTY_STRING))) {
			friendJid = myBareJid;
		}
		feedDestiData.setOwnerJid(friendJid);

		feedDestiData.setRootName(displayName);

		boolean isShared = true;
		if (isPublic == 0) {
			isShared = false;
		}
		feedDestiData.setShared(isShared);
	}

	private void doIfFriendAdded(Bundle bundle) {
		Serializable serializable = bundle.getSerializable(MESSAGE_TO_PASS);
		Hashtable<String, XmppFriend> hashtable = (Hashtable<String, XmppFriend>) serializable;
		doIfAddedFriendAvailble(hashtable);
	}

	private void doIfFriendAvailble(ArrayList<AddressBookData> list) {
		synchronized (mAddsbookAllDataList) {
			fillData(mAddsbookAllDataList, mAddressbookDataMap, list);
		}

		filterAndSortFriendsAndUpdateView();
		writeToDb(list);
	}

	private void doIfAddedFriendAvailble(Hashtable<String, XmppFriend> hashtable) {
		// sorted, sort mAddsbookAllDataList
		synchronized (mAddsbookAllDataList) {
			fillData(hashtable, mAddsbookAllDataList, mAddsbookHuddleDataList,
					mXmppAddsContactMap);
		}

		filterAndSortFriendsAndUpdateView();
	}

	private void doIfFriendDeleted(Bundle bundle) {
		Serializable serializable = bundle.getSerializable(MESSAGE_TO_PASS);
		XmppFriend data = (XmppFriend) serializable;
		String jid = data.getFriendJid();
		doDeleteFriend(jid);
	}

	private void doDeleteFriend(String jid) {
		mXmppHuddleShowStateMap.remove(jid);
		rmItemFromContactList(mAddsbookAllDataList, mAddsbookHuddleDataList,
				jid);
		updateAddsbookAdapterAndView();
	}

	// 当然，这个移除现在只考虑了XMPP联系人的移除。。。
	private void rmItemFromContactList(List<CellOneContactData> friendList,
			List<CellOneContactData> huddleList, String jid) {
		List<CellOneContactData> list = null;
		boolean isHuddle = XmppOperation.isHuddle(jid);
		if (isHuddle) {
			list = huddleList;
		} else {
			list = friendList;
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			CellOneContactData data = list.get(i);
			String contactJid = data.getContactJid();

			if (contactJid.equals(jid)) {
				// mXmppAddsContactMap 和 mAddsbookHuddleDataList 指向同一对象（如果
				// mXmppAddsContactMap 包含有huddle对象的话）
				list.remove(data);
				break;
			}
		}
	}

	private void doIfUpdatedFriendAvail(Bundle bundle) {
		Parcelable parcelable = bundle.getParcelable(MESSAGE_TO_PASS);
		XmppFriend data = (XmppFriend) parcelable;

		updateContactInfo(mXmppAddsContactMap, data);
		synchronized (mAddsbookAllDataList) {
			updateContactInfo(mAddsbookAllDataList, data);
		}

		sortFriendsAndUpdateView();
	}

	/* Handler 操作部分 begin */

	private void sendMsgToHandler(int what) {
		Message message = Message.obtain();
		message.what = what;
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, String string) {
		Message message = Message.obtain();
		message.what = what;
		if (string != null) {
			Bundle bundle = new Bundle();
			bundle.putString(MESSAGE_TO_PASS, string);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, ArrayList<? extends Parcelable> list) {
		Message message = Message.obtain();
		message.what = what;
		if (list != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(MESSAGE_TO_PASS, list);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, Parcelable parcelable) {
		Message message = Message.obtain();
		message.what = what;
		if (parcelable != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelable(MESSAGE_TO_PASS, parcelable);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, Hashtable<String, ?> list) {
		Message message = Message.obtain();
		message.what = what;
		if (list != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(MESSAGE_TO_PASS, list);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			if (msg.what == MSG_FRIEND_METHOD_GET_LIST_COMPLETED) {
				handleRevFriendGetList(bundle);
			} else if (msg.what == MSG_ROSTER_DATA_COMPLETED) {
				handleRosterData(bundle);
			} else if (msg.what == MSG_ROSTER_PRESENCE_DATA_COMPLETED) {
				handleRevRosterPresenceData(bundle);
			} else if (msg.what == MSG_ROSTER_ITEM_ADDED) {
				handleRevRosterItemAdded(bundle);
			} else if (msg.what == MSG_ROSTER_ITEM_REMOVED) {
				handleRevRosterItemRmed(bundle);
			} else if (msg.what == MSG_ROSTER_ITEM_UPDATED) {
				handleRevRosterItemUpdated(bundle);
			} else if (msg.what == MSG_REV_NICK_NAME) {
				handleRevNickname(bundle);
			} else if (msg.what == MSG_DB_COMPLETED) {
				handleWriteDbCompleted();
			} else if (msg.what == MSG_WS_lOGIN_COMPLETED) {
				handleRevWsLoginCompleted(bundle);
			} else if (msg.what == MSG_WS_GET_ADDSBOOK_LISTVIEW_COMPLETED) {
				handleRevWsGetAddsbookListviewCompleted(bundle);
			}
		}

	};

	private void handleWriteDbCompleted() {

	}

	private void handleRevFriendGetList(Bundle bundle) {

	}

	private void handleRosterData(Bundle bundle) {
		doIfFriendAdded(bundle);
	}

	private void handleRevRosterPresenceData(Bundle bundle) {
		doIfUpdatedFriendAvail(bundle);
	}

	private void handleRevRosterItemAdded(Bundle bundle) {
		doIfFriendAdded(bundle);
	}

	private void handleRevRosterItemRmed(Bundle bundle) {
		doIfFriendDeleted(bundle);
	}

	private void handleRevRosterItemUpdated(Bundle bundle) {
		doIfUpdatedFriendAvail(bundle);
	}

	private void handleRevNickname(Bundle bundle) {
		doIfUpdatedFriendAvail(bundle);
	}

	private void handleRevWsLoginCompleted(Bundle bundle) {
		String sessionid = bundle.getString(MESSAGE_TO_PASS);

		if (sessionid != null) {
			doWsRequestGetAddsbookListview(sessionid);
		}
	}

	private void handleRevWsGetAddsbookListviewCompleted(Bundle bundle) {
		ArrayList<TypeAddsbookView> list = bundle
				.getParcelableArrayList(MESSAGE_TO_PASS);

		if (list != null) {
			ArrayList<AddressBookData> counterList = getMappedAddressBookData(
					mAddressbookDataMap, list);
			doIfFriendAvailble(counterList);
		}
	}

	/* Handler 操作部分 end */

	/* 关于XMPP联系人部分数据更新 begin */

	private void fillData(Hashtable<String, XmppFriend> dataSrc,
			List<CellOneContactData> listAllDesti,
			List<CellOneContactData> listHuddleDesti,
			Hashtable<String, XmppFriend> xmppAddsContactMap) {
		Set<Entry<String, XmppFriend>> set = dataSrc.entrySet();
		Iterator<Entry<String, XmppFriend>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Entry<String, XmppFriend> entry = iterator.next();
			XmppFriend data = entry.getValue();

			String friendJid = data.getFriendJid();
			boolean isItemHaveAdded = xmppAddsContactMap.containsKey(friendJid);
			if (isItemHaveAdded) {
				continue;
			}

			XmppFriendPresenExtra friendPresenExtra = data
					.getFriendPresenExtra();
			int presenceValue = friendPresenExtra.getPresenceState();
			int userState = FriendOperation.getMappedStatus(presenceValue);

			boolean isStar = data.isStarSign();
			int catlog = getCatlog(isStar);
			String showName = data.getShowName();
			String showNamePinyin = data.getShowNamePinyin();
			ArrayList<String> showNamePinyinList = getList(showNamePinyin);

			// 在此处设置默认照片，如果有照片的话，需要设置，考虑下代码是否需要修改 **
			if (mDefPhotoBp == null) {
				mDefPhotoBp = getBitmap(R.drawable.addsbook_def_photo);
			}
			String photoPath = data.getHeadPortrait();
			Bitmap photoBitmap = getProperPhoto(photoPath, mDefPhotoBp);

			CellOneContactData cellOneContactData = null;
			// 群的标示符名称需要进一步确认 **
			if (friendJid.contains(Constants.HUDDLE_IDENTIFIER)) {
				int style = CellOneContactView.CONTACT_CONTROL.CONTACT_CONTROL_ORGANIZATION
						.ordinal();
				cellOneContactData = new CellOneContactData(style);
				cellOneContactData.setUserState(userState);
				cellOneContactData.setContactPhoto(photoBitmap);
				cellOneContactData.setContactName(showName);
				cellOneContactData.setContactJid(friendJid);
				cellOneContactData.setNamePinyinList(showNamePinyinList);
				// 这个catlog只是初步值，至于到底是否属于#类别，取决于下一行代码的判断，所以注意顺序
				cellOneContactData.setFriendPinyinCatlog(catlog);
				setPinyinCatlog(cellOneContactData, showNamePinyin);

				listHuddleDesti.add(cellOneContactData);
				mXmppHuddleShowStateMap.put(friendJid, false);
			} else {
				int style = CellOneContactView.CONTACT_CONTROL.CONTACT_CONTROL_DEFAULT
						.ordinal();
				cellOneContactData = new CellOneContactData(style);
				cellOneContactData.setUserState(userState);
				cellOneContactData.setContactPhoto(photoBitmap);
				cellOneContactData.setContactName(showName);
				cellOneContactData.setContactJid(friendJid);
				cellOneContactData.setNamePinyinList(showNamePinyinList);
				// 这个catlog只是初步值，至于到底是否属于#类别，取决于下一行代码的判断，所以注意顺序
				cellOneContactData.setFriendPinyinCatlog(catlog);
				setPinyinCatlog(cellOneContactData, showNamePinyin);

				listAllDesti.add(cellOneContactData);
			}

			xmppAddsContactMap.put(friendJid, data);
		}
	}

	private Bitmap getProperPhoto(String photoPath, Bitmap defPhoto) {
		Bitmap bitmap = defPhoto;
		if (photoPath != null) {
			Bitmap decodedBitmap = BitmapFactory.decodeFile(photoPath);

			if (decodedBitmap != null) {
				bitmap = decodedBitmap;
			}
		}

		int width = DisplayUtil.dip2px(mContext, IMAGE_WIDTH_DIP);
		int height = DisplayUtil.dip2px(mContext, IMAGE_HEIGHT_DIP);
		if (bitmap != null) {
			bitmap = BitmapOperator.getImageThumbnail(bitmap, width, height);
		}

		return bitmap;
	}

	private int getCatlog(boolean isStar) {
		int catlog;
		if (isStar) {
			catlog = Constants.FriendPinyinCatlog.FRIEND_STAR;
		} else {
			catlog = Constants.FriendPinyinCatlog.FRIEND_NORMAL;
		}

		return catlog;
	}

	private void updateContactInfo(Hashtable<String, XmppFriend> hashMap,
			XmppFriend friend) {
		String friendJid = friend.getFriendJid();
		hashMap.put(friendJid, friend);
	}

	private void updateContactInfo(List<CellOneContactData> list,
			XmppFriend xmpFriend) {
		String jid = xmpFriend.getFriendJid();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			CellOneContactData data = list.get(i);
			String contactJid = data.getContactJid();

			if (contactJid.equals(jid)) {
				updateCellContactData(xmpFriend, data);
				break;
			}
		}
	}

	private void updateCellContactData(XmppFriend xmpFriend,
			CellOneContactData data) {
		String showName = xmpFriend.getShowName();
		String showNamePinyin = xmpFriend.getShowNamePinyin();
		ArrayList<String> showNamePinyinList = getList(showNamePinyin);

		/** 仔细考虑，是否放开 **/
		XmppFriendPresenExtra presenExtra = xmpFriend.getFriendPresenExtra();
		int newPresenceType = presenExtra.getPresenceState();
		int newUserState = FriendOperation.getMappedStatus(newPresenceType);

		// photo不在这里更新，但是后面要记得这个信息是要更新的
		// data.setContactPhoto(contactPhoto)
		data.setContactName(showName);
		data.setNamePinyinList(showNamePinyinList);
		data.setUserState(newUserState);

		setPinyinCatlog(data, showNamePinyin);
	}

	private void setPinyinCatlog(CellOneContactData data, String showNamePinyin) {
		if ((showNamePinyin == null)
				|| showNamePinyin.equals(Constants.EMPTY_STRING)) {
			return;
		}

		int catlog = data.getFriendPinyinCatlog();
		int starCatlog = Constants.FriendPinyinCatlog.FRIEND_STAR;
		if (catlog == starCatlog) {
			return;
		}

		char letter = showNamePinyin.charAt(0);
		boolean isLetter = Character.isLetter(letter);
		if (isLetter) {
			catlog = Constants.FriendPinyinCatlog.FRIEND_NORMAL;
		} else {
			catlog = Constants.FriendPinyinCatlog.FRIEND_EXTRA;
		}

		data.setFriendPinyinCatlog(catlog);
	}

	/* 关于XMPP联系人部分数据更新 end */

	/* 关于通讯录部分数据更新 begin */

	private void fillData(List<CellOneContactData> listAllDesti,
			Hashtable<String, AddressBookData> addressbookDataMap,
			ArrayList<AddressBookData> addressBookDataList) {
		if (addressBookDataList == null) {
			return;
		}

		int size = addressBookDataList.size();
		for (int i = 0; i < size; i++) {
			AddressBookData addressBookData = addressBookDataList.get(i);
			String addsbookId = addressBookData.getAddbookId();

			boolean isExist = addressbookDataMap.containsKey(addsbookId);
			if (isExist) {
				updateContactInfo(listAllDesti, addressBookData);
				addressbookDataMap.put(addsbookId, addressBookData);
				continue;
			}

			String displayName = addressBookData.getRootName();
			CellOneContactData data = constructOrganization(addsbookId,
					displayName);
			listAllDesti.add(data);
			addressbookDataMap.put(addsbookId, addressBookData);
		}
	}

	private void updateContactInfo(List<CellOneContactData> list,
			AddressBookData addressBookData) {
		String jid = addressBookData.getAddbookId();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			CellOneContactData data = list.get(i);
			String contactJid = data.getContactJid();

			if (contactJid.equals(jid)) {
				updateCellContactData(addressBookData, data);
				break;
			}
		}
	}

	private void updateCellContactData(AddressBookData addressBookData,
			CellOneContactData data) {
		String displayName = addressBookData.getRootName();
		data.setContactName(displayName);

		String displayNamePinyin = PinYinUtil.getPinYin(displayName);
		ArrayList<String> showNamePinyinList = new ArrayList<String>();
		showNamePinyinList.add(displayNamePinyin);
		data.setNamePinyinList(showNamePinyinList);
	}

	private CellOneContactData constructOrganization(String addsbookId,
			String displayName) {
		if (mDefOrganizationAvatarBp == null) {
			mDefOrganizationAvatarBp = getBitmap(R.drawable.addsbook_def_organization);
		}

		CellOneContactData cellOneContactData;
		int style = CellOneContactView.CONTACT_CONTROL.CONTACT_CONTROL_ADDRESSBOOK
				.ordinal();
		cellOneContactData = new CellOneContactData(style);
		cellOneContactData.setContactPhoto(mDefOrganizationAvatarBp);
		cellOneContactData.setContactName(displayName);
		cellOneContactData.setContactJid(addsbookId);

		String displayNamePinyin = PinYinUtil.getPinYin(displayName);
		ArrayList<String> showNamePinyinList = new ArrayList<String>();
		showNamePinyinList.add(displayNamePinyin);
		cellOneContactData.setNamePinyinList(showNamePinyinList);

		// 注意：对于通讯录部分，我们这里不管星标的事情，如果后面交互要考虑，我们再修改
		boolean isStar = false;
		int catlog = getCatlog(isStar);
		cellOneContactData.setFriendPinyinCatlog(catlog);
		setPinyinCatlog(cellOneContactData, displayNamePinyin);

		return cellOneContactData;
	}

	/* 关于通讯录部分数据更新 end */

	/* 数据库操作部分 begin */

	private ArrayList<AddressBookData> readFromDb(String myBareJid) {
		ArrayList<AddressBookData> list = doReadAddressBookDataFromDb(myBareJid);
		return list;
	}

	private ArrayList<AddressBookData> doReadAddressBookDataFromDb(
			String myBareJid) {
		ArrayList<AddressBookData> list = null;

		Context context = KunlunApplication.getContext();
		TbAddressbookController controller = new TbAddressbookController(
				context);

		try {
			controller.open();
			list = controller.getAddressBookDataList(myBareJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return list;
	}

	private void writeToDb(ArrayList<AddressBookData> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			AddressBookData data = list.get(i);
			doWriteAddressBookDataToDb(data);
		}
	}

	private void doWriteAddressBookDataToDb(AddressBookData data) {
		Context context = KunlunApplication.getContext();
		TbAddressbookController controller = new TbAddressbookController(
				context);

		try {
			controller.open();
			controller.insertOrNeededUpdate(data);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	/* 数据库操作部分 end */

	/* ws操作部分 begin */

	public void doWsRequestLogin() {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		String myJid = XmppOperation.getMyBareJid();

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestLogin(nameSpace, endPoint, prefix, myJid, handleTypeSync);
	}

	private void wsRequestLogin(String nameSpace, String endPoint,
			String prefix, String myJid, int handleType) {
		String methodName = WebserviceConstants.GLOCAL_METHOD_LOGIN;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(myJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestGetAddsbookListview(String sessionid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_ADDSBOOK;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestGetAddsbookListview(nameSpace, endPoint, prefix, sessionid,
				handleTypeSync);
	}

	private void wsRequestGetAddsbookListview(String nameSpace,
			String endPoint, String prefix, String sessionid, int handleType) {
		String methodName = WebserviceConstants.ADDSBOOK_METHOD_GET_ADDSBOOK_LISTVIEW;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter
				.getArgus(sessionid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	/* ws操作部分 end */

	/* switch activity section begin */

	public void doSwitchActivity(Context context,
			Class<? extends Activity> targetActivity) {
		mMyXmppCallback.setAddsbookActiyCallback(null);
		switchActivity(context, targetActivity, null);
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

	public void doSwitchMainActivity(Context context,
			Class<? extends Activity> targetActivity) {
		mMyXmppCallback.setAddsbookActiyCallback(null);
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchHuddleActivity(Context context,
			Class<? extends Activity> targetActivity) {
		mMyXmppCallback.setAddsbookActiyCallback(null);
		switchActivityWithNoFinish(context, targetActivity, null);
	}

	private void doSwitchContactDetailActivity(Context context,
			Class<? extends Activity> targetActivity, XmppFriend xmpFriend) {
		mMyXmppCallback.setAddsbookActiyCallback(null);

		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_ADDSBOOK_CONTACT_INFO_KEY, xmpFriend);

		switchActivity(context, targetActivity, bundle);
	}

	private void doSwitchMessageChatActivity(Context context,
			Class<? extends Activity> targetActivity, XmppFriend xmpFriend) {
		mMyXmppCallback.setAddsbookActiyCallback(null);

		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_ADDSBOOK_MESSAGE_CHAT_KEY, xmpFriend);

		switchActivity(context, targetActivity, bundle);
	}

	private void doSwitchAddsbookOrgActivity(Context context,
			Class<? extends Activity> targetActivity, String addsbookId,
			String addsbookContent) {
		mMyXmppCallback.setAddsbookActiyCallback(null);

		Bundle bundle = new Bundle();
		bundle.putString(INTENT_ADDSBOOK_AND_ORG_ADDSBOOK_ID, addsbookId);
		bundle.putString(INTENT_ADDSBOOK_AND_ORG_ADDSBOOK_CONTENT,
				addsbookContent);

		switchActivity(context, targetActivity, bundle);
	}

	private void doSwitchHuddleChatActivity(Context context,
			Class<? extends Activity> targetActivity, String roomId) {
		mMyXmppCallback.setAddsbookActiyCallback(null);

		Bundle bundle = new Bundle();
		bundle.putString(INTENT_ROOM_ID, roomId);

		switchActivityWithNoFinish(context, targetActivity, bundle);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		switchActivityWithNoFinish(context, targetActivity, bundle);
		Activity activity = (Activity) context;
		activity.finish();
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

	/* switch activity section end */

	/* 公用的相对独立部分 begin */

	private void filterAndSortFriendsAndUpdateView() {
		synchronized (mAddsbookAllDataList) {
			filterTypedFriends();
			Collections.sort(mAddsbookAllDataList);
		}

		updateAddsbookAdapterAndView();
	}

	private void sortFriendsAndUpdateView() {
		synchronized (mAddsbookAllDataList) {
			Collections.sort(mAddsbookAllDataList);
		}

		updateAddsbookAdapterAndView();
	}

	private void updateAddsbookAdapterAndView() {
		if (mAddsbookAdapter != null) {
			mAddsbookAdapter.notifyDataSetChanged();
		} else {
			mAddsbookAdapter = getAdapter(mContext, mAddsbookAllDataList);
			mAddsbookAdapter.setListener(mOnCellOneContactBtnAdapterListener);
			mAddsbookAdapter.setListener(mOnCellOneContactViewAdapterListener);
			mAddsbookAdapter.setListener(mOnCellOneContactMenuAdapterListener);

			AlphabetListView.AlphabetPositionListener listener = mAddsbookAdapter
					.getAlphabetPositionListener();
			mIView.setPositionLinstener(listener);
			// 这个要测试，看有木有问题 **
			mIView.updateAddsbookAdapter(mAddsbookAdapter);
		}
	}

	private ArrayList<String> getList(String string) {
		ArrayList<String> stringList = new ArrayList<String>();
		if (string != null) {
			stringList.add(string);
		}

		return stringList;
	}

	private CellOneContactAdapter getAdapter(Context context,
			List<CellOneContactData> list) {
		CellOneContactAdapter cellOneContactAdapter = new CellOneContactAdapter(
				context, list);

		return cellOneContactAdapter;
	}

	private Bitmap getBitmap(int resid) {
		Resources resources = mContext.getResources();
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resid);

		return bitmap;
	}

	private void resolveObject(Object object) {
		Object resolvedObject = mWebsPresenter.resolveObject(object);
		String method = mWebsPresenter.getMethod(resolvedObject);
		Object dataObject = mWebsPresenter.getDataObject(resolvedObject);

		if (method.equals(WebserviceConstants.GLOCAL_METHOD_LOGIN)) {
			String sessionId = null;
			if (dataObject != null) {
				sessionId = (String) dataObject;
			}

			sendMsgToHandler(MSG_WS_lOGIN_COMPLETED, sessionId);
		} else if (method
				.equals(WebserviceConstants.ADDSBOOK_METHOD_GET_ADDSBOOK_LISTVIEW)) {
			ArrayList<TypeAddsbookView> list = null;
			if (dataObject != null) {
				list = (ArrayList<TypeAddsbookView>) dataObject;
			}

			sendMsgToHandler(MSG_WS_GET_ADDSBOOK_LISTVIEW_COMPLETED, list);
		}
	}

	/* 公用的相对独立部分 end */

	@Override
	public void onWsResultNotify(Object object) {
		resolveObject(object);
	}

	@Override
	public void onRecvRoster(Hashtable<String, XmppFriend> hashtable) {
		if (hashtable != null) {
			sendMsgToHandler(MSG_ROSTER_DATA_COMPLETED, hashtable);
		}
	}

	@Override
	public void onRecvRosterPresence(XmppFriend xmpFriend) {
		if (xmpFriend != null) {
			sendMsgToHandler(MSG_ROSTER_PRESENCE_DATA_COMPLETED, xmpFriend);
		}
	}

	@Override
	public void onRecvNickname(XmppFriend xmpFriend) {
		if (xmpFriend != null) {
			sendMsgToHandler(MSG_REV_NICK_NAME, xmpFriend);
		}
	}

	@Override
	public void onRosterItemAdded(XmppFriend xmpFriend) {
		if (xmpFriend != null) {
			Hashtable<String, XmppFriend> hashtable = new Hashtable<String, XmppFriend>();
			String key = xmpFriend.getFriendJid();
			hashtable.put(key, xmpFriend);

			sendMsgToHandler(MSG_ROSTER_ITEM_ADDED, hashtable);
		}
	}

	@Override
	public void onRosterItemRemoved(XmppFriend xmpFriend) {
		if (xmpFriend != null) {
			sendMsgToHandler(MSG_ROSTER_ITEM_REMOVED, xmpFriend);
		}
	}

	@Override
	public void onRosterItemUpdated(XmppFriend xmpFriend) {
		if (xmpFriend != null) {
			sendMsgToHandler(MSG_ROSTER_ITEM_UPDATED, xmpFriend);
		}
	}

	@Override
	public void onLoadUserinfoCompleted(XmppFriend xmppFriend) {

	}

}
