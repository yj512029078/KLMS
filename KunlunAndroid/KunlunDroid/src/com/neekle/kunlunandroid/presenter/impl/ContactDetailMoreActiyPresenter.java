package com.neekle.kunlunandroid.presenter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.widget.Toast;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.ContactDetailMoreAdapter;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.ContactDetailMoreData;
import com.neekle.kunlunandroid.db.TbFriendsController;
import com.neekle.kunlunandroid.presenter.common.WebservicePresenter;
import com.neekle.kunlunandroid.presenter.interf.IContactDetailMoreActivity;
import com.neekle.kunlunandroid.presenter.interf.IContactDetailMoreActiyPresenterCb;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.TypeReturn;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;
import com.neekle.kunlunandroid.web.data.friend.TypeFriend;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.MyXmppCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.XmppService;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

public class ContactDetailMoreActiyPresenter implements
		IContactDetailMoreActiyPresenterCb, ISoapReceivedCallback {

	private static final int MSG_WS_lOGIN_COMPLETED = 80;
	private static final int MSG_WS_FRIEND_GET_LIST_COMPLETED = 81;
	// private static final int MSG_WS_INFO_GET_COMPLETED = 82;
	private static final int MSG_WS_INFO_UPDATE_COMPLETED = 83;

	private static final String FRIEND_TYPE_HUDDLE = "1";
	private static final String MESSAGE_TO_PASS = "message_to_pass";
	private static final String INTENT_CONTACT_DETAIL_AND_MORE_KEY = "contact_detail_and_more_key";

	private List<ContactDetailMoreData> mDataList = new ArrayList<ContactDetailMoreData>();
	private ContactDetailMoreAdapter mAdapter;

	// 临时测试，后面改掉
	private String mWsTestAccount1 = "yj001@fe.shenj.com";
	private String mWsTestAccount2 = "yj002@fe.shenj.com";
	private String mSessionId;
	private TypeFriend mCurrentTypeFriend;
	private TypeFriend mToUpdateTypeFriend;

	private Context mContext;
	private MyXmppCallback mMyXmppCallback;
	private XmppService mXmppService;
	private IContactDetailMoreActivity mIView;
	private WebservicePresenter mWebsPresenter;

	private XmppFriend mIntentSavedXmppFriend;

	public ContactDetailMoreActiyPresenter(IContactDetailMoreActivity view,
			Context context) {
		mIView = view;
		mContext = context;

		mXmppService = XmppService.getSingleton();
		mMyXmppCallback = MyXmppCallback.getSingleton();
		// mMyXmppCallback.setAddsbookActiyCallback(this);

		mWebsPresenter = new WebservicePresenter(context);
		mWebsPresenter.setCallback(this);
	}

	public void init(Intent intent) {
		String myBareJid = XmppOperation.getMyBareJid();
		// 由于ws有问题，所以先使用测试账号进行测试,后面去掉
		myBareJid = mWsTestAccount1;
		doWsRequestLogin(myBareJid);

		Parcelable parcelable = intent
				.getParcelableExtra(INTENT_CONTACT_DETAIL_AND_MORE_KEY);
		XmppFriend xmppFriend = (XmppFriend) parcelable;
		mIntentSavedXmppFriend = xmppFriend;

		// 由于ws有问题，所以先使用测试账号进行测试,后面去掉
		XmppData xmppData = XmppData.getSingleton();
		xmppFriend = xmppData.getFriend(mWsTestAccount2);

		if (xmppFriend != null) {
			String showName = xmppFriend.getShowName();
			mIView.showName(showName);
		}

		fillData(xmppFriend);
		mAdapter = getAdapter(mContext, mDataList);
		mIView.updateContactDetailMoreAdapter(mAdapter);
	}

	private void updateAdapter() {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	public void fillData(XmppFriend xmppFriend) {
		String tittle = mContext.getString(R.string.email);
		String email = null;
		if (xmppFriend != null) {
			email = xmppFriend.getEmail();
		}
		ContactDetailMoreData data = new ContactDetailMoreData(tittle, email);
		mDataList.add(data);

		tittle = mContext.getString(R.string.mobile_phone);
		String mobile = null;
		if (xmppFriend != null) {
			mobile = xmppFriend.getMobilePhone();
		}
		data = new ContactDetailMoreData(tittle, mobile);
		mDataList.add(data);

		tittle = mContext.getString(R.string.company_phone);
		String phone = null;
		if (xmppFriend != null) {
			phone = xmppFriend.getPhone();
		}
		data = new ContactDetailMoreData(tittle, phone);
		mDataList.add(data);

		tittle = mContext.getString(R.string.department);
		data = new ContactDetailMoreData(tittle, null);
		mDataList.add(data);

		tittle = mContext.getString(R.string.home_phone);
		data = new ContactDetailMoreData(tittle, null);
		mDataList.add(data);

		tittle = mContext.getString(R.string.fax);
		data = new ContactDetailMoreData(tittle, null);
		mDataList.add(data);

		tittle = mContext.getString(R.string.personal_website);
		data = new ContactDetailMoreData(tittle, null);
		mDataList.add(data);

		// 这段代码，确保mCurrentTypeFriend不为null
		String displayName = XmppOperation.getFriendShowName(mWsTestAccount2);
		String type = null;
		if (XmppOperation.isHuddle(mWsTestAccount2)) {
			type = FRIEND_TYPE_HUDDLE;
		}

		mCurrentTypeFriend = new TypeFriend(displayName, email,
				mWsTestAccount2, null, mobile, phone, type);
	}

	private ContactDetailMoreAdapter getAdapter(Context context,
			List<ContactDetailMoreData> list) {
		ContactDetailMoreAdapter adapter = new ContactDetailMoreAdapter(
				context, list);
		return adapter;
	}

	public void doShowDetail(int position) {
		if ((mDataList != null) && (mDataList.size() != 0)) {
			ContactDetailMoreData data = mDataList.get(position);
			String title = data.getTittle();
			String content = data.getContent();

			mIView.showPopupWindow(title, content);
		}
	}

	public void doOnPopWindowConfirmBtnClick(String title, String newContent) {
		if (mSessionId == null) {
			String hint = mContext.getString(R.string.ws_login_fail);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
			return;
		}

		ContactDetailMoreData data = getData(mDataList, title);
		String oldContent = data.getContent();

		if (oldContent == null) {
			oldContent = Constants.EMPTY_STRING;
		}
		if (newContent == null) {
			newContent = Constants.EMPTY_STRING;
		}

		if (!oldContent.equals(newContent)) {
			TypeFriend typeFriend = (TypeFriend) mCurrentTypeFriend.deepCopy();
			mToUpdateTypeFriend = getTypeFriendToUpdate(typeFriend, title,
					newContent);
			doWsRequestFriendInfoUpdate(mSessionId, mToUpdateTypeFriend);
		} else {
			String hint = mContext
					.getString(R.string.update_contact_info_success);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
		}
	}

	private TypeFriend getTypeFriendToUpdate(TypeFriend typeFriend,
			String title, String newContent) {
		String email = mContext.getString(R.string.email);
		String mobilePhone = mContext.getString(R.string.mobile_phone);
		String companyPhone = mContext.getString(R.string.company_phone);
		String department = mContext.getString(R.string.department);
		String homePhone = mContext.getString(R.string.home_phone);
		String fax = mContext.getString(R.string.fax);
		String personalWebsite = mContext.getString(R.string.personal_website);

		if (title.equals(email)) {
			typeFriend.setEmail(newContent);
		} else if (title.equals(mobilePhone)) {
			typeFriend.setMobile(newContent);
		} else if (title.equals(companyPhone)) {
			typeFriend.setPhone(newContent);
		}

		return typeFriend;
	}

	public void doWriteToDb() {
		String myJid = XmppOperation.getMyBareJid();

		if (mCurrentTypeFriend != null) {
			writeToDb(myJid, mCurrentTypeFriend);
		}
	}

	private void writeToDb(String myJid, TypeFriend data) {
		String email = data.getEmail();
		String mobile = data.getMobile();
		String phone = data.getPhone();
		String friendJid = data.getFriendJid();

		Context context = KunlunApplication.getContext();
		TbFriendsController controller = new TbFriendsController(context);

		try {
			controller.open();
			controller.updateContactDetailMoreInfo(myJid, friendJid, email,
					mobile, phone);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	public void doSwitchContactDetailActivity(Context context,
			Class<? extends Activity> targetActivity) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_CONTACT_DETAIL_AND_MORE_KEY,
				mIntentSavedXmppFriend);

		switchActivity(context, targetActivity, bundle);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
		activity.finish();
	}

	private void doWsRequestLogin(String myBareJid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestLogin(nameSpace, endPoint, prefix, myBareJid, handleTypeSync);
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

	private void doWsRequestFriendAdd(String sessionid, String friendJid,
			String displayName, String phone, String mobie, String email,
			String group) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_FRIEND;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestFriendAdd(nameSpace, endPoint, prefix, sessionid, friendJid,
				displayName, phone, mobie, email, group, handleTypeSync);
	}

	private void wsRequestFriendAdd(String nameSpace, String endPoint,
			String prefix, String sessionid, String friendJid,
			String displayName, String phone, String mobie, String email,
			String group, int handleType) {
		String methodName = WebserviceConstants.FRIEND_METHOD_ADD;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(
				sessionid, friendJid, displayName, phone, mobie, email, group);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestInfoGet(String sessionid, String friendJid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_FRIEND;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestInfoGet(nameSpace, endPoint, prefix, sessionid, friendJid,
				handleTypeSync);
	}

	private void wsRequestInfoGet(String nameSpace, String endPoint,
			String prefix, String sessionid, String friendJid, int handleType) {
		String methodName = WebserviceConstants.FRIEND_METHOD_INFOR_GET;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(
				sessionid, friendJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestFriendGetList(String sessionid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_FRIEND;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestFriendGetList(nameSpace, endPoint, prefix, sessionid,
				handleTypeSync);
	}

	private void wsRequestFriendGetList(String nameSpace, String endPoint,
			String prefix, String sessionid, int handleType) {
		String methodName = WebserviceConstants.FRIEND_METHOD_GET_LIST;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter
				.getArgus(sessionid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestFriendInfoUpdate(String sessionid,
			TypeFriend typeFriend) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_FRIEND;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestFriendInfoUpdate(nameSpace, endPoint, prefix, sessionid,
				typeFriend, handleTypeSync);
	}

	private void wsRequestFriendInfoUpdate(String nameSpace, String endPoint,
			String prefix, String sessionid, TypeFriend typeFriend,
			int handleType) {
		String methodName = WebserviceConstants.FRIEND_METHOD_INFOR_UPDATE;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(
				sessionid, typeFriend);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

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

	private void sendMsgToHandler(int what, LinkedHashMap<String, ?> list) {
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
			int what = msg.what;
			switch (what) {
			case MSG_WS_lOGIN_COMPLETED: {
				handleMsgWsLogin(bundle);
				break;
			}
			case MSG_WS_FRIEND_GET_LIST_COMPLETED: {
				handleMsgWsFriendGetList(bundle);
				break;
			}
			case MSG_WS_INFO_UPDATE_COMPLETED: {
				handleMsgWsFriendInfoUpdate(bundle);
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
		doWsRequestFriendGetList(sessionId);
	}

	private void handleMsgWsFriendGetList(Bundle bundle) {
		Parcelable parcelable = bundle.getParcelable(MESSAGE_TO_PASS);
		if (parcelable == null) {
			String hint = mContext.getString(R.string.get_contact_info_failed);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
			return;
		}

		FriendInfo friendInfo = (FriendInfo) parcelable;
		TypeFriend typeFriend = getTypeFriend(friendInfo);
		updateDataAndView(typeFriend);
	}

	private void handleMsgWsFriendInfoUpdate(Bundle bundle) {
		Parcelable parcelable = bundle.getParcelable(MESSAGE_TO_PASS);
		TypeReturn typeReturn = (TypeReturn) parcelable;

		int code = Constants.DEF_INT_VALUE;
		String codeString = typeReturn.getCode();
		if (codeString != null) {
			code = Integer.valueOf(codeString);
		}

		boolean isSuccess = mWebsPresenter.judgeIfSuccess(code);
		if (isSuccess) {
			mCurrentTypeFriend = mToUpdateTypeFriend;
			updateDataAndView(mCurrentTypeFriend);

			String hint = mContext
					.getString(R.string.update_contact_info_success);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
		} else {
			mToUpdateTypeFriend = mCurrentTypeFriend;

			String hint = mContext
					.getString(R.string.update_contact_info_failed);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
		}
	}

	private TypeFriend getTypeFriend(FriendInfo friendInfo) {
		String displayName = friendInfo.getDisplayName();
		String email = friendInfo.getEmail();
		String friendJid = friendInfo.getFriendJID();

		String group = null;
		ArrayList<String> groupList = friendInfo.getGroupList();
		if ((groupList != null) && groupList.size() != 0) {
			group = new String();

			for (String string : groupList) {
				group += string;
			}
		}

		String mobile = friendInfo.getMobile();
		String phone = friendInfo.getPhone();

		String type = null;
		if (XmppOperation.isHuddle(friendJid)) {
			type = FRIEND_TYPE_HUDDLE;
		}

		TypeFriend typeFriend = new TypeFriend(displayName, email, friendJid,
				group, mobile, phone, type);
		return typeFriend;
	}

	private void updateDataAndView(TypeFriend typeFriend) {
		String email = mContext.getString(R.string.email);
		String mobilePhone = mContext.getString(R.string.mobile_phone);
		String companyPhone = mContext.getString(R.string.company_phone);
		String department = mContext.getString(R.string.department);
		String homePhone = mContext.getString(R.string.home_phone);
		String fax = mContext.getString(R.string.fax);
		String personalWebsite = mContext.getString(R.string.personal_website);

		String emailContent = typeFriend.getEmail();
		ContactDetailMoreData emailData = getData(mDataList, email);
		emailData.setContent(emailContent);

		String mobileContent = typeFriend.getMobile();
		ContactDetailMoreData mobileData = getData(mDataList, mobilePhone);
		mobileData.setContent(mobileContent);

		String phoneContent = typeFriend.getPhone();
		ContactDetailMoreData phoneData = getData(mDataList, companyPhone);
		phoneData.setContent(phoneContent);

		updateAdapter();
		mCurrentTypeFriend = typeFriend;
	}

	private ContactDetailMoreData getData(List<ContactDetailMoreData> list,
			String desireTittle) {
		int size = list.size();
		int i = 0;
		for (; i < size; i++) {
			ContactDetailMoreData data = list.get(i);
			String title = data.getTittle();

			if (title.equals(desireTittle)) {
				break;
			}
		}

		ContactDetailMoreData data = list.get(i);
		return data;
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
		} else if (method.equals(WebserviceConstants.FRIEND_METHOD_GET_LIST)) {
			ArrayList<FriendInfo> list = (ArrayList<FriendInfo>) dataObject;

			FriendInfo friendInfo = null;
			if ((list != null) && (list.size() != 0)) {
				friendInfo = list.get(0);
			}

			sendMsgToHandler(MSG_WS_FRIEND_GET_LIST_COMPLETED, friendInfo);
		} else if (method
				.equals(WebserviceConstants.FRIEND_METHOD_INFOR_UPDATE)) {
			TypeReturn typeReturn = (TypeReturn) dataObject;
			sendMsgToHandler(MSG_WS_INFO_UPDATE_COMPLETED, typeReturn);
		}
	}

	@Override
	public void onWsResultNotify(Object object) {
		resolveWsResult(object);
	}

	@Override
	public void testPresenter() {
		// TODO Auto-generated method stub

	}

}
