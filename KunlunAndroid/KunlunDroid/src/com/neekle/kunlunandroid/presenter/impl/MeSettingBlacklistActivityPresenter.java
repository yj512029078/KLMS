package com.neekle.kunlunandroid.presenter.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.baidu.platform.comapi.map.p;
import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.R.string;
import com.neekle.kunlunandroid.adapter.CellPhoneContactAdapter;
import com.neekle.kunlunandroid.adapter.MeSettingBlacklistAdapter;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.CellPhoneContactData;
import com.neekle.kunlunandroid.data.MeSettingBlacklistData;
import com.neekle.kunlunandroid.presenter.common.WebservicePresenter;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingBlacklistActivity;
import com.neekle.kunlunandroid.screens.SelectContactActivity;
import com.neekle.kunlunandroid.util.BitmapOperator;
import com.neekle.kunlunandroid.util.DisplayUtil;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeUserBlacklist;
import com.neekle.kunlunandroid.web.data.common.TypeReturn;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

public class MeSettingBlacklistActivityPresenter implements
		ISoapReceivedCallback {

	private static final int IMAGE_WIDTH_DIP = 40;
	private static final int IMAGE_HEIGHT_DIP = 40;

	private static final int MSG_WS_lOGIN_COMPLETED = 150;

	private static final String MESSAGE_TO_PASS = "message_to_pass";
	private static final String INTENT_FROM_SETTING = "from_setting";
	private static final String INTENT_SELECTED_USER_LIST_KEY = "UserList";

	private Context mContext;
	private Bitmap mDefBitmap;
	private IMeSettingBlacklistActivity mIView;
	private WebservicePresenter mWebsPresenter;

	private String mSessionId;
	private ArrayList<MeSettingBlacklistData> mArrayList = new ArrayList<MeSettingBlacklistData>();
	private MeSettingBlacklistAdapter mAdapter;

	public MeSettingBlacklistActivityPresenter(
			IMeSettingBlacklistActivity view, Context context) {
		mIView = view;
		mContext = context;

		mWebsPresenter = new WebservicePresenter(context);
		mWebsPresenter.setCallback(this);
	}

	public void init() {
		// 暂时现将登陆的入口放在这里
		doWsRequestLogin();

		Resources res = mContext.getResources();
		int id = R.drawable.cm_mini_avatar;
		mDefBitmap = BitmapFactory.decodeResource(res, id);

		constructData();
		updateAdapterAndView();
	}

	private void updateAdapterAndView() {
		if (mAdapter == null) {
			mAdapter = new MeSettingBlacklistAdapter(mContext, mArrayList);
			mAdapter.setOnAdapterItemClickListener(mOnAdapterItemClickListener);

			mIView.updateAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private MeSettingBlacklistAdapter.OnAdapterItemClickListener mOnAdapterItemClickListener = new MeSettingBlacklistAdapter.OnAdapterItemClickListener() {

		@Override
		public void onClick(MeSettingBlacklistData data) {
			int itemType = data.getItemType();

			switch (itemType) {
			case MeSettingBlacklistAdapter.ItemType.ADD: {
				doOnAddItemClick();
				break;
			}
			case MeSettingBlacklistAdapter.ItemType.DELETE: {
				doOnDeleteItemClick();
				break;
			}
			case MeSettingBlacklistAdapter.ItemType.NORMAL: {
				doOnNormalItemClick(data);
				break;
			}
			default:
				break;
			}
		}
	};

	private void doOnAddItemClick() {
		ArrayList<String> blackList = getCurrentBlacklist();

		int requestCode = 0;
		Class<? extends Activity> targetActivity = SelectContactActivity.class;
		doSwitchHuddleSelectContactActivity(mContext, targetActivity,
				blackList, requestCode);
	}

	private void doOnDeleteItemClick() {
		int size = mArrayList.size();
		for (int i = 0; i < size; i++) {
			MeSettingBlacklistData data = mArrayList.get(i);
			int itemType = data.getItemType();
			if (itemType == MeSettingBlacklistAdapter.ItemType.NORMAL) {
				data.setDeleteIconVisible(true);
			}
		}

		updateAdapterAndView();
	}

	private void doOnNormalItemClick(MeSettingBlacklistData data) {
		boolean isCanDelete = data.isDeleteIconVisible();
		if (isCanDelete) {
			mArrayList.remove(data);
			updateAdapterAndView();

			String contactJid = data.getBareJid();
			rmToUserBlackList(mSessionId, contactJid);
		}
	}

	private void constructData() {
		initBlacklistData();
		addAddAndDeleteView();
	}

	private void initBlacklistData() {
		XmppData xmppData = XmppData.getSingleton();
		Hashtable<String, XmppFriend> hashtable = xmppData.getAllFriends();

		Set<Entry<String, XmppFriend>> set = hashtable.entrySet();
		Iterator<Entry<String, XmppFriend>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<String, XmppFriend> entry = iterator.next();
			XmppFriend xmppFriend = entry.getValue();
			boolean isInBlacklist = getIsInBlacklist(xmppFriend);

			if (isInBlacklist) {
				MeSettingBlacklistData data = getMeSettingBlacklistData(xmppFriend);
				mArrayList.add(data);
			}
		}
	}

	private MeSettingBlacklistData getMeSettingBlacklistData(
			XmppFriend xmppFriend) {
		String jid = xmppFriend.getFriendJid();
		String showName = xmppFriend.getShowName();

		String photoPath = xmppFriend.getHeadPortrait();
		Bitmap photoBitmap = getProperPhoto(photoPath, mDefBitmap);

		int itemType = MeSettingBlacklistAdapter.ItemType.NORMAL;
		MeSettingBlacklistData data = getMeSettingBlacklistData(photoBitmap,
				showName, false, itemType, jid);

		return data;
	}

	private boolean getIsInBlacklist(XmppFriend data) {
		return data.isInBlacklist();
	}

	private void addAddAndDeleteView() {
		Resources res = mContext.getResources();

		int addId = R.drawable.cm_add_bg_normal;
		Bitmap addBitmap = BitmapFactory.decodeResource(res, addId);
		String name = mContext.getString(R.string.add);
		int itemType = MeSettingBlacklistAdapter.ItemType.ADD;
		MeSettingBlacklistData addData = getMeSettingBlacklistData(addBitmap,
				name, false, itemType, null);
		mArrayList.add(addData);

		int deleteId = R.drawable.cm_delete_bg_normal;
		Bitmap deleteBitmap = BitmapFactory.decodeResource(res, deleteId);
		name = mContext.getString(R.string.delete);
		itemType = MeSettingBlacklistAdapter.ItemType.DELETE;
		MeSettingBlacklistData deleteData = getMeSettingBlacklistData(
				deleteBitmap, name, false, itemType, null);
		mArrayList.add(deleteData);
	}

	private MeSettingBlacklistData getMeSettingBlacklistData(Bitmap bitmap,
			String name, boolean isDeleteIconShow, int itemType, String bareJid) {
		MeSettingBlacklistData data = new MeSettingBlacklistData();
		data.setBitmap(bitmap);
		data.setName(name);
		data.setDeleteIconVisible(isDeleteIconShow);
		data.setItemType(itemType);
		data.setBareJid(bareJid);

		return data;
	}

	private ArrayList<String> getCurrentBlacklist() {
		ArrayList<String> arrayList = new ArrayList<String>();

		int size = mArrayList.size();
		for (int i = 0; i < size; i++) {
			MeSettingBlacklistData data = mArrayList.get(i);
			int itemType = data.getItemType();
			if (itemType == MeSettingBlacklistAdapter.ItemType.NORMAL) {
				String bareJid = data.getBareJid();
				arrayList.add(bareJid);
			}
		}

		return arrayList;
	}

	public void resolveToGetSelectedContacts(Intent intent) {
		boolean isHaveNewAdded = false;

		ArrayList<String> newArrayList = intent
				.getStringArrayListExtra(INTENT_SELECTED_USER_LIST_KEY);
		if (newArrayList != null) {
			addNewSelectedContactToDataListAndKeepOrder(newArrayList);
			isHaveNewAdded = true;
		}

		updateDataListToResetView();
		updateAdapterAndView();

		if (!isHaveNewAdded) {
			return;
		}

		if (mSessionId != null) {
			int size = newArrayList.size();
			for (int i = 0; i < size; i++) {
				String contactJid = newArrayList.get(i);
				addToUserBlackList(mSessionId, contactJid);
			}
		}
	}

	private void addToUserBlackList(String sessionId, String contactJid) {
		doUpdateXmppFriendAndWriteDb(contactJid, true);

		int opType = WebserviceConstants.USER_METHOD_USER_BLACKLIST_ARGS_OP_TYPE_ADD;
		doWsRequestUserBlackList(sessionId, opType, contactJid);
	}

	private void rmToUserBlackList(String sessionId, String contactJid) {
		doUpdateXmppFriendAndWriteDb(contactJid, false);

		int opType = WebserviceConstants.USER_METHOD_USER_BLACKLIST_ARGS_OP_TYPE_DELETE;
		doWsRequestUserBlackList(sessionId, opType, contactJid);
	}

	private void addNewSelectedContactToDataListAndKeepOrder(
			ArrayList<String> newJidList) {
		int newSize = newJidList.size();
		for (int i = 0; i < newSize; i++) {
			String bareJid = newJidList.get(i);
			XmppData xmppData = XmppData.getSingleton();
			XmppFriend xmppFriend = xmppData.getFriend(bareJid);

			String showName = xmppFriend.getShowName();
			String path = xmppFriend.getHeadPortrait();
			Bitmap bitmap = getProperPhoto(path, mDefBitmap);

			int itemType = MeSettingBlacklistAdapter.ItemType.NORMAL;

			MeSettingBlacklistData data = getMeSettingBlacklistData(bitmap,
					showName, false, itemType, bareJid);

			int index = computeInsertIndex(mArrayList);
			mArrayList.add(index, data);
		}
	}

	private int computeInsertIndex(ArrayList<MeSettingBlacklistData> arrayList) {
		int i = 0;
		int size = mArrayList.size();
		for (; i < size; i++) {
			MeSettingBlacklistData data = mArrayList.get(i);
			int itemType = data.getItemType();

			if (itemType != MeSettingBlacklistAdapter.ItemType.NORMAL) {
				break;
			}
		}

		return i;
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

	private void updateDataListToResetView() {
		int size = mArrayList.size();
		for (int i = 0; i < size; i++) {
			MeSettingBlacklistData data = mArrayList.get(i);
			int itemType = data.getItemType();

			if (itemType == MeSettingBlacklistAdapter.ItemType.NORMAL) {
				data.setDeleteIconVisible(false);
			}
		}
	}

	private void doUpdateXmppFriendAndWriteDb(String contactJid,
			boolean isInBlacklist) {
		XmppOperation xmppOperation = new XmppOperation();

		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriend(contactJid);
		if (xmppFriend != null) {
			xmppFriend.setInBlacklist(isInBlacklist);
			xmppOperation.writeXmpFriendToDb(xmppFriend);
		}
	}

	private void doWsRequestLogin() {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		String myJid = XmppOperation.getMyBareJid();

		int handleTypeSync = WebserviceConstants.TaskThreadType.WEBSERVICE_INTENT_SERVICE_TYPE;
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

	private void doWsRequestUserBlackList(String sessionid, int opType,
			String contactJid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_USER;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.WEBSERVICE_INTENT_SERVICE_TYPE;
		wsRequestUserBlackList(nameSpace, endPoint, prefix, sessionid, opType,
				contactJid, handleTypeSync);
	}

	private void wsRequestUserBlackList(String nameSpace, String endPoint,
			String prefix, String sessionid, int opType, String contactJid,
			int handleType) {
		String methodName = WebserviceConstants.USER_METHOD_USER_BLACKLIST;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(
				sessionid, opType, contactJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestUserGetBlackList(String sessionid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_USER;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.WEBSERVICE_INTENT_SERVICE_TYPE;
		wsRequestUserGetBlackList(nameSpace, endPoint, prefix, sessionid,
				handleTypeSync);
	}

	private void wsRequestUserGetBlackList(String nameSpace, String endPoint,
			String prefix, String sessionid, int handleType) {
		String methodName = WebserviceConstants.USER_METHOD_USER_GET_BLACKLIST;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter
				.getArgus(sessionid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	public void doSwitchMeSettingPrivacyActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchHuddleSelectContactActivity(Context context,
			Class<? extends Activity> targetActivity,
			ArrayList<String> blacklist, int requestCode) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(INTENT_FROM_SETTING, true);
		bundle.putStringArrayList(INTENT_SELECTED_USER_LIST_KEY, blacklist);

		switchActivityForResult(context, targetActivity, bundle, requestCode);
	}

	private void switchActivityForResult(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle,
			int requestCode) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		activity.startActivityForResult(intent, requestCode);
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

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			int what = msg.what;
			switch (what) {
			case MSG_WS_lOGIN_COMPLETED: {
				handleMsgWsLogin(bundle);
				break;
			}
			default: {
				break;
			}

			}
		}

	};

	private void handleMsgWsLogin(Bundle bundle) {
		String sessionId = bundle.getString(MESSAGE_TO_PASS);

		if (sessionId == null) {
			String hint = mContext.getString(R.string.ws_login_fail);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
			return;
		}

		mSessionId = sessionId;
		// 临时
		// doWsRequestUserGetBlackList(sessionId);
	}

	private void resolveWsResult(Object object) {
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
				.equals(WebserviceConstants.USER_METHOD_USER_BLACKLIST)) {
			// ...
			// can do sth
		}
	}

	@Override
	public void onWsResultNotify(Object object) {
		resolveWsResult(object);
	}

}
