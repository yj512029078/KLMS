package com.neekle.kunlunandroid.xmpp;

import java.util.ArrayList;
import java.util.Hashtable;

import org.xmpp.myWRAP.AddressBookInfo;
import org.xmpp.myWRAP.AvatarMetadata;
import org.xmpp.myWRAP.Bytestream.StreamType;
import org.xmpp.myWRAP.BytestreamData;
import org.xmpp.myWRAP.ConnectionError;
import org.xmpp.myWRAP.CreateMUCRoomError;
import org.xmpp.myWRAP.FileMetadata;
import org.xmpp.myWRAP.Geoloc;
import org.xmpp.myWRAP.IMailSMTPInfo;
import org.xmpp.myWRAP.JID;
import org.xmpp.myWRAP.LogArea;
import org.xmpp.myWRAP.LogLevel;
import org.xmpp.myWRAP.MUCRoomAffiliation;
import org.xmpp.myWRAP.MUCRoomConfig;
import org.xmpp.myWRAP.MUCRoomInfo;
import org.xmpp.myWRAP.MUCRoomRole;
import org.xmpp.myWRAP.Message;
import org.xmpp.myWRAP.Microblog;
import org.xmpp.myWRAP.Presence;
import org.xmpp.myWRAP.klcppwrapJNI;
import org.xmpp.myWRAP.Presence.PresenceType;
import org.xmpp.myWRAP.Roster;
import org.xmpp.myWRAP.RosterItem;
import org.xmpp.myWRAP.StringList;
import org.xmpp.myWRAP.SubscriptionType;
import org.xmpp.myWRAP.XmppCallback;
import org.xmpp.myWRAP.XmppStack;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.HuddleChatMessageInfo;
import com.neekle.kunlunandroid.data.MessageComType;
import com.neekle.kunlunandroid.data.MicroblogInfo;
import com.neekle.kunlunandroid.db.DbConstantsType;
import com.neekle.kunlunandroid.db.TbHuddleInfoController;
import com.neekle.kunlunandroid.db.TbMultiChatRoomInfoController;
import com.neekle.kunlunandroid.db.TbRecordChatSumController;
import com.neekle.kunlunandroid.db.TbRecordMicroblogController;
import com.neekle.kunlunandroid.db.TbRecordMultiChatController;
import com.neekle.kunlunandroid.db.TbUINotificationController;
import com.neekle.kunlunandroid.presenter.interf.IAddsbookActiyPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IHuddleChatActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IHuddleChatDetailedSettingActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IHuddleChatModifyNickNameActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IHuddleChatNameActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IHuddleChatSettingActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.ISelectContactActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IInviteAddFriendActiyPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.ILoginActiyPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IMainScreenActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IManipulateActiyPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IMessageChatActiyPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IMessageSessionActiyPresenterCb;

import com.neekle.kunlunandroid.presenter.interf.IMicroBlogWritingActivityPresenterCb;
import com.neekle.kunlunandroid.presenter.interf.IMicroblogListActivity;
import com.neekle.kunlunandroid.presenter.interf.IMicroblogListActivityPresenterCb;

import com.neekle.kunlunandroid.screens.KunlunApplication;

import com.neekle.kunlunandroid.services.AutoConnectService;
import com.neekle.kunlunandroid.util.ChatMessageUtil;
import com.neekle.kunlunandroid.util.LogUtil;
import com.neekle.kunlunandroid.view.controls.R.string;
import com.neekle.kunlunandroid.xmpp.data.XmppConnectionError;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;
import com.neekle.kunlunandroid.xmpp.data.XmppSingleChatMessage;

public class MyXmppCallback extends XmppCallback {

	private static final String LOGIN_TAG = "login_tag";

	private volatile ILoginActiyPresenterCb mILoginActiyPresenterCb;
	private volatile IManipulateActiyPresenterCb mIManipulateActiyPresenterCb;
	private volatile IAddsbookActiyPresenterCb mIAddsbookActiyPresenterCallback;
	private volatile IInviteAddFriendActiyPresenterCb mIInviteAddFriendActiyPresenterCb;
	private volatile IMessageChatActiyPresenterCb mIMessageChatActiyPresenterCb;

	private volatile IHuddleChatActivityPresenterCb mIHuddleChatActivityPresenterCb;
	private volatile IHuddleChatDetailedSettingActivityPresenterCb mIHuddleChatDetailedSettingActivityPresenterCb;
	private volatile IHuddleChatSettingActivityPresenterCb mIHuddleChatSettingActivityPresenterCb;
	private volatile ISelectContactActivityPresenterCb mIHuddleSelectContactActivityPresenterCb;
	private volatile IMessageSessionActiyPresenterCb mIMessageSessionActiyPresenterCb;
	private volatile IHuddleChatNameActivityPresenterCb mIHuddleChatNameActivityPresenterCb;
	private volatile IHuddleChatModifyNickNameActivityPresenterCb mIHuddleChatModifyNickNameActivityPresenterCb;
	private volatile IMicroBlogWritingActivityPresenterCb mIMicroBlogWritingActivityPresenterCb;
	private volatile IMicroblogListActivityPresenterCb mIMicroblogListActivityPresenterCb;

	private volatile IMainScreenActivityPresenterCb mIMainScreenActivityPresenterCb;

	private volatile static MyXmppCallback mSingleton;
	private XmppStack mXmppStack;
	private XmppService mXmppService;
	private XmppData mXmppData;
	private XmppOperation mXmppCbOperation;

	private Context mAppContext;

	private MyXmppCallback() {
		mXmppService = XmppService.getSingleton();
		mXmppData = XmppData.getSingleton();
		mXmppCbOperation = new XmppOperation();
	}

	public static MyXmppCallback getSingleton() {
		if (mSingleton == null) {
			synchronized (MyXmppCallback.class) {
				if (mSingleton == null) {
					mSingleton = new MyXmppCallback();
				}
			}
		}

		return mSingleton;
	}

	public void setAppContext(Context context) {
		mAppContext = context;
		mXmppCbOperation.setContext(context);
	}

	public void setXmppStack(XmppStack xmppStack) {
		mXmppStack = xmppStack;
	}

	public XmppStack getXmppStack() {
		return mXmppStack;
	}

	public void setListenNetStateServiceCallback(ILoginActiyPresenterCb callback) {
		mILoginActiyPresenterCb = callback;
	}

	public void setLoginActiyCallback(ILoginActiyPresenterCb callback) {
		mILoginActiyPresenterCb = callback;
	}

	public void setManipulateActiyCallback(IManipulateActiyPresenterCb callback) {
		mIManipulateActiyPresenterCb = callback;
	}

	public void setAddsbookActiyCallback(IAddsbookActiyPresenterCb callback) {
		mIAddsbookActiyPresenterCallback = callback;
	}

	public void setInviteAddFriendActiyCallback(
			IInviteAddFriendActiyPresenterCb callback) {
		mIInviteAddFriendActiyPresenterCb = callback;
	}

	public void setMessageChatActivityCallback(
			IMessageChatActiyPresenterCb callback) {
		mIMessageChatActiyPresenterCb = callback;
	}

	// 主屏
	public void setMainScreenActivityCallBack(
			IMainScreenActivityPresenterCb callback) {

		mIMainScreenActivityPresenterCb = callback;
	}

	public IMainScreenActivityPresenterCb getMainScreenActivityCallBack() {

		return mIMainScreenActivityPresenterCb;
	}

	// 群
	public void setHuddleChatActivityCallBack(
			IHuddleChatActivityPresenterCb callback) {

		mIHuddleChatActivityPresenterCb = callback;
	}

