package com.neekle.kunlunandroid.presenter.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.xmpp.myWRAP.Presence.PresenceType;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Base64;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.IdGenarater;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.presenter.interf.IAddsbookActiyPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IContactDetailActiyPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.ILoginActiyPresenterCb;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.util.BitmapOperator;
import com.neekle.kunlunandroid.util.FileOperation;
import com.neekle.kunlunandroid.view.controls.cellOneContact.CellOneContactView;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.TypeReturn;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;
import com.neekle.kunlunandroid.web.data.friend.TypeFriendDetails;
import com.neekle.kunlunandroid.web.data.friend.TypeUser;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.XmppService;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

public class FriendOperation implements ISoapReceivedCallback {

	private static final String BACK_SLASH = "/";
	private static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	private static final String BASE_FOLDER_RELATIVE_PATH_TO_SDCARD = "KunLunAndroid/";
	private static final String BASE_FOLDER_ABSOLUTE_PATH = SDCARD_PATH
			+ BACK_SLASH + BASE_FOLDER_RELATIVE_PATH_TO_SDCARD;
	private static final String FILE_SAVE_BELONGED_TYPE = "pictures";
	private static final String FILE_TYPE_PHOTO = "photo";
	private static final String FILE_TYPE_BG = "bg";

	private static final String UNDERLINE = "_";
	private static final String FILE_NAME_SUFFIX = ".jpg";

	private String mFriendJid;

	private static final String MESSAGE_TO_PASS = "message_to_pass";

	private static final int MSG_WS_lOGIN_COMPLETED = 170;
	private static final int MSG_WS_INFO_GET_COMPLETED = 171;

	//这里没有做成单例，而是使用static，确保全局唯一
	private static IContactDetailActiyPresenterCb mIContactDetailActiyPresenterCb;
	private static IAddsbookActiyPresenterCb mIAddsbookActiyPresenterCb;

	public void setContactDetailActiyCallback(
			IContactDetailActiyPresenterCb callback) {
		mIContactDetailActiyPresenterCb = callback;
	}

	public void setAddsbookActiyCallback(IAddsbookActiyPresenterCb callback) {
		mIAddsbookActiyPresenterCb = callback;
	}

	// 这里跟底层转换上来的类相关，需要替换 **
	public static int getMappedStatus(final int swigValue) {
		int result = 0;

		int available = PresenceType.Available.swigValue();
		int away = PresenceType.Away.swigValue();
		int dnd = PresenceType.DND.swigValue();
		int unavailable = PresenceType.Unavailable.swigValue();

		if (swigValue == available) {
			result = CellOneContactView.STATUS.K_IDLE.ordinal();
		} else if (swigValue == away) {
			result = CellOneContactView.STATUS.K_LEAVE.ordinal();
		} else if (swigValue == dnd) {
			result = CellOneContactView.STATUS.K_BUSY.ordinal();
		} else if (swigValue == unavailable) {
			result = CellOneContactView.STATUS.K_OFFLINE.ordinal();
		}

		return result;
	}

	public void doHandleFriendInvite(String partnerBareJid) {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		boolean isNeedInviteToFriendWithMe = info.isNeedInviteToFriendWithMe();
		if (isNeedInviteToFriendWithMe) {
			return;
		}

		approveSubscription(partnerBareJid);
	}

	private void approveSubscription(String partnerBareJid) {
		Context context = KunlunApplication.getContext();
		XmppService xmppService = XmppService.getSingleton();
		XmppOperation xmppOperation = new XmppOperation();

		xmppService.approveSubscription(partnerBareJid);

		String fromFullJid = XmppOperation.getMyFullJid();
		String toBareJid = partnerBareJid;

		int resId = R.string.i_have_approved_partner_invite;
		String msg = context.getString(resId);
		xmppOperation.writeApproveInviteToDb(fromFullJid, toBareJid, msg);
	}

	public void doLoadUserinfoToUpdateXmppFriendAndDb(String friendJid) {
		if (friendJid == null) {
			return;
		}

		mFriendJid = friendJid;
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		String sessionId = info.getSessionId();
		if (sessionId == null) {
			doWsRequestLogin();
		} else {
			doWsRequestInfoGet(sessionId, friendJid);
		}
	}

	private void doWsRequestLogin() {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		String myJid = XmppOperation.getMyBareJid();

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestLogin(nameSpace, endPoint, prefix, myJid, handleTypeSync);
	}