	public void setHuddleChatDetailedSettingActivityCallBack(
			IHuddleChatDetailedSettingActivityPresenterCb callback) {

		mIHuddleChatDetailedSettingActivityPresenterCb = callback;
	}

	public void setHuddleChatSettingActivityCallBack(
			IHuddleChatSettingActivityPresenterCb callback) {

		mIHuddleChatSettingActivityPresenterCb = callback;
	}

	public void setHuddleSelectContactActivityCallBack(
			ISelectContactActivityPresenterCb callback) {

		mIHuddleSelectContactActivityPresenterCb = callback;
	}

	public void setMessageSessionActivityCallBack(
			IMessageSessionActiyPresenterCb callback) {

		mIMessageSessionActiyPresenterCb = callback;
	}

	public void setHuddleChatNameActivityCallBack(
			IHuddleChatNameActivityPresenterCb callback) {

		mIHuddleChatNameActivityPresenterCb = callback;
	}

	public void setHuddleChatModifyNickNameActivityCallBack(
			IHuddleChatModifyNickNameActivityPresenterCb callback) {

		mIHuddleChatModifyNickNameActivityPresenterCb = callback;
	}

	// 微博
	public void setMicroBlogWritingActivityCallBack(
			IMicroBlogWritingActivityPresenterCb callback) {

		mIMicroBlogWritingActivityPresenterCb = callback;
	}

	public void setMicroblogListActivityCallBack(
			IMicroblogListActivityPresenterCb callback) {

		mIMicroblogListActivityPresenterCb = callback;
	}

	public ILoginActiyPresenterCb getMainActiyPresenterCallback() {
		return mILoginActiyPresenterCb;
	}

	public IManipulateActiyPresenterCb getManipulateActiyPresenterCallback() {
		return mIManipulateActiyPresenterCb;
	}

	public IAddsbookActiyPresenterCb getAddsbookActiyPresenterCallback() {
		return mIAddsbookActiyPresenterCallback;
	}

	public IInviteAddFriendActiyPresenterCb getMessageChatActivityPresenterCallback() {
		return mIInviteAddFriendActiyPresenterCb;
	}

	public IMessageChatActiyPresenterCb getInviteAddFriendActiyPresenterCallback() {
		return mIMessageChatActiyPresenterCb;
	}

	public IHuddleChatActivityPresenterCb getHuddleChatActivityPresenterCallback() {

		return mIHuddleChatActivityPresenterCb;
	}

	public IHuddleChatSettingActivityPresenterCb getHuddleChatSettingActivityCallBack() {

		return mIHuddleChatSettingActivityPresenterCb;
	}

	public ISelectContactActivityPresenterCb getIHuddleSelectContactActivityCallBack() {

		return mIHuddleSelectContactActivityPresenterCb;
	}

	public IMessageSessionActiyPresenterCb getIMessageSessionActiyCallBack() {

		return mIMessageSessionActiyPresenterCb;
	}

	public IHuddleChatNameActivityPresenterCb getIHuddleChatNameActivityPresenterCallBack() {

		return mIHuddleChatNameActivityPresenterCb;

	}

	public IHuddleChatModifyNickNameActivityPresenterCb getIHuddleChatModifyNickNameActivityPresenterCallBack() {

		return mIHuddleChatModifyNickNameActivityPresenterCb;
	}

	public IMicroBlogWritingActivityPresenterCb getIMicroBlogWritingActivityPresenterCallBack() {

		return mIMicroBlogWritingActivityPresenterCb;
	}

	public IMicroblogListActivityPresenterCb getIMicroblogListActivityPresenterCallBack() {

		return mIMicroblogListActivityPresenterCb;
	}

	/******************** Common, 日志 Start ********************/

	@Override
	public void onLog(LogLevel level_, LogArea area_, String log_) {
		super.onLog(level_, area_, log_);

		if (area_ == LogArea.LogAreaXmlIncoming) {
			String msg = "------onLog<REV>------" + "\n" + log_;
			LogUtil.logInfo(LOGIN_TAG, msg);
		} else if (area_ == LogArea.LogAreaXmlOutgoing) {
			String msg = "------onLog<SEND>------" + "\n" + log_;
			LogUtil.logInfo(LOGIN_TAG, msg);
		} else {
			String msg = "------onLog------" + "\n" + log_;
			LogUtil.logInfo(LOGIN_TAG, msg);
		}
	}

	/******************** Common, 日志 End ********************/

	/******************** 登陆流程 Start ********************/

	@Override
	public void onInvaildJID() {
		// TODO Auto-generated method stub
		super.onInvaildJID();

		String msg = "onInvaildJID";
		LogUtil.logInfo(LOGIN_TAG, msg);

		if (mILoginActiyPresenterCb != null) {
			mILoginActiyPresenterCb.onInvaildJID();
		}
	}

	@Override
	public void onInvaildPassword() {
		// TODO Auto-generated method stub
		super.onInvaildPassword();

		String msg = "onInvaildPassword";
		LogUtil.logError(LOGIN_TAG, msg);

		if (mILoginActiyPresenterCb != null) {
			mILoginActiyPresenterCb.onInvaildPassword();
		}
	}

	@Override
	public void onTcpConnSuccess() {
		// TODO Auto-generated method stub
		super.onTcpConnSuccess();

		String msg = "onTcpConnSuccess";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	@Override
	public void onTcpConnFailed(ConnectionError error_) {
		// TODO Auto-generated method stub
		super.onTcpConnFailed(error_);

		boolean isLogined = mXmppService.getIsLogined();
		Log.e("logintest", "onTcpConnFailed isLogined: " + isLogined);

		int swigValue = error_.swigValue();
		String errorMsg = error_.toString();
		XmppConnectionError xmpConnectionError = new XmppConnectionError(
				swigValue, errorMsg);

		AutoConnectService.onTcpConnFailed(xmpConnectionError);

		if (mILoginActiyPresenterCb != null) {
			mILoginActiyPresenterCb.onTcpConnFailed(xmpConnectionError);
		}
	}

	@Override
	public void onNegotiatingEncryption() {
		// TODO Auto-generated method stub
		super.onNegotiatingEncryption();

		String msg = "onNegotiatingEncryption";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	@Override
	public void onNegotiatingCompression() {
		// TODO Auto-generated method stub
		super.onNegotiatingCompression();

		String msg = "onNegotiatingCompression";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	@Override
	public void onAuthenticating() {
		// TODO Auto-generated method stub
		super.onAuthenticating();

		String msg = "onAuthenticating";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	@Override
	public void onAuthFailed() {
		// TODO Auto-generated method stub
		super.onAuthFailed();

		String msg = "onAuthFailed";
		LogUtil.logError(LOGIN_TAG, msg);

		if (mILoginActiyPresenterCb != null) {
			mILoginActiyPresenterCb.onAuthFailed();
		}
	}

	@Override
	public void onBindingResource() {
		// TODO Auto-generated method stub
		super.onBindingResource();

		String msg = "onBindingResource";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	@Override
	public void onCreatingSession() {
		// TODO Auto-generated method stub
		super.onCreatingSession();

		String msg = "onCreatingSession";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	@Override
	public void onLoadingRoster() {
		// TODO Auto-generated method stub
		super.onLoadingRoster();

		String msg = "onLoadingRoster";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();

		String msg = "onLoginSuccess";
		LogUtil.logInfo(LOGIN_TAG, msg);

		mXmppCbOperation.preLoadDataBaseData();
		mXmppCbOperation.preLoadInputControlDataBaseData();

		AutoConnectService.onLoginSuccess();

		if (mILoginActiyPresenterCb != null) {
			// 说明：这里回调到应用层后，会在当前线程执行写数据库操作
			mILoginActiyPresenterCb.onLoginSuccess();
		}

		// 说明：这里要加载数据库存储的配置信息，要在写数据库的后面
		KunlunApplication.onRevXmppLoginSuccess();
	}

	@Override
	public void onLogoutSuccess() {
		// TODO Auto-generated method stub
		super.onLogoutSuccess();

		boolean isLogined = mXmppService.getIsLogined();
		Log.e("logintest", "onLogoutSuccess isLogined: " + isLogined);

		String msg = "onLogoutSuccess";
		LogUtil.logInfo(LOGIN_TAG, msg);
	}

	/******************** 登陆流程 End ********************/

	/******************** Registration流程 Start ********************/
	@Override
	public void onRetrieveRegistrationInfo(String username_, String name_,
			String email_) {
		// TODO Auto-generated method stub
		super.onRetrieveRegistrationInfo(username_, name_, email_);
	}

	/******************** Registration流程 End ********************/

	/******************** Attention流程 Start ********************/

	/******************** MUC流程 Start ********************/
	@Override
	public void onModifyMUCRoomMemberListSuccess(JID room_) {
		// TODO Auto-generated method stub
		super.onModifyMUCRoomMemberListSuccess(room_);
	}

	@Override
	public void onModifyMUCRoomMemberListFailed(JID room_) {
		// TODO Auto-generated method stub
		super.onModifyMUCRoomMemberListFailed(room_);
	}

	@Override
	public void onModifyMUCRoomOwnerListSuccess(JID room_) {
		// TODO Auto-generated method stub
		super.onModifyMUCRoomOwnerListSuccess(room_);
	}

	@Override
	public void onModifyMUCRoomOwnerListFailed(JID room_) {
		// TODO Auto-generated method stub
		super.onModifyMUCRoomOwnerListFailed(room_);
	}

	@Override
	public void onRetrieveMUCRoomMemberList(JID room_, StringList members_) {
		// TODO Auto-generated method stub
		super.onRetrieveMUCRoomMemberList(room_, members_);
	}

	@Override
	public void onRetrieveMUCRoomOwnerList(JID room_, StringList owners_) {
		// TODO Auto-generated method stub
		super.onRetrieveMUCRoomOwnerList(room_, owners_);
	}

	@Override
	public void onChangeMUCNicknameConflict(JID room_) {
		// TODO Auto-generated method stub
		super.onChangeMUCNicknameConflict(room_);
	}

	/**
	 * 群：创建房间成功
	 * 
	 * */
	@Override
	public void onCreateMUCRoomSuccess(JID room_) {
		// TODO Auto-generated method stub
		super.onCreateMUCRoomSuccess(room_);

		XmppJid huddleRoom_ = new XmppJid(room_.full());

		mIHuddleChatActivityPresenterCb
				.onCreateHuddleRoomSuccessCb(huddleRoom_);
	}

	/**
	 * 创建群-配置房间成功
	 * 
	 * */
	@Override
	public void onConfigMUCRoomSuccess(JID room_) {
		// TODO Auto-generated method stub
		super.onConfigMUCRoomSuccess(room_);

		XmppJid huddleRoomJID = new XmppJid(room_.full());

		String roomBareJid = room_.bare();

		if (mIHuddleChatActivityPresenterCb != null) {

			mIHuddleChatActivityPresenterCb
					.onConfigHuddleRoomSuccess(huddleRoomJID);

		}

		String myJid = mXmppCbOperation.getDbMyJid();

		mXmppCbOperation.insertHuddleInfoToDb(myJid, roomBareJid);
	}

	/**
	 * 创建群-配置房间失败
	 * 
	 * */
	@Override
	public void onConfigMUCRoomFailed(JID room_) {
		// TODO Auto-generated method stub
		super.onConfigMUCRoomFailed(room_);

		XmppJid huddleRoomJID = new XmppJid(room_.full());

		mIHuddleChatActivityPresenterCb.onConfigMUCRoomFailed(huddleRoomJID);
	}

	/*
	 * 群：接收邀请
	 */
	public void onRecvMUCRoomDirectInvitation(JID room_, JID invitor_,
			String reason_) {

		super.onRecvMUCRoomDirectInvitation(room_, invitor_, reason_);

		// 去除邀请别人入群，自己受到邀请的情况
		String invitorJid = invitor_.bare();
		if ("".equals(invitorJid) || invitorJid == null) {

			return;
		}
		XmppJid huddleRoomJID = new XmppJid(room_.full());

		// 自动接收邀请，即进入房间
		mXmppService.enterHuddle(huddleRoomJID);
		// 查询房间信息
		mXmppService.queryHuddleInfo(huddleRoomJID);
		// 请求获取房间Owner列表
		mXmppService.queryHuddleOwnerList(huddleRoomJID);
		// 查询房间配置信息
		mXmppService.queryMUCRoomConfig(huddleRoomJID);

		// 入库操作
		String roomBareJid = room_.bare();
		String myJid = mXmppCbOperation.getDbMyJid();

		// 此邀请方式一次邀请能够收到五次邀请信息，待询问，所以暂时使用间接邀请
		// 模拟一条群消息入库
		HuddleChatMessageInfo msg = mXmppCbOperation
				.resolveHuddleInvitationMessage(invitorJid, room_);
		// 入库：Tb_Record_Multi_Chat
		mXmppCbOperation.insertHuddleChatMsgToDb(msg);
		// 入库：Tb_Record _ChatSum
		int id = mXmppCbOperation.onHandleRevHuddleMsgChatSumDb(msg);
		// 入库：Tb_UI_Notification 界面通知
		mXmppCbOperation.onHandleRevHuddleMsgUINotificationDb(id, msg);

		// 更新界面
		if (mIMessageSessionActiyPresenterCb != null) {

			mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
		}

		if (mIMainScreenActivityPresenterCb != null) {

			mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
		}

		// 群信息入库
		mXmppCbOperation.insertHuddleInfoToDb(myJid, roomBareJid);
	}

	/*
	 * 群：接收邀请（实）
	 */
	public void onRecvMUCRoomMediatedInvitation(JID room_, JID invitor_,
			String reason_) {
		super.onRecvMUCRoomMediatedInvitation(room_, invitor_, reason_);

		// 去除邀请别人入群，自己受到邀请的情况
		String invitorJid = invitor_.bare();
		if ("".equals(invitorJid) || invitorJid == null) {

			return;
		}

		XmppJid huddleRoomJID = new XmppJid(room_.full());
		// 自动接收邀请，即进入房间
		mXmppService.enterHuddle(huddleRoomJID);
		// 查询房间信息
		mXmppService.queryHuddleInfo(huddleRoomJID);
		// 请求获取房间Owner列表
		mXmppService.queryHuddleOwnerList(huddleRoomJID);
		// 入库操作
		String roomBareJid = room_.bare();
		String myJid = mXmppCbOperation.getDbMyJid();

		// 模拟一条群消息入库
		HuddleChatMessageInfo msg = mXmppCbOperation
				.resolveHuddleInvitationMessage(invitorJid, room_);
		// 入库：Tb_Record_Multi_Chat
		mXmppCbOperation.insertHuddleChatMsgToDb(msg);
		// 入库：Tb_Record _ChatSum
		int id = mXmppCbOperation.onHandleRevHuddleMsgChatSumDb(msg);
		// 入库：Tb_UI_Notification 界面通知
		mXmppCbOperation.onHandleRevHuddleMsgUINotificationDb(id, msg);

		// 更新界面
		if (mIMessageSessionActiyPresenterCb != null) {

			mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
		}

		if (mIMainScreenActivityPresenterCb != null) {

			mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
		}

		mXmppCbOperation.insertHuddleInfoToDb(myJid, roomBareJid);
	}

	/**
	 * 群：群成员状态变化（群成员数变化、状态变化等）回调
	 * 
	 * */
	@Override
	public void onRecvMUCRoomPresence(JID room_,
			String participantOldNickname_, String participantNewNickname_,
			JID participantJid_, Presence pres_, MUCRoomAffiliation affi_,
			MUCRoomRole role_) {
		// TODO Auto-generated method stub
		super.onRecvMUCRoomPresence(room_, participantOldNickname_,
				participantNewNickname_, participantJid_, pres_, affi_, role_);

		String myJid = mXmppCbOperation.getDbMyJid();

		String roomJid = room_.bare();// 裸jid:20140303122418@conference.kevin-pc
		String occupantJid = participantJid_.bare();

		String occupantName = participantNewNickname_;
		if (participantNewNickname_.trim().equalsIgnoreCase("")
				|| participantNewNickname_ == null) {

			occupantName = participantOldNickname_;

		} else {

			if (!participantNewNickname_.equals(participantOldNickname_)) {

				// 群设置修改我的群昵称
				TbHuddleInfoController tbHuddleInfoController = new TbHuddleInfoController(
						mAppContext);
				tbHuddleInfoController.open();
				tbHuddleInfoController.updateNickName(myJid, roomJid,
						participantNewNickname_);
				tbHuddleInfoController.close();

				if (mIHuddleChatModifyNickNameActivityPresenterCb != null
						&& myJid.trim().equals(occupantJid)) {

					mIHuddleChatModifyNickNameActivityPresenterCb
							.updataSelfHuddleNickName(participantNewNickname_);
				}
			}

		}
		String enterLeaveTag = null;

		// 删除群时，服务器推送群成员（只有登陆着自己，其他成员不推送过来）此时occupantJid为""在下面处理也行
		if (affi_.toString().trim()
				.equals(MUCRoomAffiliation.AffiliationOwner.toString())
				&& role_.toString().trim()
						.equals(MUCRoomRole.RoleModerator.toString())) {
			// 是群成员也在房间
			enterLeaveTag = "Enter";

		} else if (affi_.toString().trim()
				.equals(MUCRoomAffiliation.AffiliationNone.toString())
				&& role_.toString().trim()
						.equals(MUCRoomRole.RoleNone.toString())) {
			// 剔除人
			enterLeaveTag = "Exit";

		}

		String dbTime = ChatMessageUtil.getDbTime();

		TbMultiChatRoomInfoController tbMultiChatRoomInfoController = new TbMultiChatRoomInfoController(
				mAppContext);
		tbMultiChatRoomInfoController.open();

		int id = tbMultiChatRoomInfoController.queryIdForRecord(myJid, roomJid,
				occupantName);// tigase踢人时occupantjid为空字符串

		if (id != -1) {

			tbMultiChatRoomInfoController.updateRecordById(id, occupantName,
					enterLeaveTag, dbTime);

		} else {

			tbMultiChatRoomInfoController.insert(myJid, roomJid, occupantJid,
					occupantName, enterLeaveTag, dbTime);
		}

		tbMultiChatRoomInfoController.close();

		/*
		 * 当添加群成员成功时，执行到此要反馈到界面上(此部分应该放在修改权限回调接口处处理)
		 */
		if (mIHuddleSelectContactActivityPresenterCb != null) {

			mIHuddleSelectContactActivityPresenterCb
					.onAddHuddleChatMemberSuccessCb();
		}

	}

	/**
	 * 群：接收群消息
	 * 
	 * */
	@Override
	public void onRecvMUCRoomChatMessage(JID room_, String nickname_,
			Message msg_, String xhtml_) {
		// TODO Auto-generated method stub
		super.onRecvMUCRoomChatMessage(room_, nickname_, msg_, xhtml_);
		String myJid = mXmppCbOperation.getDbMyJid();

		String fromUserName = msg_.from().resource();
		String occupantJid = "";
		// 过滤自己发送的消息及服务器发送的消息
		if (fromUserName.equals("")) {
			// 发送方姓名,为空表明是服务器推送
			return;

		} else {
			// 过滤自己发送的消息

			occupantJid = mXmppCbOperation.getHuddleMemberJidByNickName(
					room_.bare(), fromUserName);

			if (myJid.equals(occupantJid)) {

				return;

			}

		}

		// 将收到的消息转换为自定义结构体数据
		HuddleChatMessageInfo msg = mXmppCbOperation.resolveHuddleChatMsg(
				room_, nickname_, occupantJid, msg_, xhtml_);

		if (msg == null) {

			return;
		}

		// 如果已收到过该消息则屏蔽，否则入库：Tb_Record _Multi_Chat
		boolean bool = mXmppCbOperation.onHandleRevHuddleMsgDb(msg);
		if (!bool) {

			return;
		}

		// 入库并更新到界面
		if (mIHuddleChatActivityPresenterCb != null) {

			mIHuddleChatActivityPresenterCb.onRecvHuddleMessage(msg);

		} else {

			// 入库：Tb_Record _ChatSum
			int id = mXmppCbOperation.onHandleRevHuddleMsgChatSumDb(msg);
			// 入库：Tb_UI_Notification 界面通知
			mXmppCbOperation.onHandleRevHuddleMsgUINotificationDb(id, msg);

			if (mIMessageSessionActiyPresenterCb != null) {

				mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
			}

			if (mIMainScreenActivityPresenterCb != null) {

				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
			}

		}
	}

	/**
	 * 群：修改群主题回调
	 * 
	 * @param room_
	 *            房间的JID
	 * @param nickname_
	 *            修改人的昵称
	 * @param subject_
	 *            主题
	 * */
	@Override
	public void onMUCRoomSubjectChanged(JID room_, String nickname_,
			String subject_) {
		// TODO Auto-generated method stub
		super.onMUCRoomSubjectChanged(room_, nickname_, subject_);

		String myJid = mXmppCbOperation.getDbMyJid();

		XmppJid huddleRoomJID = new XmppJid(room_.full());
		String roomId = room_.bare();
		String showName = subject_;

		TbRecordChatSumController tbRecordChatSumController = new TbRecordChatSumController(
				mAppContext);
		tbRecordChatSumController.open();
		tbRecordChatSumController.updateRecordShowName(myJid, roomId, showName);
		tbRecordChatSumController.close();

		TbHuddleInfoController tbHuddleInfoController = new TbHuddleInfoController(
				mAppContext);
		tbHuddleInfoController.open();
		tbHuddleInfoController.updateHuddleTitle(myJid, roomId, showName);
		tbHuddleInfoController.close();

		// 返回到修改群主题界面结果
		if (mIHuddleChatNameActivityPresenterCb != null) {

			mIHuddleChatNameActivityPresenterCb.onHuddleObjectChanged(
					huddleRoomJID, nickname_, subject_);
		}
		// 如果当前出于消息会话列表页面则实时更新数据
		if (mIMessageSessionActiyPresenterCb != null) {

			mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
		}
		// 如果正修改主题的群正好处于消息会话页面则实时更新数据
		if (mIHuddleChatActivityPresenterCb != null) {

			mIHuddleChatActivityPresenterCb.onHuddleSubjectChanged(
					huddleRoomJID, nickname_, subject_);
		}
		// 如果正修改主题的群正好处于群设置页面则实时更新数据
		if (mIHuddleChatSettingActivityPresenterCb != null) {

			mIHuddleChatSettingActivityPresenterCb.onHuddleSubjectChanged(
					huddleRoomJID, nickname_, subject_);
		}
	}

	@Override
	public void onRetrieveMUCRoomConfig(JID room_, MUCRoomConfig config_) {
		// TODO Auto-generated method stub
		super.onRetrieveMUCRoomConfig(room_, config_);

	}

	/**
	 * 创建房间失败
	 * 
	 * */
	@Override
	public void onCreateMUCRoomFailed(JID room_, CreateMUCRoomError error_) {
		// TODO Auto-generated method stub
		super.onCreateMUCRoomFailed(room_, error_);
		if (mIHuddleChatActivityPresenterCb != null) {

			mIHuddleChatActivityPresenterCb.onCreateMUCRoomFailedCb();
		}

	}

	/**
	 * 删除群 成功
	 * 
	 * */
	@Override
	public void onDestroyMUCRoomSuccess(JID room_) {
		// TODO Auto-generated method stub
		super.onDestroyMUCRoomSuccess(room_);
		// 将Tb_Record _ChatSum 聊天记录汇总表对应房间删除
		// Application application = (KunlunApplication) mAppContext
		// .getApplicationContext();
		// KunlunApplication kunlunApplication = (KunlunApplication)
		// application;
		// String myJid = kunlunApplication.getGlobalUserJid();// 登陆者jid
		// TbRecordChatSumController tbRecordChatSumController = new
		// TbRecordChatSumController(
		// mAppContext);
		// tbRecordChatSumController.open();
		//
		// int id = tbRecordChatSumController
		// .queryIdForRecord(myJid, room_.bare());
		// if (id != -1) {
		// tbRecordChatSumController.delteRecordBy(id);
		// }
		// tbRecordChatSumController.close();

		// 回调到界面上
		XmppJid room = new XmppJid(room_.full());
		mIHuddleChatSettingActivityPresenterCb.onDeleteHuddleSuccess(room);
	}

	/**
	 * 删除群失败
	 * 
	 * */
	@Override
	public void onDestroyMUCRoomFailed(JID room_) {
		// TODO Auto-generated method stub
		super.onDestroyMUCRoomFailed(room_);

		XmppJid room = new XmppJid(room_.full());
		mIHuddleChatSettingActivityPresenterCb.onDeleteHuddleFailed(room);
	}

	@Override
	public void onRetrieveMUCRoomInfo(JID room_, MUCRoomInfo info_) {
		// TODO Auto-generated method stub
		super.onRetrieveMUCRoomInfo(room_, info_);
	}

	@Override
	public void onMUCRoomInvitationRejected(JID room_, JID invitee_,
			String reason_) {
		// TODO Auto-generated method stub
		super.onMUCRoomInvitationRejected(room_, invitee_, reason_);
	}

	/*
	 * 收到位置消息 (non-Javadoc)
	 */
	public void onRecvLocationMessage(String id_, JID from_, Geoloc geoloc_) {

		double longitude = geoloc_.lon();
		double latitude = geoloc_.lat();
		String jidOrRommJid = from_.bare();

		Log.e("onRecvLocationMessage", longitude + "," + latitude
				+ jidOrRommJid);

		XmppSingleChatMessage message = mXmppCbOperation.resolveGeolocMessage(
				id_, from_, geoloc_);

		if (mIMessageChatActiyPresenterCb != null) {

			mXmppCbOperation.writeNormalMsgToDb(message, false);
			mIMessageChatActiyPresenterCb.onRecvChatMessage(message);

		} else {
			mXmppCbOperation.writeNormalMsgToDb(message, true);

			if (mIMessageSessionActiyPresenterCb != null) {
				mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
			}
			if (mIMainScreenActivityPresenterCb != null) {
				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
			}
		}

	}

	/*
	 * 群地理位置
	 */
	public void onRecvMUCRoomLocationMessage(String id_, JID room_,
			String nickname_, Geoloc geoloc_) {

		String occupantJid = mXmppCbOperation.getHuddleMemberJidByNickName(
				room_.bare(), nickname_);

		// 过滤自己发送的消息
		if (mXmppCbOperation.getDbMyJid().equals(occupantJid)) {

			return;
		}

		// 解析消息
		HuddleChatMessageInfo message = mXmppCbOperation
				.resolveHuddleGeolocMessage(id_, occupantJid, room_, nickname_,
						geoloc_);

		// 如果已收到过该消息则屏蔽，否则入库：Tb_Record _Multi_Chat
		boolean bool = mXmppCbOperation.onHandleRevHuddleMsgDb(message);
		if (!bool) {

			return;
		}

		// 通知消息入库并更新到界面
		if (mIHuddleChatActivityPresenterCb != null) {

			mIHuddleChatActivityPresenterCb.onRecvHuddleMessage(message);

		} else {

			// 入库：Tb_Record _ChatSum
			int id = mXmppCbOperation.onHandleRevHuddleMsgChatSumDb(message);
			// 入库：Tb_UI_Notification 界面通知
			mXmppCbOperation.onHandleRevHuddleMsgUINotificationDb(id, message);

			if (mIMessageSessionActiyPresenterCb != null) {

				mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
			}

			if (mIMainScreenActivityPresenterCb != null) {

				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
			}

		}
	}

	/******************** MUC流程 End ********************/

	/******************** AddressBook流程 Start ********************/

	@Override
	public void onNotifyAddressBookChangedSuccess(String uid_) {
		// TODO Auto-generated method stub
		super.onNotifyAddressBookChangedSuccess(uid_);
	}

	@Override
	public void onNotifyAddressBookChangedFailed(String uid_) {
		// TODO Auto-generated method stub
		super.onNotifyAddressBookChangedFailed(uid_);
	}

	@Override
	public void onRecvAddressBookChangedNotification(JID from_,
			AddressBookInfo addressBookInfo_) {
		// TODO Auto-generated method stub
		super.onRecvAddressBookChangedNotification(from_, addressBookInfo_);
	}

	/******************** AddressBook流程 End ********************/

	/******************** 微博流程 Start ********************/

	// 告知发布微博成功
	@Override
	public void onPublishMicroblogSuccess(String id_) {
		// TODO Auto-generated method stub

		mXmppCbOperation.updataSendResultTag(id_, "n");
		if (mIMicroblogListActivityPresenterCb != null) {

			mIMicroblogListActivityPresenterCb.onPublishMicroblogSuccess(id_);

		}

	}

	// 告知发布微博失败
	@Override
	public void onPublishMicroblogFailed(String id_) {
		// TODO Auto-generated method stub

		mXmppCbOperation.updataSendResultTag(id_, "y");
		if (mIMicroblogListActivityPresenterCb != null) {

			mIMicroblogListActivityPresenterCb.onPublishMicroblogFailed(id_);

		}

	}

	// 告知自己删除自己的微博成功
	@Override
	public void onDeleteMicroblogSuccess(String id_) {
		// TODO Auto-generated method stub

		String myJid = XmppData.getSingleton().getJid().getBare();
		// 1、从数据库中删除该记录
		TbRecordMicroblogController controller = new TbRecordMicroblogController(
				mAppContext);
		controller.open();
		controller.deleteRecord(myJid, id_);
		controller.close();

		// 2、更新到界面
		if (mIMicroblogListActivityPresenterCb != null) {

			mIMicroblogListActivityPresenterCb.onDeleteMicroblogSuccess(id_);

		}

	}

	// 告知自己删除自己的微博失败
	@Override
	public void onDeleteMicroblogFailed(String id_) {
		// TODO Auto-generated method stub
		if (mIMicroblogListActivityPresenterCb != null) {

			mIMicroblogListActivityPresenterCb.onDeleteMicroblogFailed(id_);

		}
	}

	// 收到微博
	@Override
	public void onRecvMicroblog(Microblog microblog_) {
		// added by yj
		String fromJid = microblog_.author();
		String fromBareJid = XmppOperation.getBareJid(fromJid);
		XmppFriend xmppFriend = XmppOperation.getXmppFriend(fromBareJid);
		if (xmppFriend != null) {
			boolean flag = xmppFriend.isIgnoreMicroBlog();
			if (flag) {
				return;
			}
		}

		// 过滤自己的微博
		if (microblog_.author().equals(mXmppCbOperation.getMyBareJid())) {

			return;
		}
		// 预处理
		MicroblogInfo info = mXmppCbOperation.processRcvMicroBlog(microblog_);
		// 入库
		mXmppCbOperation.writeMicroBlogRecordIntoDb(info);
		// 更新界面
		if (mIMicroblogListActivityPresenterCb != null) {

			mIMicroblogListActivityPresenterCb.onRecvMicroblog(info);

		} else {
			// 更新首屏
			mXmppCbOperation.onHandleMicroBlogMsgUINotificationDb(info.getId(),
					info);

			if (mIMainScreenActivityPresenterCb != null) {

				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();

			}
		}

	}

	// 告知你的朋友删除了他的微博
	@Override
	public void onMicroblogDeleted(String id_) {
		// TODO Auto-generated method stub
		super.onMicroblogDeleted(id_);
	}

	/******************** 微博流程 End ********************/

	/******************** 文件传输流程 Start ********************/

	@Override
	public void onFtRequest(JID initiator_, String sid_, FileMetadata file_,
			boolean supportS5b_, boolean supportIbb_, boolean supportOob_) {
		// TODO Auto-generated method stub
		super.onFtRequest(initiator_, sid_, file_, supportS5b_, supportIbb_,
				supportOob_);
	}

	@Override
	public void onFtIncomingBytestreamOpened(String sid_) {
		// TODO Auto-generated method stub
		super.onFtIncomingBytestreamOpened(sid_);
	}

	@Override
	public void onFtOutgoingBytestreamOpened(String sid_) {
		// TODO Auto-generated method stub
		super.onFtOutgoingBytestreamOpened(sid_);
	}

	@Override
	public void onRecvFtBytestreamData(String sid_, BytestreamData data_) {
		// TODO Auto-generated method stub
		super.onRecvFtBytestreamData(sid_, data_);
	}

	@Override
	public void onFtBytestreamClosed(String sid_) {
		// TODO Auto-generated method stub
		super.onFtBytestreamClosed(sid_);
	}

	/******************** 文件传输流程 End ********************/

	/******************** DiscoItem流程 Start ********************/
	// 可能是测试接口
	@Override
	public void onDiscoItem(JID parent_, JID jid_, String node_, String name_) {
		// TODO Auto-generated method stub
		super.onDiscoItem(parent_, jid_, node_, name_);
	}

	/******************** DiscoItem流程 End ********************/

	/******************** Subscription流程 Start ********************/
	// 注意，对于A请求订阅B，B同意后，然后B再去去请求订阅A，此时A应该自动同意，而不再需要弹出界面给用户确认
	@Override
	public void onRecvSubscriptionRequest(JID jid_, String msg_) {
		// TODO Auto-generated method stub
		super.onRecvSubscriptionRequest(jid_, msg_);

		String fromFullJid = jid_.full();
		Log.e("proceduretest", fromFullJid + " onRecvSubscriptionRequest");

		String fromBareJid = jid_.bare();
		Log.e("onRecvSubscriptionRequest", fromFullJid);

		// 对方添加你为好友，执行这条语句，同意订阅后，能在spark上看到你在对方的账户下是好友状态了，对方会去订阅你
		// Note:这个接口回调，实际对应的是我们UI要去弹出显示框，让用户选择，到底是接收订阅请求还是拒绝订阅请求
		// A 要加B为好友，加完以后A会去订阅B,则B会接收到这个回调
		// tmp add it ,later add judge statement

		// 临时处理
		XmppFriend xmppFriend = mXmppData.getFriend(fromBareJid);
		int inviteState = SubscriptionType.S10nNone.swigValue();
		if (xmppFriend != null) {
			inviteState = xmppFriend.getInviteState();
		}

		// 测试发现，需要自动订阅的是 S10nNoneOut 这种情况
		if (inviteState == SubscriptionType.S10nNoneOut.swigValue()) {
			mXmppStack.approveSubscription(jid_);
		} else {
			String toBareJid = XmppOperation.getMyBareJid();
			mXmppCbOperation.writeInvitedToDb(fromFullJid, toBareJid, msg_);

			KunlunApplication.onRevFriendInviteRequest(fromBareJid);
		}

		if (mIMessageSessionActiyPresenterCb != null) {
			mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
		}
		if (mIMainScreenActivityPresenterCb != null) {
			mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
		}
	}

	/******************** Subscription流程 End ********************/

	/******************** Avatar流程 Start ********************/

	@Override
	public void onPublishAvatarSuccess(String id_) {
		// TODO Auto-generated method stub
		super.onPublishAvatarSuccess(id_);
	}

	@Override
	public void onPublishAvatarFailed(String id_) {
		// TODO Auto-generated method stub
		super.onPublishAvatarFailed(id_);
	}

	@Override
	public void onRecvAvatarMetadata(JID from_, AvatarMetadata metadata_) {
		// TODO Auto-generated method stub
		super.onRecvAvatarMetadata(from_, metadata_);

		String bareJid = from_.bare();
		int width = metadata_.width();
		int height = metadata_.height();
		KunlunApplication.onRevAvatarChanged(bareJid);
	}

	// 需要说明的是，自己修改了头像也会回调到这里
	@Override
	public void onRecvAvatarData(JID from_, String id_, String base64_) {
		// TODO Auto-generated method stub
		super.onRecvAvatarData(from_, id_, base64_);
	}

	@Override
	public void onLoadAvatarSuccess(JID from_, String id_, String base64_) {
		// TODO Auto-generated method stub
		super.onLoadAvatarSuccess(from_, id_, base64_);
	}

	@Override
	public void onLoadAvatarFailed(JID from_, String id_) {
		// TODO Auto-generated method stub
		super.onLoadAvatarFailed(from_, id_);
	}

	/******************** Avatar流程 End ********************/

	/******************** 个人信息流程 Start ********************/

	@Override
	public void onNotifyPersonalInfoChangedSuccess(String uid_) {
		// TODO Auto-generated method stub
		super.onNotifyPersonalInfoChangedSuccess(uid_);
	}

	@Override
	public void onNotifyPersonalInfoChangedFailed(String uid_) {
		// TODO Auto-generated method stub
		super.onNotifyPersonalInfoChangedFailed(uid_);
	}

	@Override
	public void onRecvPersonalInfoChangedNotification(JID from_) {
		// TODO Auto-generated method stub
		super.onRecvPersonalInfoChangedNotification(from_);
	}

	/******************** 个人信息流程 End ********************/

	/******************** 消息流程 Start ********************/
	// receipts_ 标识是否重要消息
	@Override
	public void onRecvChatMessage(Message msg_, String xhtml_, boolean receipts_) {
		// TODO Auto-generated method stub
		super.onRecvChatMessage(msg_, xhtml_, receipts_);

		// added by yj
		JID jid = msg_.from();
		String bareJid = jid.bare();
		boolean isInBlacklist = XmppOperation.judgeIsInBlacklist(bareJid);
		if (isInBlacklist) {
			return;
		}

		XmppSingleChatMessage message = mXmppCbOperation.resolveChatMessage(
				msg_, xhtml_, receipts_);

		if (mIMessageChatActiyPresenterCb != null) {
			// 入库操作放到上层处理
			// mXmppCbOperation.writeNormalMsgToDb(message, false);
			mIMessageChatActiyPresenterCb.onRecvChatMessage(message);
		} else {
			mXmppCbOperation.writeNormalMsgToDb(message, true);

			if (mIMessageSessionActiyPresenterCb != null) {
				mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
			}
			if (mIMainScreenActivityPresenterCb != null) {
				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
			}
		}

		// added by yj
		// 后面要加判断处理，并做相应修改
		KunlunApplication.onRevMsgToNotify();
	}

	/*
	 * (non-Javadoc) 收到消息回执
	 */
	@Override
	public void onRecvReceiptMessage(String id_, JID from_, String receiptId_) {
		// TODO Auto-generated method stub

		XmppSingleChatMessage message = mXmppCbOperation.resolveReceiptMessage(
				id_, from_, receiptId_);
		// 更新数据库
		mXmppCbOperation.updateImportMsgInfoType(message);

		if (mIMessageChatActiyPresenterCb != null) {

			mIMessageChatActiyPresenterCb.onRecvReceiptMessage(message);

		}

	}

	/*
	 * 收到即时邮件通知
	 */
	public void onRecvIMailMessage(String id_, JID from_,
			IMailSMTPInfo iMailSMTPInfo_, boolean receipts_) {

		XmppSingleChatMessage message = mXmppCbOperation.resolveIMailMessage(
				id_, from_, iMailSMTPInfo_, receipts_);

		if (mIMessageChatActiyPresenterCb != null) {

			// mXmppCbOperation.writeNormalMsgToDb(message, false);
			mIMessageChatActiyPresenterCb.onRecvChatMessage(message);

		} else {
			mXmppCbOperation.writeNormalMsgToDb(message, true);

			if (mIMessageSessionActiyPresenterCb != null) {
				mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
			}
			if (mIMainScreenActivityPresenterCb != null) {
				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
			}
		}

	}

	/*
	 * 收到振屏消息(双人聊天)
	 */
	public void onRecvAttentionMessage(String id_, JID from_) {

		// 震屏
		mXmppCbOperation.onShakeScreen();

		XmppSingleChatMessage message = mXmppCbOperation
				.resolveChatAttentionMessage(id_, from_);

		if (mIMessageChatActiyPresenterCb != null) {

			// mXmppCbOperation.writeNormalMsgToDb(message, false);
			mIMessageChatActiyPresenterCb.onRecvChatMessage(message);

		} else {
			mXmppCbOperation.writeNormalMsgToDb(message, true);

			if (mIMessageSessionActiyPresenterCb != null) {
				mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
			}
			if (mIMainScreenActivityPresenterCb != null) {
				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
			}
		}

	}

	/*
	 * 收到震屏消息（群聊）
	 */
	public void onRecvMUCRoomAttentionMessage(String id_, JID room_,
			String nickname_) {

		String occupantJid = mXmppCbOperation.getHuddleMemberJidByNickName(
				room_.bare(), nickname_);

		// 过滤自己发送的消息
		if (mXmppCbOperation.getDbMyJid().equals(occupantJid)) {

			return;
		}

		// 解析消息
		HuddleChatMessageInfo msg = mXmppCbOperation
				.resolveHuddleAttentionMessage(id_, occupantJid, room_,
						nickname_);
		// 如果已收到过该消息则屏蔽，否则入库：Tb_Record _Multi_Chat
		boolean bool = mXmppCbOperation.onHandleRevHuddleMsgDb(msg);
		if (!bool) {

			return;
		}

		// 通知消息入库并更新到界面
		if (mIHuddleChatActivityPresenterCb != null) {

			mIHuddleChatActivityPresenterCb.onRecvHuddleMessage(msg);

		} else {

			// 入库：Tb_Record _ChatSum
			int id = mXmppCbOperation.onHandleRevHuddleMsgChatSumDb(msg);
			// 入库：Tb_UI_Notification 界面通知
			mXmppCbOperation.onHandleRevHuddleMsgUINotificationDb(id, msg);

			if (mIMessageSessionActiyPresenterCb != null) {

				mIMessageSessionActiyPresenterCb.onUnReadMsgNotificationCb();
			}

			if (mIMainScreenActivityPresenterCb != null) {

				mIMainScreenActivityPresenterCb.onUnReadMsgNotificationCb();
			}

		}

		// 震屏
		mXmppCbOperation.onShakeScreen();

	}

	/******************** 消息流程 End ********************/

	/******************** NickName流程 Start ********************/
	@Override
	public void onPublishNicknameSuccess(String uid_) {
		// TODO Auto-generated method stub
		super.onPublishNicknameSuccess(uid_);
	}

	// 在对方的 onRecvSubscriptionRequest之后
	@Override
	public void onPublishNicknameFailed(String uid_) {
		// TODO Auto-generated method stub
		super.onPublishNicknameFailed(uid_);
	}

	@Override
	public void onRecvNickname(JID from_, String nickname_) {
		// TODO Auto-generated method stub
		super.onRecvNickname(from_, nickname_);

		String jid = from_.bare();
		XmppFriend xmpFriend = mXmppData.getFriend(jid);
		xmpFriend = mXmppCbOperation.updateFriend(xmpFriend, nickname_);
		mXmppData.addFriend(xmpFriend);

		mXmppCbOperation.writeXmpFriendToDb(xmpFriend);
		if (mIAddsbookActiyPresenterCallback != null) {
			mIAddsbookActiyPresenterCallback.onRecvNickname(xmpFriend);
		}
	}

	/******************** NickName流程 End ********************/

	/******************** 联系人流程 Start ********************/

	// yj:关于Openfire测试，比较奇怪的是，添加好友后，不管对方是否同意，立刻可以看到好友，除非后来对方拒绝，则无法看到好友
	public void onRosterItemAdded(RosterItem item_) {

		super.onRosterItemAdded(item_);

		String jid = item_.jid();
		Log.e("proceduretest", jid + " onRosterItemAdded");

		XmppFriend xmpFriend = mXmppCbOperation.resolveRosterItem(item_);
		mXmppData.addFriend(xmpFriend);

		mXmppCbOperation.writeXmpFriendToDb(xmpFriend);
		if (mIAddsbookActiyPresenterCallback != null) {
			mIAddsbookActiyPresenterCallback.onRosterItemAdded(xmpFriend);
		}

		if (mIHuddleChatDetailedSettingActivityPresenterCb != null) {
			mIHuddleChatDetailedSettingActivityPresenterCb.onRosterItemAdded();
		}

		KunlunApplication.onRevAvatarChanged(jid);
	}

	public void onRosterItemRemoved(RosterItem item_) {
		String jid = item_.jid();
		Log.e("proceduretest", jid + " onRosterItemRemoved");

		XmppFriend xmpFriend = mXmppData.getFriend(jid);
		mXmppData.removeFriend(xmpFriend);

		if (mIAddsbookActiyPresenterCallback != null) {
			// 即使 xmpFriend 为null，也没有关系，应用层已经做了判断处理
			mIAddsbookActiyPresenterCallback.onRosterItemRemoved(xmpFriend);
		}

		mXmppCbOperation.deleteXmpFriendFromDb(xmpFriend);
		if (mIHuddleChatDetailedSettingActivityPresenterCb != null) {
			mIHuddleChatDetailedSettingActivityPresenterCb
					.onRosterItemRemoved();
		}

		// 注意：现在联系人移除后，只会清空数据库的记录，不会清空该账户对应的文件系统目录下的文件夹,后续要处理
	}

	@Override
	public void onRosterItemUpdated(RosterItem item_) {
		// TODO Auto-generated method stub
		super.onRosterItemUpdated(item_);

		String jid = item_.jid();
		String subscriType = item_.subscription().toString();
		Log.e("proceduretest", jid + " onRosterItemUpdated subscriType "
				+ subscriType);
		int subscriptionValue = item_.subscription().swigValue();

		// if (subscriptionValue == SubscriptionType.S10nNone.swigValue()) {
		//
		// }

		XmppFriend xmpFriend = mXmppData.getFriend(jid);
		if (xmpFriend == null) {
			return;
		}

		Log.i("subscriType", subscriType + "  and int value is "
				+ item_.subscription().swigValue());
		// int subscriSwigValue = item_.subscription().swigValue();
		// int s10nToSwigValue = SubscriptionType.S10nTo.swigValue();
		// if (subscriSwigValue == s10nToSwigValue) {
		// JID jid_ = new JID(jid);
		// mXmppStack.approveSubscription(jid_);
		// }

		xmpFriend = mXmppCbOperation.updateFriend(xmpFriend, item_);
		mXmppData.addFriend(xmpFriend);

		mXmppCbOperation.writeXmpFriendToDb(xmpFriend);
		if (mIAddsbookActiyPresenterCallback != null) {
			mIAddsbookActiyPresenterCallback.onRosterItemUpdated(xmpFriend);
		}
	}

	/* 目前对我们来说是没有用的，实际添加好友过程是，先添加以后， */
	/* 意思是我添加联系人成功了，然后底层给的回馈，onRosterItemAdded 返回，然后等待对方确认，如果对方确认了，才会回调到这里 */
	// 奇怪，不明白问什么openfire上测试发现，spark客户端登陆后，立刻会回调到这里,而不是spark接收订阅后
	@Override
	public void onRosterItemSubscribed(RosterItem item_) {
		// TODO Auto-generated method stub
		super.onRosterItemSubscribed(item_);

		String subscription = item_.subscription().toString();
		int subscriptionValue = item_.subscription().swigValue();

		String jid = item_.jid();
		Log.e("proceduretest", jid + " onRosterItemSubscribed");

		Log.i("invitetest", "onRosterItemSubscribed:" + jid);

		XmppFriend xmpFriend = mXmppCbOperation.resolveRosterItem(item_);

		// 这里如果要做分支判定，只有两种状态，S10nTo 和 S10nBoth
		// 实际测试结果是： A（手机）订阅B（PC），A会收到S10nNoneOut； 而B订阅A，最终经过自动订阅后，A会收到
		// S10nFromOut
		if (subscriptionValue == SubscriptionType.S10nNoneOut.swigValue()) {
			String toBareJid = XmppOperation.getMyBareJid();
			String msg = jid;
			msg += mAppContext
					.getString(R.string.has_approved_your_invite_so_to_msg);
			mXmppCbOperation.writeInviteApprovedToDb(jid, toBareJid, msg);
		}
	}

	/* 目前对我们来说是没有用的 */
	/* 意思是我删除联系人删除成功了，然后底层给的回馈，onRosterItemAdded 返回，然后等待对方确认，如果对方确认了，才会回调到这里 */
	// 奇怪，不明白问什么openfire上测试发现，spark客户端拒绝后，不会回调到这里，而是回调到了 onRosterItemRemoved
	@Override
	public void onRosterItemUnsubscribed(RosterItem item_) {
		// TODO Auto-generated method stub
		super.onRosterItemUnsubscribed(item_);

		String subscription = item_.subscription().toString();
		int subscriptionValue = item_.subscription().swigValue();

		String jid = item_.jid();
		Log.e("proceduretest", jid + " onRosterItemUnsubscribed");

		Log.i("invitetest", "onRosterItemUnsubscribed:" + jid);

		XmppFriend xmpFriend = mXmppData.getFriend(jid);
		mXmppData.removeFriend(xmpFriend);

		if (mIAddsbookActiyPresenterCallback != null) {
			// 即使 xmpFriend 为null，也没有关系，应用层已经做了判断处理
			mIAddsbookActiyPresenterCallback.onRosterItemRemoved(xmpFriend);
		}

		mXmppCbOperation.deleteXmpFriendFromDb(xmpFriend);
		// 下面是否要添加群的处理，需要和DGD商量确认，暂不添加
		// 。。。

		// S10nNone
		// 测试发现，回调到这个接口的只可能有 S10nNoneOut 值
		if (subscriptionValue == SubscriptionType.S10nNoneOut.swigValue()) {
			String toBareJid = XmppOperation.getMyBareJid();
			String msg = jid;
			msg += mAppContext.getString(R.string.refuse_your_invite);
			mXmppCbOperation.writeInviteDeniedToDb(jid, toBareJid, msg);
		}
	}

	/* yj:通过这个接口获取到的jid，是a@b的形式，当然这个接口的resource_参数会标明好友的登陆软件信息 */
	@Override
	public void onRecvRosterPresence(RosterItem item_, String resource_,
			PresenceType type_, String msg_) {
		super.onRecvRosterPresence(item_, resource_, type_, msg_);

		String jid = item_.jid();
		XmppFriend xmpFriend = mXmppData.getFriend(jid);
		xmpFriend = mXmppCbOperation.updatePresenExtra(item_, resource_, type_,
				msg_, xmpFriend);
		mXmppData.addFriend(xmpFriend);

		Log.e("zhuangtaiceshi",
				"callback_"
						+ jid
						+ ":"
						+ String.valueOf(xmpFriend.getFriendPresenExtra()
								.getPresenceState()));
		mXmppCbOperation.writeXmpFriendToDb(xmpFriend);
		if (mIAddsbookActiyPresenterCallback != null) {
			mIAddsbookActiyPresenterCallback.onRecvRosterPresence(xmpFriend);
		}

		if (mIMessageSessionActiyPresenterCb != null) {

			int state = xmpFriend.getFriendPresenExtra().getPresenceState();

			Log.e("zhuangtaiceshi", "callback:" + String.valueOf(state));

			mIMessageSessionActiyPresenterCb
					.onUpdataContactPresence(jid, state);

		}

	}

	/* yj:只是刚开始会推送一次，后面不会再推送信息 */
	@Override
	public void onRecvRoster(Roster roster_) {
		// TODO Auto-generated method stub
		super.onRecvRoster(roster_);

		Hashtable<String, XmppFriend> hashtable = mXmppCbOperation
				.resolveRoster(roster_);

		Log.i("sizeinfo", "onRecvRoster pre hashtable : " + hashtable.size());
		mXmppData.addFriend(hashtable);
		Log.i("sizeinfo", "onRecvRoster post hashtable : " + hashtable.size());

		mXmppCbOperation.writeXmpFriendToDb(hashtable);

		// mXmppCbOperation.preProcessHuddleWork();//推送群jid入房间
		mXmppCbOperation.prePreocessLocalHuddle(mXmppCbOperation.getDbMyJid());

		if (mIAddsbookActiyPresenterCallback != null) {
			mIAddsbookActiyPresenterCallback.onRecvRoster(hashtable);
		}

	}

	/******************** 联系人流程 End ********************/

}