	private void wsRequestLogin(String nameSpace, String endPoint,
			String prefix, String myJid, int handleType) {
		Context context = KunlunApplication.getContext();
		WebservicePresenter websPresenter = new WebservicePresenter(context);
		websPresenter.setCallback(this);
		String methodName = WebserviceConstants.GLOCAL_METHOD_LOGIN;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = websPresenter.getArgus(myJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		websPresenter.action(webserviceParamsObject, handleType);
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
		Context context = KunlunApplication.getContext();
		WebservicePresenter websPresenter = new WebservicePresenter(context);
		websPresenter.setCallback(this);

		String methodName = WebserviceConstants.FRIEND_METHOD_INFOR_GET;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = websPresenter.getArgus(
				sessionid, friendJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		websPresenter.action(webserviceParamsObject, handleType);
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
			case MSG_WS_INFO_GET_COMPLETED: {
				// handleMsgWsFriendGetList(bundle);
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
			return;
		}

		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		info.setSessionId(sessionId);

		if (mFriendJid != null) {
			doWsRequestInfoGet(sessionId, mFriendJid);
		}
	}

	private void resolveWsResult(Object object) {
		Context context = KunlunApplication.getContext();
		WebservicePresenter websPresenter = new WebservicePresenter(context);

		Object resolvedObject = websPresenter.resolveObject(object);
		String method = websPresenter.getMethod(resolvedObject);
		Object dataObject = websPresenter.getDataObject(resolvedObject);

		if (method.equals(WebserviceConstants.GLOCAL_METHOD_LOGIN)) {
			String sessionId = null;
			if (dataObject != null) {
				sessionId = (String) dataObject;
			}

			sendMsgToHandler(MSG_WS_lOGIN_COMPLETED, sessionId);
		} else if (method.equals(WebserviceConstants.FRIEND_METHOD_INFOR_GET)) {
			ArrayList<TypeFriendDetails> list = (ArrayList<TypeFriendDetails>) dataObject;
			TypeFriendDetails typeFriendDetails = null;
			if ((list != null) && (list.size() != 0)) {
				typeFriendDetails = list.get(0);
			}

			XmppData xmppData = XmppData.getSingleton();
			XmppFriend xmppFriend = xmppData.getFriend(mFriendJid);
			updateXmppFriendAndWriteDb(typeFriendDetails, xmppFriend);
		}
	}

	private void updateXmppFriendAndWriteDb(
			TypeFriendDetails typeFriendDetails, XmppFriend xmppFriend) {
		if (typeFriendDetails == null) {
			return;
		}

		if (xmppFriend == null) {
			return;
		}

		TypeUser typeUser = typeFriendDetails.getUserInfor();

		String myBareJid = XmppOperation.getMyBareJid();
		String basePath = BASE_FOLDER_ABSOLUTE_PATH + myBareJid + BACK_SLASH
				+ FILE_SAVE_BELONGED_TYPE + BACK_SLASH + mFriendJid
				+ BACK_SLASH;

		String base64Avatar = typeUser.getAvatar();
		String headPortraitAbsPath = getSavedPicPath(base64Avatar, basePath,
				FILE_TYPE_PHOTO);

		String base64Background = typeUser.getBackground();
		String backgroundPicAbsPath = getSavedPicPath(base64Background,
				basePath, FILE_TYPE_BG);

		String label = typeUser.getLabel();
		String sexName = typeUser.getSexName();

		String rawHeadPortraitAbsPath = xmppFriend.getHeadPortrait();
		FileOperation.deleteFile(rawHeadPortraitAbsPath);

		String rawBackgroundPicAbsPath = xmppFriend.getBackgroundPic();
		FileOperation.deleteFile(rawBackgroundPicAbsPath);

		xmppFriend.setHeadPortrait(headPortraitAbsPath);
		xmppFriend.setBackgroundPic(backgroundPicAbsPath);
		xmppFriend.setSignature(label);
		xmppFriend.setSex(sexName);

		XmppOperation xmppOperation = new XmppOperation();
		xmppOperation.writeXmpFriendToDb(xmppFriend);

		if (mIAddsbookActiyPresenterCb != null) {
			mIAddsbookActiyPresenterCb.onLoadUserinfoCompleted(xmppFriend);
		}

		if (mIContactDetailActiyPresenterCb != null) {
			mIContactDetailActiyPresenterCb.onLoadUserinfoCompleted(xmppFriend);
		}
	}

	private String getSavedPicPath(String base64Data, String basePath,
			String fileType) {
		if (base64Data == null) {
			return null;
		}

		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		Bitmap bitmap = BitmapOperator.bytes2Bitmap(bytes);
		String msgId = IdGenarater.MsgId.generateMsgId();
		String fileName = fileType + UNDERLINE + msgId + FILE_NAME_SUFFIX;
		String filePath = basePath + fileName;
		BitmapOperator.saveBitmapAsJpeg(filePath, bitmap);

		return filePath;
	}

	@Override
	public void onWsResultNotify(Object object) {
		resolveWsResult(object);
	}
}
