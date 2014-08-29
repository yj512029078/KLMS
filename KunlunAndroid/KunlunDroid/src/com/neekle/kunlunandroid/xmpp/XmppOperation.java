package com.neekle.kunlunandroid.xmpp;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmpp.myWRAP.JID;
import org.xmpp.myWRAP.Message;
import org.xmpp.myWRAP.Message.MessageType;
import org.xmpp.myWRAP.Geoloc;
import org.xmpp.myWRAP.IMailSMTPInfo;
import org.xmpp.myWRAP.Microblog;
import org.xmpp.myWRAP.Roster;
import org.xmpp.myWRAP.RosterItem;
import org.xmpp.myWRAP.StringList;
import org.xmpp.myWRAP.SubscriptionType;
import org.xmpp.myWRAP.Presence.PresenceType;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Vibrator;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.R.string;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.IdGenarater;
import com.neekle.kunlunandroid.common.imgcombiner.ImageCombiner;
import com.neekle.kunlunandroid.data.ContactInfo;
import com.neekle.kunlunandroid.data.ContactStarInfo;
import com.neekle.kunlunandroid.data.HuddleChatMessageInfo;
import com.neekle.kunlunandroid.data.MessageComType;
import com.neekle.kunlunandroid.data.MicroblogInfo;
import com.neekle.kunlunandroid.db.DbConstants;
import com.neekle.kunlunandroid.db.DbConstantsType;
import com.neekle.kunlunandroid.db.TbAccountChatInputFeatureListController;
import com.neekle.kunlunandroid.db.TbAccountUiFeatureListController;
import com.neekle.kunlunandroid.db.TbChatInputFeatureListController;
import com.neekle.kunlunandroid.db.TbFriendsController;
import com.neekle.kunlunandroid.db.TbHuddleInfoController;
import com.neekle.kunlunandroid.db.TbMultiChatRoomInfoController;
import com.neekle.kunlunandroid.db.TbRecordChatSumController;
import com.neekle.kunlunandroid.db.TbRecordMicroblogController;
import com.neekle.kunlunandroid.db.TbRecordMultiChatController;
import com.neekle.kunlunandroid.db.TbRecordSingleChatController;
import com.neekle.kunlunandroid.db.TbUIFeatureListController;
import com.neekle.kunlunandroid.db.TbUINotificationController;
import com.neekle.kunlunandroid.http.HttpDownLoadFileClient;
import com.neekle.kunlunandroid.http.HttpUpLoadData;
import com.neekle.kunlunandroid.screens.HuddleChatActvity;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.screens.MicroBlogLocationActivity;
import com.neekle.kunlunandroid.screens.MicroBlogWritingActivity;
import com.neekle.kunlunandroid.util.ChatMessageUtil;
import com.neekle.kunlunandroid.util.FilePathUtil;
import com.neekle.kunlunandroid.util.TimeOperater;
import com.neekle.kunlunandroid.util.pinyin.PinYinUtil;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppFriendPresenExtra;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;
import com.neekle.kunlunandroid.xmpp.data.XmppSingleChatMessage;

public class XmppOperation {

	private Context mAppContext;

	private String msgType;// 消息类型

	public void setContext(Context context) {
		this.mAppContext = context;
	}

	public static boolean isHuddle(String jid) {
		boolean isHuddle = false;
		if (jid.contains(Constants.HUDDLE_IDENTIFIER)) {
			isHuddle = true;
		}

		return isHuddle;
	}

	public static boolean judgeIsInBlacklist(String bareJid) {
		boolean isInBlacklist = false;

		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriend(bareJid);
		if (xmppFriend != null) {
			isInBlacklist = xmppFriend.isInBlacklist();
		}

		return isInBlacklist;
	}

	public static boolean judgeIfLoginAccountChange(String nowLoginJid) {
		String preLoginXmppJidBareString = null;
		XmppData xmppData = XmppData.getSingleton();
		XmppJid preLoginXmppJid = xmppData.getJid();
		if (preLoginXmppJid != null) {
			preLoginXmppJidBareString = preLoginXmppJid.getBare();
		}

		XmppJid nowLoginXmppJid = new XmppJid(nowLoginJid);
		String nowLoginXmppJidBareString = nowLoginXmppJid.getBare();

		boolean isChanged = false;
		if (preLoginXmppJidBareString == null) {
			isChanged = true;
		} else if (nowLoginXmppJidBareString == null) {
			isChanged = true;
		} else if (!preLoginXmppJidBareString.equals(nowLoginXmppJidBareString)) {
			isChanged = true;
		}

		return isChanged;
	}

	public static void judgeToReadXmppFriendFromDbAndSetData(String loginJid) {
		XmppOperation xmppOperation = new XmppOperation();
		boolean isDataOk = xmppOperation.getIsXmppFriendsDataPrepared();
		if (!isDataOk) {
			ArrayList<XmppFriend> list = xmppOperation
					.getAllXmppFriendsFromDb();
			XmppData xmppData = XmppData.getSingleton();
			xmppData.clearAllFriend();
			xmppData.addFriend(list);
		}
	}

	public static boolean isSameMsg(XmppSingleChatMessage message,
			XmppSingleChatMessage otherMessage) {
		String fromFullJId = message.getFromFullJid();
		String toBareJId = message.getToBareJid();
		int infoType = message.getInfoType();
		String dbFromFullJId = otherMessage.getFromFullJid();
		String dbToBareJId = otherMessage.getToBareJid();
		int dbInfoType = otherMessage.getInfoType();

		if (!fromFullJId.equals(dbFromFullJId)) {
			return false;
		} else if (!toBareJId.equals(dbToBareJId)) {
			return false;
		} else if (!(infoType == dbInfoType)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean getIsLoginedSelf(String judgeJid) {
		boolean isSelf = false;

		XmppJid judgeXmppJid = new XmppJid(judgeJid);
		String judgeBareJid = judgeXmppJid.getBare();

		XmppData xmppData = XmppData.getSingleton();
		XmppJid xmppJid = xmppData.getJid();
		String bareJid = xmppJid.getBare();

		if (judgeBareJid.equals(bareJid)) {
			isSelf = true;
		}

		return isSelf;
	}

	public static String getBareJid(String validJid) {
		XmppJid xmppJid = new XmppJid(validJid);
		String bareJid = xmppJid.getBare();

		return bareJid;
	}

	public static String getShowName(XmppFriend xmpFriend) {
		String showName = null;
		String realName = xmpFriend.getName();
		String remarkName = xmpFriend.getRemarkName();
		String jid = xmpFriend.getFriendJid();

		if ((remarkName != null)
				&& (!remarkName.equals(Constants.EMPTY_STRING))) {
			showName = remarkName;
		} else if ((realName != null)
				&& (!realName.equals(Constants.EMPTY_STRING))) {
			showName = realName;
		} else {
			XmppJid xmpJid = new XmppJid(jid);
			showName = xmpJid.getUserName();
		}

		return showName;
	}

	public static String getShowNamePinyin(XmppFriend xmpFriend) {
		String pinyin = null;
		ArrayList<String> pinyinList = null;
		String realName = xmpFriend.getName();
		String remarkName = xmpFriend.getRemarkName();
		String jid = xmpFriend.getFriendJid();

		if ((remarkName != null)
				&& (!remarkName.equals(Constants.EMPTY_STRING))) {
			pinyinList = xmpFriend.getRemarkNamePinyinList();
		} else if ((realName != null)
				&& (!realName.equals(Constants.EMPTY_STRING))) {
			pinyinList = xmpFriend.getNamePinyinList();
		} else {
			XmppJid xmpJid = new XmppJid(jid);
			String userName = xmpJid.getUserName();

			pinyinList = new ArrayList<String>();
			pinyinList.add(userName);
		}

		if ((pinyinList != null) && (pinyinList.size() != 0)) {
			pinyin = pinyinList.get(0);
		}

		return pinyin;
	}

	public static String getFriendShowName(String bareJid) {
		String showName = null;

		XmppJid xmppJid = new XmppJid(bareJid);
		XmppFriend xmppFriend = getXmppFriend(bareJid);
		if (xmppFriend != null) {
			showName = xmppFriend.getShowName();
		} else {
			// 备注：暂时就如果在朋友列表中没有信息，就直接取username，实际这种情况，就是对应的登陆者自己，我们暂时拿不到相关自己的信息
			showName = xmppJid.getUserName();
		}

		return showName;
	}

	private static String getFriendPhotoPath(String bareJid) {
		String photoPath = null;

		XmppFriend xmppFriend = getXmppFriend(bareJid);
		if (xmppFriend != null) {
			photoPath = xmppFriend.getHeadPortrait();
		}

		return photoPath;
	}

	public static String getPartnerBareJid(String fromBareJid,
			String toBareJid, String sendOrRevTag) {
		String partnerBareJid = null;

		if (sendOrRevTag.equals(Constants.MsgSendOrRcvTag.SEND)) {
			partnerBareJid = toBareJid;
		} else if (sendOrRevTag.equals(Constants.MsgSendOrRcvTag.SENDING)) {
			partnerBareJid = toBareJid;
		} else {
			partnerBareJid = fromBareJid;
		}

		return partnerBareJid;
	}

	public static String getMyBareJid() {
		XmppData xmppData = XmppData.getSingleton();
		XmppJid xmpJid = xmppData.getJid();
		String myJid = xmpJid.getBare();

		return myJid;
	}

	public static String getMyFullJid() {
		XmppData xmppData = XmppData.getSingleton();
		XmppJid myXmppJid = xmppData.getJid();
		String fromFullJid = myXmppJid.getFull();

		return fromFullJid;
	}

	// 获得自己的显示名，这个现在和朋友列表单独分开，现在没有相关数据，后面要改掉的
	public static String getMyShowName(String bareJid) {
		XmppJid xmppJid = new XmppJid(bareJid);
		String showName = xmppJid.getUserName();

		return showName;
	}

	// 获得自己的显示名，这个现在和朋友列表单独分开，现在没有相关数据，后面要改掉的
	private static String getMyPhotoPath(String bareJid) {
		String photoPath = null;
		// ...

		return photoPath;
	}

	public static XmppFriend getXmppFriend(String bareJid) {
		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriend(bareJid);

		return xmppFriend;
	}

	public XmppSingleChatMessage resolveChatMessage(Message msg_,
			String xhtml_, boolean receipts_) {
		XmppSingleChatMessage singleChatMessage = new XmppSingleChatMessage();

		String myBareJid = getMyBareJid();
		JID fromJid = msg_.from();
		String fromFullJid = fromJid.full();
		String fromBareJid = fromJid.bare();
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromName = getFriendShowName(fromBareJid);
		JID toJid = msg_.to();
		String toFullJid = toJid.full();
		String toBareJid = toJid.bare();
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String toName = getMyShowName(toBareJid);
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;

		// 如果对方，没有传过来，则自己生成
		String messageId = msg_.id();
		if ((messageId == null) || (messageId.equals(Constants.EMPTY_STRING))) {
			messageId = ChatMessageUtil.getMsgId();
		}
		MessageType type = msg_.subtype();
		int messageType = type.swigValue();// 此处MessageType 为整型值，区别于群聊
		// 如果对方，没有传过来，则自己生成
		String thread = msg_.thread();
		if ((thread == null) || (thread.equals(Constants.EMPTY_STRING))) {
			thread = IdGenarater.ThreadId.generateThreadId();
		}
		String timeStamp = ChatMessageUtil.getStampTime();
		int infoType = Constants.MsgInfoType.INSTANT;
		if (receipts_) {
			infoType = Constants.MsgInfoType.IMPORTANT;
		}

		singleChatMessage.setMyJid(myBareJid);
		singleChatMessage.setFromFullJid(fromFullJid);
		singleChatMessage.setFromName(fromName);
		singleChatMessage.setToBareJid(toBareJid);
		singleChatMessage.setToName(toName);
		singleChatMessage.setSendOrRcvTag(sendOrRcvTag);
		singleChatMessage.setMessageId(messageId);
		singleChatMessage.setMessageType(messageType);
		singleChatMessage.setThreadId(thread);
		singleChatMessage.setTimeStamp(timeStamp);
		singleChatMessage.setInfoType(infoType);

		if ((xhtml_ != null) && (!xhtml_.equals(Constants.EMPTY_STRING))) {
			singleChatMessage.setBodyType(ChatMessageUtil.MSG_BODY_TYPE_XHTML);

			// 解析xhtml，将多媒体文件预先下载
			String msgBody = this.preParseXhtmlMsg(msg_.body(), xhtml_,
					fromBareJid);// 可能要替换为xhtml_

			String msgText = this.getReceiveMsgType();

			if (infoType == Constants.MsgInfoType.IMPORTANT) {

				msgText = mAppContext
						.getString(R.string.message_session_activity_import_msg);

			}
			singleChatMessage.setMsgBody(msgBody);
			singleChatMessage.setMsgText(msgText);

		} else {

			singleChatMessage.setBodyType(ChatMessageUtil.MSG_BODY_TYPE_TEXT);
			String msgBody = msg_.body();
			String msgText = msgBody;
			if (infoType == Constants.MsgInfoType.IMPORTANT) {

				msgText = mAppContext
						.getString(R.string.message_session_activity_import_msg);

			}
			singleChatMessage.setMsgBody(msgBody);
			singleChatMessage.setMsgText(msgText);
		}

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromPhotoPath = getFriendPhotoPath(fromBareJid);
		singleChatMessage.setFromPhotoPath(fromPhotoPath);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		// 这个实现，是要修改的，现在返回的是null
		String toPhotoPath = getMyPhotoPath(toBareJid);
		singleChatMessage.setToPhotoPath(toPhotoPath);

		String lang = msg_.xmlLang();
		singleChatMessage.setLang(lang);

		String subject = msg_.subject();
		singleChatMessage.setSubject(subject);

		String partnerJid = getPartnerBareJid(fromBareJid, toBareJid,
				sendOrRcvTag);
		singleChatMessage.setPartnerJid(partnerJid);

		return singleChatMessage;
	}

	/**
	 * 地理位置信息解析
	 * 
	 * @param id_
	 * @param from_
	 * @param geoloc_
	 * @return
	 */
	public XmppSingleChatMessage resolveGeolocMessage(String id_, JID from_,
			Geoloc geoloc_/* Message msg_, String xhtml_ */) {
		XmppSingleChatMessage singleChatMessage = new XmppSingleChatMessage();

		String myBareJid = getMyBareJid();
		String fromFullJid = from_.full();
		String fromBareJid = from_.bare();
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromName = getFriendShowName(fromBareJid);

		String toBareJid = myBareJid;
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String toName = getMyShowName(toBareJid);
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;

		// 如果对方，没有传过来，则自己生成
		String messageId = id_;
		if ((messageId == null) || (messageId.equals(Constants.EMPTY_STRING))) {
			messageId = ChatMessageUtil.getMsgId();
		}
		int messageType = com.neekle.kunlunandroid.xmpp.common.XmppConstants.MessageType.Chat;// 此处MessageType
																								// 为整型值，区别于群聊
		// 如果对方，没有传过来，则自己生成
		String thread = null;
		if ((thread == null) || (thread.equals(Constants.EMPTY_STRING))) {
			thread = ChatMessageUtil.getThreadId();
		}
		String timeStamp = ChatMessageUtil.getStampTime();
		int infoType = Constants.MsgInfoType.INSTANT;

		singleChatMessage.setMyJid(myBareJid);
		singleChatMessage.setFromFullJid(fromFullJid);
		singleChatMessage.setFromName(fromName);
		singleChatMessage.setToBareJid(toBareJid);
		singleChatMessage.setToName(toName);
		singleChatMessage.setSendOrRcvTag(sendOrRcvTag);
		singleChatMessage.setMessageId(messageId);
		singleChatMessage.setMessageType(messageType);
		singleChatMessage.setThreadId(thread);
		singleChatMessage.setTimeStamp(timeStamp);
		singleChatMessage.setInfoType(infoType);
		singleChatMessage.setMsgBody(ChatMessageUtil.createGeolocMsg(
				geoloc_.lon(), geoloc_.lat()));
		singleChatMessage.setMsgText(ChatMessageUtil.MSG_TEXT_GEOLOC);
		singleChatMessage.setBodyType(ChatMessageUtil.MSG_BODY_TYPE_GEOLOC);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromPhotoPath = getFriendPhotoPath(fromBareJid);
		singleChatMessage.setFromPhotoPath(fromPhotoPath);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		// 这个实现，是要修改的，现在返回的是null
		String toPhotoPath = getMyPhotoPath(toBareJid);
		singleChatMessage.setToPhotoPath(toPhotoPath);

		String subject = "";
		singleChatMessage.setSubject(subject);

		String partnerJid = getPartnerBareJid(fromBareJid, toBareJid,
				sendOrRcvTag);
		singleChatMessage.setPartnerJid(partnerJid);

		return singleChatMessage;
	}

	/**
	 * 解析回执消息（绝大多数处理没用）
	 * 
	 * @param id_
	 * @param from_
	 * @param geoloc_
	 * @return
	 */
	public XmppSingleChatMessage resolveReceiptMessage(String id_, JID from_,
			String receiptId_) {
		XmppSingleChatMessage singleChatMessage = new XmppSingleChatMessage();

		String myBareJid = getMyBareJid();
		String fromFullJid = from_.full();
		String fromBareJid = from_.bare();
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromName = getFriendShowName(fromBareJid);

		String toBareJid = myBareJid;
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String toName = getMyShowName(toBareJid);
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;

		// 如果对方，没有传过来，则自己生成
		String messageId = receiptId_;// ***
		if ((messageId == null) || (messageId.equals(Constants.EMPTY_STRING))) {
			messageId = ChatMessageUtil.getMsgId();
		}
		int messageType = com.neekle.kunlunandroid.xmpp.common.XmppConstants.MessageType.Chat;
		// 如果对方，没有传过来，则自己生成
		String thread = null;
		if ((thread == null) || (thread.equals(Constants.EMPTY_STRING))) {
			thread = ChatMessageUtil.getThreadId();
		}
		String timeStamp = ChatMessageUtil.getStampTime();
		int infoType = Constants.MsgInfoType.INSTANT;

		singleChatMessage.setMyJid(myBareJid);
		singleChatMessage.setFromFullJid(fromFullJid);
		singleChatMessage.setFromName(fromName);
		singleChatMessage.setToBareJid(toBareJid);
		singleChatMessage.setToName(toName);
		singleChatMessage.setSendOrRcvTag(sendOrRcvTag);
		singleChatMessage.setMessageId(messageId);
		singleChatMessage.setMessageType(messageType);
		singleChatMessage.setThreadId(thread);
		singleChatMessage.setTimeStamp(timeStamp);
		singleChatMessage.setInfoType(infoType);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromPhotoPath = getFriendPhotoPath(fromBareJid);
		singleChatMessage.setFromPhotoPath(fromPhotoPath);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		// 这个实现，是要修改的，现在返回的是null
		String toPhotoPath = getMyPhotoPath(toBareJid);
		singleChatMessage.setToPhotoPath(toPhotoPath);

		String subject = "";
		singleChatMessage.setSubject(subject);

		String partnerJid = getPartnerBareJid(fromBareJid, toBareJid,
				sendOrRcvTag);
		singleChatMessage.setPartnerJid(partnerJid);

		return singleChatMessage;
	}

	/**
	 * 解析即时邮件通知消息
	 * 
	 * @param id_
	 * @param from_
	 * @param receiptId_
	 * @return
	 */
	public XmppSingleChatMessage resolveIMailMessage(String id_, JID from_,
			IMailSMTPInfo iMailSMTPInfo_, boolean receipts_/*
															 * String id_, JID
															 * from_, String
															 * receiptId_
															 */) {

		XmppSingleChatMessage singleChatMessage = new XmppSingleChatMessage();

		String myBareJid = getMyBareJid();
		String fromFullJid = from_.full();
		String fromBareJid = from_.bare();
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromName = getFriendShowName(fromBareJid);

		String toBareJid = myBareJid;
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String toName = getMyShowName(toBareJid);
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;

		// 如果对方，没有传过来，则自己生成
		String messageId = id_;// ***
		if ((messageId == null) || (messageId.equals(Constants.EMPTY_STRING))) {
			messageId = ChatMessageUtil.getMsgId();
		}
		int messageType = com.neekle.kunlunandroid.xmpp.common.XmppConstants.MessageType.Chat;
		;// (可能存在问题，messagetype 而不是msginfotype) 此处MessageType 为整型值，区别于群聊
			// 如果对方，没有传过来，则自己生成
		String thread = null;
		if ((thread == null) || (thread.equals(Constants.EMPTY_STRING))) {

			thread = ChatMessageUtil.getThreadId();
		}
		String timeStamp = ChatMessageUtil.getStampTime();
		int infoType = Constants.MsgInfoType.IMAIL_NOTIFICATION;

		singleChatMessage.setMyJid(myBareJid);
		singleChatMessage.setFromFullJid(fromFullJid);
		singleChatMessage.setFromName(fromName);
		singleChatMessage.setToBareJid(toBareJid);
		singleChatMessage.setToName(toName);
		singleChatMessage.setSendOrRcvTag(sendOrRcvTag);
		singleChatMessage.setMessageId(messageId);
		singleChatMessage.setMessageType(messageType);
		singleChatMessage.setThreadId(thread);
		singleChatMessage.setTimeStamp(timeStamp);
		singleChatMessage.setInfoType(infoType);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromPhotoPath = getFriendPhotoPath(fromBareJid);
		singleChatMessage.setFromPhotoPath(fromPhotoPath);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		// 这个实现，是要修改的，现在返回的是null
		String toPhotoPath = getMyPhotoPath(toBareJid);
		singleChatMessage.setToPhotoPath(toPhotoPath);

		String title = iMailSMTPInfo_.title();// 邮件主题
		int attachmentNum = iMailSMTPInfo_.attachmentNum();// 附件数量
		String msgBody = title + "," + attachmentNum;
		String subject = "";

		singleChatMessage.setSubject(subject);
		singleChatMessage.setMsgBody(msgBody);
		singleChatMessage.setMsgText(ChatMessageUtil.MSG_IMAIL_NOTIFICATION);
		singleChatMessage.setBodyType(ChatMessageUtil.MSG_BODY_TYPE_TEXT);
		String partnerJid = getPartnerBareJid(fromBareJid, toBareJid,
				sendOrRcvTag);
		singleChatMessage.setPartnerJid(partnerJid);

		return singleChatMessage;
	}

	/**
	 * 解析振屏消息
	 * 
	 * @param from_
	 * @return
	 */
	public XmppSingleChatMessage resolveChatAttentionMessage(String id_,
			JID from_) {

		XmppSingleChatMessage singleChatMessage = new XmppSingleChatMessage();

		String myBareJid = getMyBareJid();
		String fromFullJid = from_.full();
		String fromBareJid = from_.bare();
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromName = getFriendShowName(fromBareJid);

		String toBareJid = myBareJid;
		// 关于消息会话显示的name，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String toName = getMyShowName(toBareJid);
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;

		// 如果对方，没有传过来，则自己生成
		String messageId = id_;
		if ((messageId == null) || (messageId.equals(Constants.EMPTY_STRING))) {
			messageId = ChatMessageUtil.getMsgId();
		}
		int messageType = com.neekle.kunlunandroid.xmpp.common.XmppConstants.MessageType.Chat;
		;// (可能存在问题，messagetype 而不是msginfotype) 此处MessageType 为整型值，区别于群聊
			// 如果对方，没有传过来，则自己生成
		String thread = null;
		if ((thread == null) || (thread.equals(Constants.EMPTY_STRING))) {

			thread = ChatMessageUtil.getThreadId();
		}
		String timeStamp = ChatMessageUtil.getStampTime();
		int infoType = Constants.MsgInfoType.ATTENTION;

		singleChatMessage.setMyJid(myBareJid);
		singleChatMessage.setFromFullJid(fromFullJid);
		singleChatMessage.setFromName(fromName);
		singleChatMessage.setToBareJid(toBareJid);
		singleChatMessage.setToName(toName);
		singleChatMessage.setSendOrRcvTag(sendOrRcvTag);
		singleChatMessage.setMessageId(messageId);
		singleChatMessage.setMessageType(messageType);
		singleChatMessage.setThreadId(thread);
		singleChatMessage.setTimeStamp(timeStamp);
		singleChatMessage.setInfoType(infoType);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		String fromPhotoPath = getFriendPhotoPath(fromBareJid);
		singleChatMessage.setFromPhotoPath(fromPhotoPath);

		// 关于消息会话显示的photo，需要说明的是，我们暂时不做动态更新，只在每次进入消息会话之前取一次
		// 这个实现，是要修改的，现在返回的是null
		String toPhotoPath = getMyPhotoPath(toBareJid);
		singleChatMessage.setToPhotoPath(toPhotoPath);

		String msgBody = mAppContext
				.getString(R.string.cellonemessage_rev_attention_msg);
		String subject = "";

		singleChatMessage.setSubject(subject);
		singleChatMessage.setMsgBody(msgBody);
		singleChatMessage.setMsgText(mAppContext
				.getString(R.string.cellonemessage_rev_attention_msg));
		singleChatMessage.setBodyType(ChatMessageUtil.MSG_BODY_TYPE_TEXT);
		String partnerJid = getPartnerBareJid(fromBareJid, toBareJid,
				sendOrRcvTag);
		singleChatMessage.setPartnerJid(partnerJid);

		return singleChatMessage;
	}

	public void updateImportMsgInfoType(XmppSingleChatMessage data) {

		TbRecordSingleChatController controller = new TbRecordSingleChatController(
				mAppContext);
		controller.open();
		controller.updateSendImportMsgInfoType(this.getMyBareJid(),
				data.getFromFullJid(), data.getMessageId(),
				Constants.MsgInfoType.IMPORTANT_RECEIPT);

		controller.close();
	}

	public Hashtable<String, XmppFriend> resolveRoster(Roster roster_) {
		StringList stringList = roster_.keys();
		long size = stringList.size();

		Hashtable<String, XmppFriend> hashtable = null;
		if (size != 0) {
			hashtable = new Hashtable<String, XmppFriend>();
		}

		for (int i = 0; i < size; i++) {
			String key = stringList.get(i);
			RosterItem rosterItem = roster_.get(key);
			String jid = rosterItem.jid();
			XmppFriend xmpFriend = resolveRosterItem(rosterItem);

			hashtable.put(jid, xmpFriend);
		}

		// readDbAndSetStartSign(hashtable);

		return hashtable;
	}

	public XmppFriend resolveRosterItem(RosterItem rosterItem) {
		XmppFriend xmpFriend = getProperXmppFriend(rosterItem);
		setFriendInfo(rosterItem, xmpFriend);
		resolveHanziToPinyin(xmpFriend);

		return xmpFriend;
	}

	private XmppFriend getProperXmppFriend(RosterItem rosterItem) {
		String jid = rosterItem.jid();
		String bareJid = getBareJid(jid);

		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmpFriend = xmppData.getFriend(bareJid);
		if (xmpFriend == null) {
			xmpFriend = new XmppFriend();
		}

		return xmpFriend;
	}

	public boolean getIsXmppFriendsDataPrepared() {
		boolean isDataOk = false;

		XmppData xmppData = XmppData.getSingleton();
		Hashtable<String, XmppFriend> hashtable = xmppData.getAllFriends();
		if ((hashtable != null) && (hashtable.size() != 0)) {
			isDataOk = true;
		}

		return isDataOk;
	}

	public ArrayList<XmppFriend> getAllXmppFriendsFromDb() {
		XmppData xmppData = XmppData.getSingleton();
		XmppJid xmppJid = xmppData.getJid();
		String myJid = xmppJid.getBare();
		ArrayList<XmppFriend> list = new ArrayList<XmppFriend>();
		ArrayList<XmppFriend> normalFriendList = doReadFromFriends(myJid);
		if (normalFriendList != null) {
			list.addAll(normalFriendList);
		}

		return list;
	}

	public void writeInvitingToDb(String fromFullJid, String toBareJid,
			String msg) {
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.SEND;
		int infoType = Constants.MsgInfoType.INVITING;

		XmppSingleChatMessage message = constructNonInstantAsMessage(
				fromFullJid, toBareJid, msg, sendOrRcvTag, infoType);
		writeInviteMsgToDb(message, false);
	}

	public void writeInvitedToDb(String fromFullJid, String toBareJid,
			String msg) {
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;
		int infoType = Constants.MsgInfoType.INVITING;

		XmppSingleChatMessage message = constructNonInstantAsMessage(
				fromFullJid, toBareJid, msg, sendOrRcvTag, infoType);
		writeInviteMsgToDb(message, true);
	}

	public void writeInviteApprovedToDb(String fromFullJid, String toBareJid,
			String msg) {
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;
		int infoType = Constants.MsgInfoType.REV_INVITE;

		XmppSingleChatMessage message = constructNonInstantAsMessage(
				fromFullJid, toBareJid, msg, sendOrRcvTag, infoType);
		writeInviteMsgToDb(message, true);
	}

	public void writeInviteDeniedToDb(String fromFullJid, String toBareJid,
			String msg) {
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.REV;
		int infoType = Constants.MsgInfoType.DENY_INVITE;

		XmppSingleChatMessage message = constructNonInstantAsMessage(
				fromFullJid, toBareJid, msg, sendOrRcvTag, infoType);
		writeInviteMsgToDb(message, true);
	}

	public void writeApproveInviteToDb(String fromFullJid, String toBareJid,
			String msg) {
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.SEND;
		int infoType = Constants.MsgInfoType.REV_INVITE;

		XmppSingleChatMessage message = constructNonInstantAsMessage(
				fromFullJid, toBareJid, msg, sendOrRcvTag, infoType);
		writeInviteMsgToDb(message, false);
	}

	public void writeDenyInviteToDb(String fromFullJid, String toBareJid,
			String msg) {
		String sendOrRcvTag = Constants.MsgSendOrRcvTag.SEND;
		int infoType = Constants.MsgInfoType.DENY_INVITE;

		XmppSingleChatMessage message = constructNonInstantAsMessage(
				fromFullJid, toBareJid, msg, sendOrRcvTag, infoType);
		writeInviteMsgToDb(message, false);
	}

	private XmppSingleChatMessage constructNonInstantAsMessage(
			String fromFullJid, String toBareJid, String msg,
			String sendOrRcvTag, int infoType) {
		XmppSingleChatMessage singleChatMessage = new XmppSingleChatMessage();

		String myBareJid = getMyBareJid();
		singleChatMessage.setMyJid(myBareJid);

		singleChatMessage.setFromFullJid(fromFullJid);

		XmppJid xmppJid = new XmppJid(fromFullJid);
		String fromBareJid = xmppJid.getBare();
		String fromName = getFriendShowName(fromBareJid);
		singleChatMessage.setFromName(fromName);

		singleChatMessage.setToBareJid(toBareJid);
		String toName = null;
		if (myBareJid.equals(toBareJid)) {
			toName = getMyShowName(toBareJid);
		} else {
			toName = getFriendShowName(toBareJid);
		}
		singleChatMessage.setToName(toName);

		singleChatMessage.setSendOrRcvTag(sendOrRcvTag);

		String messageId = IdGenarater.MsgId.generateMsgId();
		singleChatMessage.setMessageId(messageId);

		// 暂时先设置为Normal，目前看来好像没什么用，后面确认下
		int messageType = MessageType.Normal.swigValue();
		singleChatMessage.setMessageType(messageType);

		String thread = IdGenarater.ThreadId.generateThreadId();
		singleChatMessage.setThreadId(thread);

		String timeStamp = TimeOperater.getCurrentTime();
		singleChatMessage.setTimeStamp(timeStamp);

		singleChatMessage.setBodyType(Constants.MsgBodyType.TEXT);
		singleChatMessage.setMsgBody(msg);
		// new add
		singleChatMessage.setMsgText(msg);
		singleChatMessage.setInfoType(infoType);

		String partnerJid = getPartnerBareJid(fromBareJid, toBareJid,
				sendOrRcvTag);
		singleChatMessage.setPartnerJid(partnerJid);

		return singleChatMessage;
	}

	private void readDbAndSetStartSign(Hashtable<String, XmppFriend> hashtable) {
		String myJid = getMyBareJid();
		ArrayList<ContactStarInfo> list = doReadStarInfoFromDb(myJid);
		if (list != null) {
			setStarSign(list, hashtable);
		}
	}

	private void setStarSign(ArrayList<ContactStarInfo> list,
			Hashtable<String, XmppFriend> hashtable) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ContactStarInfo data = list.get(i);

			String friendJidDb = data.getFriendJid();
			boolean isStarDb = data.isStarSign();

			XmppFriend xmpFriend = hashtable.get(friendJidDb);
			if (xmpFriend != null) {
				String friendJid = xmpFriend.getFriendJid();
				if (friendJid.equals(friendJidDb.trim())) {
					xmpFriend.setStarSign(isStarDb);
				}
			}
		}

	}

	public XmppFriend updateFriend(XmppFriend xmpFriend, RosterItem rosterItem) {
		setFriendInfo(rosterItem, xmpFriend);
		resolveHanziToPinyin(xmpFriend);

		return xmpFriend;
	}

	public XmppFriend updateFriend(XmppFriend xmpFriend, String nickName) {
		xmpFriend.setName(nickName);
		resolveHanziToPinyin(xmpFriend);

		return xmpFriend;
	}

	public void updateXmpFriendAndWriteToDb(String friendJid, boolean isStar) {
		XmppData xmppData = XmppData.getSingleton();
		Hashtable<String, XmppFriend> hashtable = xmppData.getAllFriends();

		// 基于Hashtable本身是thread-safe的，所以觉得下面多线程环境下代码没有什么问题
		XmppFriend xmpFriend = hashtable.get(friendJid);
		// 当然这里属于对XmpFriend对象本身的操作，如果需要同步，我们可以同步，但是感觉这个子线程只会在初始构造的时候改变一次，所以没有关系，实际上也没有thread-safe的问题
		xmpFriend.setStarSign(isStar);
		doUpdateFriendToDb(friendJid, isStar);
	}

	private void resolveHanziToPinyin(XmppFriend xmpFriend) {
		String nickName = xmpFriend.getName();
		String remarkName = xmpFriend.getRemarkName();

		String nickNamePinyin = null;
		if (nickName != null) {
			nickNamePinyin = PinYinUtil.getPinYin(nickName);
		}
		String remarkNamePinyin = null;
		if (remarkName != null) {
			remarkNamePinyin = PinYinUtil.getPinYin(remarkName);
		}

		ArrayList<String> nickNamePinyinList = new ArrayList<String>();
		nickNamePinyinList.add(nickNamePinyin);
		ArrayList<String> remarkNamePinyinList = new ArrayList<String>();
		remarkNamePinyinList.add(remarkNamePinyin);

		xmpFriend.setNamePinyinList(nickNamePinyinList);
		xmpFriend.setRemarkNamePinyinList(remarkNamePinyinList);

		// 这里showName和showNamePinyin是匹配的
		String showName = getShowName(xmpFriend);
		String showNamePinyin = getShowNamePinyin(xmpFriend);
		xmpFriend.setShowName(showName);
		xmpFriend.setShowNamePinyin(showNamePinyin);
	}

	private void setFriendInfo(RosterItem rosterItem, XmppFriend xmpFriend) {
		String jid = rosterItem.jid();
		String remarkName = rosterItem.name();
		boolean isOnline = rosterItem.online();

		ArrayList<String> groupList = null;
		StringList groupStringList = rosterItem.groups();
		long groupSize = groupStringList.size();
		if (groupSize != 0) {
			groupList = new ArrayList<String>();
		}
		for (int j = 0; j < groupSize; j++) {
			String string = groupStringList.get(j);
			groupList.add(string);
		}

		xmpFriend.setFriendJid(jid);
		// 注意：因为我们实际应用层有规则，对于remarkName为null的情况，需要如何处理，所以此处并不错规则映射
		xmpFriend.setRemarkName(remarkName);
		xmpFriend.setOnline(isOnline);
		xmpFriend.setGroupList(groupList);

		SubscriptionType subscriptionType = rosterItem.subscription();
		int typeSwigValue = subscriptionType.swigValue();
		xmpFriend.setInviteState(typeSwigValue);

		String myJid = getMyBareJid();
		xmpFriend.setMyJid(myJid);

		XmppFriendPresenExtra friendPresenExtra = xmpFriend
				.getFriendPresenExtra();
		if (friendPresenExtra == null) {
			int unavailable = PresenceType.Unavailable.swigValue();
			friendPresenExtra = new XmppFriendPresenExtra();
			friendPresenExtra.setJid(jid);
			friendPresenExtra.setPresenceState(unavailable);
			xmpFriend.setFriendPresenExtra(friendPresenExtra);
		}

		// temp add,后面要移除掉，群部分的头像不在我们这边处理
		//注意：群头像部分后面应该由杜广东处理，所以后面如下处理逻辑一定要去掉
		boolean isHuddle = XmppOperation.isHuddle(jid);
		if (isHuddle) {
			ImageCombiner combiner = new ImageCombiner();
			String[] pathStrings = { null, null, null };
			Bitmap bitmap = combiner.getCombinedImg(pathStrings);
			String path = combiner.getSavedBitmapAsJpeg(jid, bitmap);
			xmpFriend.setHeadPortrait(path);
		}
	}

	public XmppFriend updatePresenExtra(RosterItem item_, String resource_,
			PresenceType type_, String msg_, XmppFriend xmpFriend) {
		String jid = item_.jid();
		int presenceState = type_.swigValue();
		XmppFriendPresenExtra presenExtra = new XmppFriendPresenExtra();
		presenExtra.setJid(jid);
		presenExtra.setPresenceState(presenceState);
		presenExtra.setPresenceMsg(msg_);
		presenExtra.setPresenceResource(resource_);

		xmpFriend.setFriendPresenExtra(presenExtra);

		return xmpFriend;
	}

	public void writeNormalMsgToDb(XmppSingleChatMessage message,
			boolean isUnRead) {
		String dbTime = ChatMessageUtil.getDbTime();
		message.setDbTime(dbTime);

		doWriteNormalMsgToDb(message, isUnRead);
	}

	public void writeInviteMsgToDb(XmppSingleChatMessage message,
			boolean isUnRead) {
		String dbTime = ChatMessageUtil.getDbTime();
		message.setDbTime(dbTime);

		doWriteInviteMsgToDb(message, isUnRead);
	}

	public void writeXmpFriendToDb(Hashtable<String, XmppFriend> hashtable) {
		Set<Entry<String, XmppFriend>> set = hashtable.entrySet();
		Iterator<Entry<String, XmppFriend>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Entry<String, XmppFriend> entry = iterator.next();
			XmppFriend data = entry.getValue();
			writeXmpFriendToDb(data);
		}
	}

	public void writeXmpFriendToDb(XmppFriend data) {
		String jid = data.getFriendJid();
		boolean isHuddle = isHuddle(jid);
		if (isHuddle) {
			doWriteHuddleToDb(data);
		} else {
			doWriteFriendToDb(data);
		}
	}

	public void deleteXmpFriendFromDb(XmppFriend data) {
		String jid = data.getFriendJid();

		// Note: 关于Tb_HuddleInfo 表的处理，后面和dgd商量以后再说
		// boolean isHuddle = isHuddle(jid);
		// if (isHuddle) {
		// writeHuddleToDb(data);
		// }

		// Note: yj
		// 暂时将所有联系人（包括组）都操作在Tb_Friends表中
		doDeleteFriendFromDb(data);
	}

	private void doWriteNormalMsgToDb(XmppSingleChatMessage message,
			boolean isUnRead) {
		doWriteToSingleChat(message);
		int infoTypes[] = new int[] { Constants.MsgInfoType.INSTANT };

		// add dgd
		if (message.getInfoType() == Constants.MsgInfoType.IMPORTANT
				|| message.getInfoType() == Constants.MsgInfoType.INSTANT
				|| message.getInfoType() == Constants.MsgInfoType.IMAIL_NOTIFICATION
				|| message.getInfoType() == Constants.MsgInfoType.ATTENTION) {

			infoTypes = new int[] { Constants.MsgInfoType.INSTANT,
					Constants.MsgInfoType.IMPORTANT,
					Constants.MsgInfoType.IMAIL_NOTIFICATION,
					Constants.MsgInfoType.ATTENTION };
		}

		long rowId = doGetRowIdFromChatSum(message, infoTypes);
		if (rowId == DbConstants.DB_DEF_ROW_ID) {
			rowId = doWriteToChatSum(message);
		} else {
			// 因为这张表总是应该记录最新一条消息的内容，用于显示在列表上，所以我们需要更新
			doUpdateChatSum(message, rowId);
		}

		if (isUnRead) {
			if (rowId != DbConstants.DB_DEF_ROW_ID) {
				String notifyType = DbConstantsType.NOTIFICATION_TYPE_UNREAD_HUDDLE_MSGS;
				doWriteToUiNotification(message, notifyType, rowId);
			}
		}
	}

	private void doWriteInviteMsgToDb(XmppSingleChatMessage message,
			boolean isUnRead) {
		int[] infoTypes = new int[] { Constants.MsgInfoType.INVITING,
				Constants.MsgInfoType.REV_INVITE,
				Constants.MsgInfoType.DENY_INVITE };

		ArrayList<XmppSingleChatMessage> list = getDesiredSingleChatMsgList(
				message, infoTypes);
		int size = list.size();
		int i = 0;
		for (; i < size; i++) {
			XmppSingleChatMessage dbMessage = list.get(i);
			boolean isSame = isSameMsg(message, dbMessage);

			if (isSame) {
				break;
			}
		}

		if (i == size) { // 数据库没有这条消息（A-B）
			doWriteToSingleChat(message);
		} else if (i != (size - 1)) { // 不是最后一条，则要将其放到最后一条
			doDeleteFromSingleChat(message);
			doWriteToSingleChat(message);
		}

		boolean isShoudWriteToUi = false;
		long rowId = doGetRowIdFromChatSum(message, infoTypes);
		if (rowId == DbConstants.DB_DEF_ROW_ID) {
			rowId = doWriteToChatSum(message);
			isShoudWriteToUi = true;
		} else {
			boolean isSame = judgeIfInfoTypeSameWithDb(message, rowId);
			if (!isSame) {
				doUpdateChatSum(message, rowId);
				isShoudWriteToUi = true;
			}
		}

		if (!isUnRead) {
			return;
		}

		if (rowId == DbConstants.DB_DEF_ROW_ID) {
			return;
		}

		String notifyType = DbConstantsType.NOTIFICATION_TYPE_UNREAD_HUDDLE_MSGS;
		// String myJid = message.getMyJid();
		// isExist = doQueryExistInUiNotification(myJid, notifyType, rowId);
		//
		// if (!isExist) {
		// isShoudWriteToUi = true;
		// } else {
		// boolean isSame = judgeIfInfoTypeSameWithDb(message, rowId);
		// if (!isSame) {
		// isShoudWriteToUi = true;
		// }
		// }

		if (isShoudWriteToUi) {
			doWriteToUiNotification(message, notifyType, rowId);
		}
	}

	private ArrayList<XmppSingleChatMessage> getDesiredSingleChatMsgList(
			XmppSingleChatMessage message, int[] infoTypes) {
		String myJid = message.getMyJid();
		String partnerJid = message.getPartnerJid();
		ArrayList<XmppSingleChatMessage> list = readFromSingleChat(myJid,
				partnerJid, infoTypes);

		return list;
	}

	private boolean judgeIfInfoTypeSameWithDb(XmppSingleChatMessage message,
			long rowId) {
		boolean isSame = false;

		int dbInfoType = doGetInfoTypeInChatSum(rowId);
		int msgInfoType = message.getInfoType();
		if (dbInfoType == msgInfoType) {
			isSame = true;
		}

		return isSame;
	}

	private ArrayList<XmppFriend> doReadFromFriends(String myJid) {
		ArrayList<XmppFriend> list = null;

		Context context = KunlunApplication.getContext();
		TbFriendsController controller = new TbFriendsController(context);

		try {
			controller.open();
			list = controller.getFriendList(myJid);
		} catch (Exception e) {

		} finally {
			controller.close();
		}

		return list;
	}

	private ArrayList<XmppSingleChatMessage> readFromSingleChat(String myJid,
			String partnerJid, int[] infoTypes) {
		ArrayList<XmppSingleChatMessage> list = null;

		Context context = KunlunApplication.getContext();
		TbRecordSingleChatController controller = new TbRecordSingleChatController(
				context);

		try {
			controller.open();
			list = controller.getMessageList(myJid, partnerJid, infoTypes);
		} catch (Exception e) {

		} finally {
			controller.close();
		}

		return list;
	}

	private void doWriteToSingleChat(XmppSingleChatMessage message) {
		Context context = KunlunApplication.getContext();
		TbRecordSingleChatController controller = new TbRecordSingleChatController(
				context);

		try {
			controller.open();
			controller.insert(message);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	private void doDeleteFromSingleChat(XmppSingleChatMessage message) {
		Context context = KunlunApplication.getContext();
		TbRecordSingleChatController controller = new TbRecordSingleChatController(
				context);

		try {
			controller.open();
			String myJid = message.getMyJid();
			String fromFullJid = message.getFromFullJid();
			String toJid = message.getToBareJid();
			int infoType = message.getInfoType();
			controller.delete(myJid, fromFullJid, toJid, infoType);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	private long doQueryRowIdInSingleChat(XmppSingleChatMessage message) {
		long rowId = DbConstants.DB_DEF_ROW_ID;

		Context context = KunlunApplication.getContext();
		TbRecordSingleChatController controller = new TbRecordSingleChatController(
				context);
		try {
			controller.open();
			String myJid = message.getMyJid();
			int infoType = message.getInfoType();
			String fromFullJid = message.getFromFullJid();
			String toBareJid = message.getToBareJid();

			rowId = controller.queryRecordRowId(myJid, fromFullJid, toBareJid,
					infoType);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return rowId;
	}

	private long doGetRowIdFromChatSum(XmppSingleChatMessage message,
			int[] infoTypes) {
		String myJid = message.getMyJid();
		String fromFullJid = message.getFromFullJid();
		XmppJid xmppJid = new XmppJid(fromFullJid);
		String fromBareJid = xmppJid.getBare();
		String toBareJid = message.getToBareJid();
		String sendOrRevTag = message.getSendOrRcvTag();
		String showJid = getPartnerBareJid(fromBareJid, toBareJid, sendOrRevTag);
		// 这里直接查到infoType，然后根据此信息去过滤数据库，后面是不是要对inType进行分类，这个后续要考虑，暂时没有影响
		int infoType = message.getInfoType();

		Context context = KunlunApplication.getContext();
		TbRecordChatSumController controller = new TbRecordChatSumController(
				context);

		long rowId = DbConstants.DB_DEF_ROW_ID;
		try {
			controller.open();
			rowId = controller.queryIdForRecord(myJid, showJid, infoTypes);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return rowId;
	}

	private long doWriteToChatSum(XmppSingleChatMessage message) {
		String myJid = message.getMyJid();
		String isSingleChat = Constants.YesOrNo.YES;
		String timeStamp = message.getTimeStamp();
		String geoloc = message.getGeoloc();
		String msgText = message.getMsgText();
		String partMessage = getPartMsg(msgText);

		String showJid = message.getPartnerJid();
		XmppJid xmppJid = new XmppJid(showJid);
		String showBareJid = xmppJid.getBare();
		String showName = getFriendShowName(showBareJid);
		int infoType = message.getInfoType();

		Context context = KunlunApplication.getContext();
		TbRecordChatSumController controller = new TbRecordChatSumController(
				context);

		String insertDbTime = "0";
		long rowId = DbConstants.DB_DEF_ROW_ID;
		try {
			controller.open();
			rowId = controller.insert(myJid, showJid, showName, isSingleChat,
					insertDbTime, timeStamp, geoloc, partMessage, infoType);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return rowId;
	}

	private void doUpdateChatSum(XmppSingleChatMessage message, long id) {
		String timeStamp = message.getTimeStamp();
		String geoloc = message.getGeoloc();
		String msgText = message.getMsgText();
		String partMessage = getPartMsg(msgText);
		int infoType = message.getInfoType();

		Context context = KunlunApplication.getContext();
		TbRecordChatSumController controller = new TbRecordChatSumController(
				context);

		// 暂时先按这个来，后面DGD确定后，改掉，只是这样写不太好，逻辑木有问题
		String insertDbTime = "0";
		try {
			controller.open();
			controller.updateRecord(id, insertDbTime, timeStamp, geoloc,
					partMessage, infoType);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	private int doGetInfoTypeInChatSum(long recordId) {
		Context context = KunlunApplication.getContext();
		TbRecordChatSumController controller = new TbRecordChatSumController(
				context);

		int infoType = Constants.MsgInfoType.INSTANT;
		try {
			controller.open();
			infoType = controller.queryInfoType(recordId);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return infoType;
	}

	private boolean doQueryExistInUiNotification(String myJid,
			String notifyType, long notifyValue) {
		boolean flag = false;
		Context context = KunlunApplication.getContext();
		TbUINotificationController controller = new TbUINotificationController(
				context);

		try {
			controller.open();
			String notifyValueString = notifyValue + Constants.EMPTY_STRING;
			int count = controller.getCount(myJid, notifyType,
					notifyValueString);
			if (count != 0) {
				flag = true;
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return flag;
	}

	private void doWriteToUiNotification(XmppSingleChatMessage message,
			String notifyType, long notifyValue) {
		Context context = KunlunApplication.getContext();
		TbUINotificationController controller = new TbUINotificationController(
				context);

		try {
			controller.open();
			String myJid = message.getMyJid();
			String notifyValueString = notifyValue + Constants.EMPTY_STRING;
			String time = message.getTimeStamp();
			controller.insert(myJid, notifyType, notifyValueString, time);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	// private void doWriteToUiNotification(XmppSingleChatMessage message,
	// long id, boolean isAllowDuplicate, String notificationType) {
	// Context context = KunlunApplication.getContext();
	// TbUINotificationController controller = new TbUINotificationController(
	// context);
	//
	// try {
	// controller.open();
	// boolean isShouldInsert = isShouldInsert(controller, message, id,
	// isAllowDuplicate, notificationType);
	// if (isShouldInsert) {
	// String myJid = message.getMyJid();
	// // 暂时先用这个，本质没什么问题，命名的问题，DGD后面会改掉
	// String type = DbConstantsType.NOTIFICATION_TYPE_UNREAD_HUDDLE_MSGS;
	// String value = id + Constants.EMPTY_STRING;
	// String time = message.getTimeStamp();
	// controller.insert(myJid, type, value, time);
	// }
	// } catch (SQLiteException e) {
	// e.printStackTrace();
	// } finally {
	// controller.close();
	// }
	// }

	// private boolean isShouldInsert(TbUINotificationController controller,
	// XmppSingleChatMessage message, long id, boolean isAllowDuplicate,
	// String notificationType) {
	// if (isAllowDuplicate) {
	// return true;
	// }
	//
	// String myJid = message.getMyJid();
	// String notificationValue = id + Constants.EMPTY_STRING;
	// int count = controller.getCount(myJid, notificationType,
	// notificationValue);
	// if (count == 0) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	private String getPartMsg(String msg) {
		String partMsg = null;

		int length = msg.length();
		if (length <= Constants.PART_MSG_MAX_LENGTH) {
			partMsg = msg.substring(0);
		} else {
			partMsg = msg.substring(0, Constants.PART_MSG_MAX_LENGTH);
		}

		return partMsg;
	}

	private ArrayList<ContactStarInfo> doReadStarInfoFromDb(String myJid) {
		Context context = KunlunApplication.getContext();
		TbFriendsController controller = new TbFriendsController(context);

		ArrayList<ContactStarInfo> list = null;
		try {
			controller.open();
			list = controller.getContactStarList(myJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return list;
	}

	private void doWriteFriendToDb(XmppFriend xmpFriend) {
		Context context = KunlunApplication.getContext();
		TbFriendsController controller = new TbFriendsController(context);

		try {
			controller.open();
			controller.insertOrNeededUpdate(xmpFriend);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	private void doWriteHuddleToDb(XmppFriend xmpFriend) {
		Context context = KunlunApplication.getContext();
		TbHuddleInfoController controller = new TbHuddleInfoController(context);

		try {
			controller.open();

			String myJid = xmpFriend.getMyJid();
			String roomJid = xmpFriend.getFriendJid();
			String huddleTitle = xmpFriend.getName();
			String remarkName = xmpFriend.getRemarkName();

			boolean isExist = controller.isExistTheRecord(myJid, roomJid);
			if (!isExist) {
				controller.insert(myJid, roomJid, huddleTitle, "n", null, "n",
						"n", "n", false);
			}
		} catch (SQLiteException e) {

		} finally {
			controller.close();
		}
	}

	private void doUpdateFriendToDb(String friendJid, boolean isStar) {
		Context context = KunlunApplication.getContext();
		TbFriendsController controller = new TbFriendsController(context);

		String myJid = getMyBareJid();

		try {
			controller.open();
			controller.updateStarSign(myJid, friendJid, isStar);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	private void doDeleteFriendFromDb(XmppFriend xmpFriend) {
		Context context = KunlunApplication.getContext();
		TbFriendsController controller = new TbFriendsController(context);

		String myJid = xmpFriend.getMyJid();
		String friendJid = xmpFriend.getFriendJid();

		try {
			controller.open();
			controller.delete(myJid, friendJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	/**
	 * 登陆后群预处理(登陆后推送的群列表入库)
	 * 
	 * */
	public void preProcessHuddleWork() {

		Hashtable<String, XmppFriend> huddleList = XmppData.getSingleton()
				.getHuddles();

		Iterator<String> iterator = huddleList.keySet().iterator();

		while (iterator.hasNext()) {

			String jid = iterator.next();

			XmppJid room_ = new XmppJid(jid);

			XmppService.getSingleton().enterHuddle(room_);
		}

	}

	public void prePreocessLocalHuddle(String myJid) {

		TbHuddleInfoController controller = new TbHuddleInfoController(
				mAppContext);
		controller.open();
		Cursor cursor = controller.queryAllHuddleInfo(myJid);
		while (cursor.moveToNext()) {

			String roomJid = cursor.getString(cursor
					.getColumnIndex(DbConstants.TB_HUDDLE_SETTING_ROOM_JID));
			XmppJid room_ = new XmppJid(roomJid);

			XmppService xmppService = XmppService.getSingleton();

			// 1、重新进房间
			xmppService.enterHuddle(room_);
			// 2、修改成之前的昵称
			String nickName = this.getSelfMucRoomNickName(myJid, roomJid);
			xmppService.changeSelfNickname(room_, nickName);
			// 3、 查询房间信息，获取群成员列表
			XmppService.getSingleton().queryHuddleInfo(room_);
			XmppService.getSingleton().queryHuddleOwnerList(room_);

		}
		cursor.close();
		controller.close();
	}

	/**
	 * 根据登录者的jid查找置顶房间中的昵称
	 * 
	 * */
	public String getSelfMucRoomNickName(String myJid, String roomId) {

		TbMultiChatRoomInfoController tbMultiChatRoomInfoController = new TbMultiChatRoomInfoController(
				mAppContext);
		tbMultiChatRoomInfoController.open();
		String mucRoomNickName = tbMultiChatRoomInfoController.getOccupantName(
				myJid, roomId, myJid);

		tbMultiChatRoomInfoController.close();

		return mucRoomNickName;
	}

	/**
	 * 群入库
	 * 
	 * */
	public void insertHuddleInfoToDb(String myJid, String roomBareJid) {

		TbHuddleInfoController controller = new TbHuddleInfoController(
				mAppContext);
		controller.open();

		if (!controller.isExistTheRecord(myJid, roomBareJid)) {

			controller.insert(myJid, roomBareJid, null, "n", "", "n", "n", "n",
					false);

		}

		controller.close();
	}

	/**
	 * 获取登录者入库myjid
	 * 
	 * */
	public String getDbMyJid() {

		String myJid = XmppData.getSingleton().getJid().getBare();

		return myJid;
	}

	private static boolean testA = true;

	/**
	 * 预添加数据库数据
	 * 
	 * */
	public synchronized void preLoadDataBaseData() {

		if (!testA) {
			return;
		} else {
			testA = false;
		}

		TbUIFeatureListController tbUIFeatureListController = new TbUIFeatureListController(
				mAppContext);
		tbUIFeatureListController.open();
		if (!tbUIFeatureListController.isExistRecords()) {

			tbUIFeatureListController.insert(mAppContext
					.getString(R.string.feature_id_multimessage), mAppContext
					.getString(R.string.feature_showname_multimessage),
					mAppContext.getString(R.string.feature_info_multimessage),
					"main_screen_multimessage", 0, 1, "");
			tbUIFeatureListController.insert(mAppContext
					.getString(R.string.feature_id_addressbook), mAppContext
					.getString(R.string.feature_showname_addressbook),
					mAppContext.getString(R.string.feature_info_addressbook),
					"main_screen_addressbook", -1, 1, "");
			tbUIFeatureListController.insert(
					mAppContext.getString(R.string.feature_id_phone),
					mAppContext.getString(R.string.feature_showname_phone),
					mAppContext.getString(R.string.feature_info_phone),
					"main_screen_phone", 3, 3, "");
			tbUIFeatureListController.insert(
					mAppContext.getString(R.string.feature_id_microblog),
					mAppContext.getString(R.string.feature_showname_microblog),
					mAppContext.getString(R.string.feature_info_microblog),
					"main_screen_microblog", 5, 5, "");

			tbUIFeatureListController.insert(
					mAppContext.getString(R.string.feature_id_feature),
					mAppContext.getString(R.string.feature_showname_feature),
					mAppContext.getString(R.string.feature_info_feature),
					"main_screen_feature", -1, 7, "");
			tbUIFeatureListController.insert(
					mAppContext.getString(R.string.feature_id_setting),
					mAppContext.getString(R.string.feature_showname_setting),
					mAppContext.getString(R.string.feature_info_setting),
					"main_screen_setting", -1, 6, "");
			tbUIFeatureListController.insert(
					mAppContext.getString(R.string.feature_id_huddle),
					mAppContext.getString(R.string.feature_showname_huddle),
					mAppContext.getString(R.string.feature_info_huddle),
					"main_screen_huddle", 8, 8, "");
			// /
			tbUIFeatureListController.insert(
					mAppContext.getString(R.string.feature_id_me),
					mAppContext.getString(R.string.feature_showname_me),
					mAppContext.getString(R.string.feature_info_me),
					"main_screen_me", -1, 9, "");

		}
		tbUIFeatureListController.close();

		// 登陆后插入功能表默认记录
		TbAccountUiFeatureListController controller = new TbAccountUiFeatureListController(
				mAppContext);
		controller.open();
		String myJid = this.getDbMyJid();
		if (!controller.isExistRecord(myJid)) {

			controller.insert(myJid,
					mAppContext.getString(R.string.feature_id_multimessage),
					"y");
			controller
					.insert(myJid, mAppContext
							.getString(R.string.feature_id_addressbook), "y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_id_phone), "y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_id_microblog), "y");

			controller.insert(myJid,
					mAppContext.getString(R.string.feature_id_feature), "y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_id_setting), "y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_id_huddle), "y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_id_me), "y");
		}

		controller.close();

	}

	/**
	 * 收到群聊消息处理入库信息（已存在则过滤(返回false)，否则插入(返回TRUE)）
	 * 
	 * @param msg
	 */
	public boolean onHandleRevHuddleMsgDb(HuddleChatMessageInfo msg) {

		// 如果已收到过该消息则屏蔽，否则入库：Tb_Record _Multi_Chat
		TbRecordMultiChatController tbRecordMultiChatController = new TbRecordMultiChatController(
				mAppContext);
		tbRecordMultiChatController.open();
		if (!"".equalsIgnoreCase(msg.getMessageId())) {

			if (tbRecordMultiChatController.isExistTheRecord(msg)) {

				tbRecordMultiChatController.close();

				return false;
			}
		}
		tbRecordMultiChatController.insert(msg);
		tbRecordMultiChatController.close();

		return true;
	}

	/**
	 * 群消息入库
	 * 
	 * @param msg
	 */
	public void insertHuddleChatMsgToDb(HuddleChatMessageInfo msg) {
		TbRecordMultiChatController tbRecordMultiChatController = new TbRecordMultiChatController(
				mAppContext);
		tbRecordMultiChatController.open();
		tbRecordMultiChatController.insert(msg);
		tbRecordMultiChatController.close();
	}

	/**
	 * 收到群聊消息处理（ 消息汇总库）如果存在则更新否则插入，确保始终存在最新一条记录：Tb_Record _ChatSum
	 * 
	 * @param msg
	 * @return
	 */
	public int onHandleRevHuddleMsgChatSumDb(HuddleChatMessageInfo msg) {

		TbRecordChatSumController tbRecordChatSumController = new TbRecordChatSumController(
				mAppContext);
		tbRecordChatSumController.open();
		int id = tbRecordChatSumController.queryIdForRecord(msg.getMyJid(),
				msg.getRoomJid());

		if (id != -1) {
			// 更新
			String partMessage = msg.getMsgText();

			if (partMessage.length() > 50) {

				partMessage = partMessage.substring(0, 50);
			}
			// tbRecordChatSumController.updateRecord(id, msg.getDbTime(),
			// msg.getTimeStamp(), msg.getGeoloc(), partMessage);
			tbRecordChatSumController.updateRecord(id, "0", msg.getTimeStamp(),
					msg.getGeoloc(), msg.getFromName() + ":" + partMessage);
		} else {

			// 插入
			String partMessage = msg.getMsgText();

			if (partMessage.length() > 50) {

				partMessage = partMessage.substring(0, 50);
			}

			// id = (int)tbRecordChatSumController.insert(msg.getMyJid(),
			// msg.getRoomJid(), "", "n", ChatMessageUtil.getDbTime(),
			// msg.getTimeStamp(), msg.getGeoloc(), partMessage);
			id = (int) tbRecordChatSumController.insert(msg.getMyJid(),
					msg.getRoomJid(), "", "n", "0", msg.getTimeStamp(),
					msg.getGeoloc(), msg.getFromName() + ":" + partMessage,
					msg.getInfoType());
		}

		tbRecordChatSumController.close();

		return id;
	}

	/**
	 * 收到群聊消息后处理界面通知
	 * 
	 * @param id
	 * @param msg
	 */
	public void onHandleRevHuddleMsgUINotificationDb(int id,
			HuddleChatMessageInfo msg) {

		// 入库：Tb_UI_Notification 界面通知
		if (id != -1) {

			TbUINotificationController tbUINotificationController = new TbUINotificationController(
					mAppContext);
			tbUINotificationController.open();

			tbUINotificationController.insert(msg.getMyJid(),
					DbConstantsType.NOTIFICATION_TYPE_UNREAD_HUDDLE_MSGS,
					String.valueOf(id), msg.getTimeStamp());

			tbUINotificationController.close();
		}
	}

	/**
	 * 收到微博消息后处理界面通知
	 * 
	 * @param id
	 *            microBlogId（区别与 NotificationType == 2 的时候）
	 * @param info
	 */
	public void onHandleMicroBlogMsgUINotificationDb(String id,
			MicroblogInfo info) {

		// 入库：Tb_UI_Notification 界面通知

		TbUINotificationController tbUINotificationController = new TbUINotificationController(
				mAppContext);
		tbUINotificationController.open();

		tbUINotificationController.insert(info.getMyJid(),
				Constants.NotificationMsgType.MICROBLOG + "", id,
				info.getPublishTime());

		tbUINotificationController.close();

	}

	/**
	 * 预添加数据库数据
	 * 
	 * */
	public synchronized void preLoadInputControlDataBaseData() {

		TbChatInputFeatureListController tbChatInputFeatureListController = new TbChatInputFeatureListController(
				mAppContext);
		tbChatInputFeatureListController.open();
		if (!tbChatInputFeatureListController.isExistRecords()) {

			tbChatInputFeatureListController
					.insert(mAppContext
							.getString(R.string.feature_chatinput_id_emoji),
							"",
							mAppContext
									.getString(R.string.feature_chatinput_showname_emoji),
							"", "cellinputmessage_face_icon", 0, 1, "");

			tbChatInputFeatureListController
					.insert(mAppContext
							.getString(R.string.feature_chatinput_id_photo),
							"",
							mAppContext
									.getString(R.string.feature_chatinput_showname_photo),
							"", "cellinputmessage_photo_icon", -1, 2, "");
			tbChatInputFeatureListController
					.insert(mAppContext
							.getString(R.string.feature_chatinput_id_phone),
							"",
							mAppContext
									.getString(R.string.feature_chatinput_showname_phone),
							"", "cellinputmessage_phone_icon", 3, 3, "");
			tbChatInputFeatureListController
					.insert(mAppContext
							.getString(R.string.feature_chatinput_id_vcall),
							"",
							mAppContext
									.getString(R.string.feature_chatinput_showname_vcall),
							"", "cellinputmessage_vcall_icon", 5, 4, "");

			tbChatInputFeatureListController
					.insert(mAppContext
							.getString(R.string.feature_chatinput_id_location),
							"",
							mAppContext
									.getString(R.string.feature_chatinput_showname_location),
							"", "cellinputmessage_location_icon", -1, 5, "");
			tbChatInputFeatureListController
					.insert(mAppContext
							.getString(R.string.feature_chatinput_id_importantmsg),
							"",
							mAppContext
									.getString(R.string.feature_chatinput_showname_importantmsg),
							"", "cellinputmessage_importmsg_icon", -1, 6, "");
			tbChatInputFeatureListController
					.insert(mAppContext
							.getString(R.string.feature_chatinput_id_burn),
							"",
							mAppContext
									.getString(R.string.feature_chatinput_showname_burn),
							"", "cellinputmessage_burnafterreading_icon", 8, 7,
							"");

		}
		tbChatInputFeatureListController.close();

		// 登陆后插入功能表默认记录
		TbAccountChatInputFeatureListController controller = new TbAccountChatInputFeatureListController(
				mAppContext);
		controller.open();
		String myJid = this.getDbMyJid();
		if (!controller.isExistRecord(myJid)) {

			controller.insert(myJid,
					mAppContext.getString(R.string.feature_chatinput_id_emoji),
					"y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_chatinput_id_photo),
					"y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_chatinput_id_phone),
					"y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_chatinput_id_vcall),
					"y");

			controller.insert(myJid, mAppContext
					.getString(R.string.feature_chatinput_id_location), "y");
			controller
					.insert(myJid,
							mAppContext
									.getString(R.string.feature_chatinput_id_importantmsg),
							"y");
			controller.insert(myJid,
					mAppContext.getString(R.string.feature_chatinput_id_burn),
					"y");
		}

		controller.close();

	}

	/**
	 * 返回消息的类型
	 * 
	 * @return
	 */
	public String getReceiveMsgType() {

		return msgType;
	}

	/**
	 * 解析收到的xhtml信息,如果含有多媒体消息，要预处理（比如缩略图下载，替换为本地地址等）
	 * 
	 * @param msgText
	 *            纯文本
	 * @param html
	 *            xhtml消息
	 * @param jidOrRoomJid
	 *            房间jid
	 * @return
	 */
	public String preParseXhtmlMsg(String msgText, String html,
			String jidOrRoomJid) {

		Document doc = Jsoup.parse(html);
		Elements links = doc.getElementsByTag("p");

		// if (links.isEmpty()) {
		//
		// msgType = msgText;
		//
		// return msgText;
		// }
		for (Element link : links) {
			if (link.select("a").isEmpty()) {
				// 表示为纯文字，为以后扩展使用
				msgType = msgText;

			} else {
				// 可能是音频 图片（图片、地理位置）
				String src = link.html(link.html()).select("img").first()
						.attr("src");
				String href = link.html(link.html()).select("a").attr("href");

				if (href.trim().indexOf(HuddleChatActvity.LOCATION_MARK) == 0) {
					// 位置 - 使用默认缩略图

				} else if (src == null || "".equalsIgnoreCase(src)) {
					// 音频 - 把音频文件先下载下来
					msgType = "[语音]";

					// 1、构造下载图片的存储位置
					FilePathUtil filePathUtil = new FilePathUtil(mAppContext);
					String audioPath = filePathUtil.getAudioPath(jidOrRoomJid);
					// 2、音频文件
					boolean downloadResult = HttpDownLoadFileClient
							.downLoadFile(href, audioPath);

					// 3、将xhtml中的url替换为本地url地址
					if (downloadResult) {
						// 音频文件下载成功

						link.html(link.html()).select("a").removeAttr("href");
						link.html(link.html()).select("a")
								.attr("href", audioPath);
						// link.html();

					} else {
						// 音频文件下载失败

					}

				} else {
					// 图片 - 把缩略图预先下载下来

					// 1、构造下载图片的存储位置
					FilePathUtil filePathUtil = new FilePathUtil(mAppContext);
					String picPath = filePathUtil.getPicPath(jidOrRoomJid);
					String thumbnailPath = filePathUtil
							.getPicThumbnail(picPath);
					// 2、下载图片
					boolean downloadResult = HttpDownLoadFileClient
							.downLoadFile(src, thumbnailPath);

					// 3、将xhtml中的url替换为本地url地址
					if (downloadResult) {
						// 下载成功
						// String te =
						// link.html(link.html()).select("img").first()
						// .attr("src").replace(src,thumbnailPath);

						link.html(link.html()).select("img").first()
								.removeAttr("src");
						link.html(link.html()).select("img").first()
								.attr("src", thumbnailPath);
						// link.html();

					} else {
						// 使用默认图片代替

					}
					msgType = "[图片]";

				}

			}
		}
		return doc.html();
	}

	/**
	 * 震屏
	 * 
	 */
	public void onShakeScreen() {
		Vibrator vibrator = (Vibrator) mAppContext
				.getSystemService(Service.VIBRATOR_SERVICE);
		// 第一个参数：该数组中第一个元素是等待多长的时间才启动震动，
		// 之后将会是开启和关闭震动的持续时间，单位为毫秒
		// 第二个参数：重复震动时在pattern中的索引，如果设置为-1则表示不重复震动
		vibrator.vibrate(new long[] { 1000, 50, 50, 100, 50 }, -1);

	}

	/**
	 * 微博消息预处理
	 * 
	 * @param microblog_
	 * @return
	 */
	public MicroblogInfo processRcvMicroBlog(Microblog microblog_) {

		String myJid = XmppData.getSingleton().getJid().getBare();
		String id = microblog_.id();
		String pId = microblog_.commentLink();
		String author = microblog_.author();
		String contentType = MicroblogInfo.CONTENT_TYPE_TEXT;
		if (microblog_.type() == (Microblog.Type.Xhtml)) {
			contentType = MicroblogInfo.CONTENT_TYPE_XHTML;
		}

		String content = this.preParseMicroBlogXhtmlMsg(microblog_.content());
		String publishTime = microblog_.published();
		String city = microblog_.geoloc();
		String device = microblog_.device();
		String infoType = MicroblogInfo.MicroblogInfoType.Comment.ordinal()
				+ "";
		if ("".equals(pId) || pId == null) {
			infoType = MicroblogInfo.MicroblogInfoType.Microblog.ordinal() + "";
		}

		String name = XmppData.getSingleton().getFriend(author).getShowName(); // new
																				// XmppJid(author).getUserName();//使用的userName

		String sendOrRcvTag = MicroblogInfo.SEND_OR_RCV_TAG_RCV;
		String isSendFailed = "n";
		String dbTime = ChatMessageUtil.getDbTime();

		MicroblogInfo info = new MicroblogInfo(id, myJid, pId, author,
				contentType, content, publishTime, city, device, infoType,
				name, sendOrRcvTag, isSendFailed, dbTime);

		return info;

	}

	/**
	 * 预处理微博富文本
	 * 
	 * @param html
	 * @return
	 */
	public String preParseMicroBlogXhtmlMsg(String html) {

		Document doc = Jsoup.parse(html);
		Elements links = doc.getElementsByTag("p");

		for (Element link : links) {
			if (link.select("a").isEmpty()) {
				// 表示为纯文字，为以后扩展使用

			} else {
				// 可能是音频 图片（图片、地理位置）
				String src = link.html(link.html()).select("img").first()
						.attr("src");
				String href = link.html(link.html()).select("a").attr("href");

				if (href.trim().indexOf(
						MicroBlogWritingActivity.LOCATION_MARK_PRE) == 0) {
					// 位置 - 使用默认缩略图

				} else if (src == null || "".equalsIgnoreCase(src)) {
					// 音频 - 把音频文件先下载下来

					// 1、构造下载音频文件的存储位置
					FilePathUtil filePathUtil = new FilePathUtil(mAppContext);
					String audioPath = filePathUtil.getAudioPath(null);
					// 2、下载音频
					boolean downloadResult = HttpDownLoadFileClient
							.downLoadFile(href, audioPath);

					// 3、将xhtml中的url替换为本地url地址
					if (downloadResult) {
						// 音频文件下载成功

						link.html(link.html()).select("a").removeAttr("href");
						link.html(link.html()).select("a")
								.attr("href", audioPath);

					} else {
						// 音频文件下载失败

					}

				} else {
					// 图片 - 把缩略图预先下载下来

					// 1、构造下载图片的存储位置
					FilePathUtil filePathUtil = new FilePathUtil(mAppContext);
					String picPath = filePathUtil.getPicPath(null);
					String thumbnailPath = filePathUtil
							.getPicThumbnail(picPath);
					// 2、下载图片
					boolean downloadResult = HttpDownLoadFileClient
							.downLoadFile(src, thumbnailPath);

					// 3、将xhtml中的url替换为本地url地址
					if (downloadResult) {
						// 下载成功
						// String te =
						// link.html(link.html()).select("img").first()
						// .attr("src").replace(src,thumbnailPath);

						link.html(link.html()).select("img").first()
								.removeAttr("src");
						link.html(link.html()).select("img").first()
								.attr("src", thumbnailPath);
						// link.html();

					} else {
						// 使用默认图片代替

					}

				}

			}
		}
		return doc.html();
	}

	/**
	 * 微博入库
	 * 
	 */
	public void writeMicroBlogRecordIntoDb(MicroblogInfo info) {

		TbRecordMicroblogController controller = new TbRecordMicroblogController(
				mAppContext);
		controller.open();
		controller.insert(info);
		controller.close();

	}

	/**
	 * 更新发送标志
	 * 
	 * @param MicroblogId
	 */
	public void updataSendResultTag(String MicroblogId, String isSendFailed) {

		String myJid = XmppData.getSingleton().getJid().getBare();

		TbRecordMicroblogController controller = new TbRecordMicroblogController(
				mAppContext);
		controller.open();
		controller.updataSendResultTag(myJid, MicroblogId, isSendFailed);
		controller.close();

	}

	/**
	 * 根据群成员的昵称获取对应的jid
	 * 
	 * @param roomJid
	 * @param nickname
	 * @return
	 */
	public String getHuddleMemberJidByNickName(String roomJid, String nickname) {

		// 修改昵称后，跟你昵称查表获得对应的jid，如果==myjid，return；
		String myJid = this.getDbMyJid();

		TbMultiChatRoomInfoController tbMultiChatRoomInfoController = new TbMultiChatRoomInfoController(
				mAppContext);
		tbMultiChatRoomInfoController.open();
		String occupantJid = tbMultiChatRoomInfoController
				.getOccupantJidByOccupantName(myJid, roomJid, nickname);
		tbMultiChatRoomInfoController.close();

		return occupantJid;
	}

	/**
	 * 对收到的群会话消息解析成自己的结构体类型数据
	 * 
	 * @param room_
	 * @param nickname_
	 * @param occupantJid
	 * @param msg_
	 * @param xhtml_
	 * @return
	 */
	public HuddleChatMessageInfo resolveHuddleChatMsg(JID room_,
			String nickname_, String occupantJid, Message msg_, String xhtml_) {

		// String fromJid = msg_.from().full();// FullJid
		// --类似20140818155258@muc.fe.shenj.com/dgd001
		String myJid = this.getDbMyJid();
		String fromJid = occupantJid;
		String fromName = msg_.from().resource();// 发送方姓名
		String toJid = msg_.to().bare();
		String creatorJid = "";// Fulljid
		String roomJid = room_.bare();// 聊天房间号Bare JID
		String messageId = msg_.id();// xmpp协议消息id
		String messageType = MessageComType.MessageType_GROUPCHAT;//
		// xmpp协议消息type
		String threadId = msg_.thread();// xmpp协议消息退回(不确定是否对)
		String geoloc = ChatMessageUtil.getGeoloc();// 地理位置
		String timeStamp = ChatMessageUtil.getStampTime();// 协议时间
		String dbTime = timeStamp;// 入库时间
		int infoType = Constants.MsgInfoType.INSTANT;// 消息类型
		String isDelete = "n";
		String sendSuccessTag = "y";

		String bodyType = ChatMessageUtil.MSG_BODY_TYPE_TEXT;// text/xhtml;
		String msgBody = msg_.body();
		String msgText = msg_.body();

		if ((xhtml_ != null) && (!xhtml_.equals(Constants.EMPTY_STRING))) {

			bodyType = ChatMessageUtil.MSG_BODY_TYPE_XHTML;
			// 解析xhtml，将多媒体文件预先下载
			msgBody = this.preParseXhtmlMsg(msg_.body(), xhtml_, roomJid);// 可能要替换为xhtml_

			msgText = this.getReceiveMsgType();
		}

		if (msgBody == null || msgText == null) {

			return null;
		}

		HuddleChatMessageInfo msg = new HuddleChatMessageInfo();

		msg.setMyJid(myJid);
		msg.setFromJid(fromJid);
		msg.setFromName(fromName);
		msg.setCreatorJid(creatorJid);
		msg.setRoomJid(roomJid);
		msg.setMessageId(messageId);
		msg.setMessageType(messageType);
		msg.setThreadId(threadId);// ThreadID
		msg.setGeoloc(geoloc);// 设置地理位置
		msg.setTimeStamp(timeStamp);
		msg.setDbTime(dbTime);
		msg.setBodyType(bodyType);
		msg.setMsgBody(msgBody);
		msg.setMsgText(msgText);
		msg.setInfoType(infoType);
		msg.setIsDelete(isDelete);
		msg.setSendOrRcvTag(Constants.MsgSendOrRcvTag.REV);
		msg.setSendSuccessTag(sendSuccessTag);

		return msg;
	}

	/**
	 * 解析群震动信息
	 * 
	 * @param id_
	 * @param occupantJid
	 * @param room_
	 * @param nickname_
	 * @return
	 */
	public HuddleChatMessageInfo resolveHuddleAttentionMessage(String id_,
			String occupantJid, JID room_, String nickname_) {

		// String fromJid = msg_.from().full();// FullJid
		// --类似20140818155258@muc.fe.shenj.com/dgd001
		String myJid = this.getDbMyJid();
		String fromJid = occupantJid;
		String fromName = nickname_;// 发送方姓名
		String toJid = myJid;
		String creatorJid = "";// Fulljid
		String roomJid = room_.bare();// 聊天房间号Bare JID
		String messageId = id_;// xmpp协议消息id
		String messageType = MessageComType.MessageType_GROUPCHAT;//
		// xmpp协议消息type
		String threadId = ChatMessageUtil.getThreadId();// xmpp协议消息退回(不确定是否对)
		String geoloc = ChatMessageUtil.getGeoloc();// 地理位置
		String timeStamp = ChatMessageUtil.getStampTime();// 协议时间
		String dbTime = timeStamp;// 入库时间
		int infoType = Constants.MsgInfoType.ATTENTION;// 震动类型
		String isDelete = "n";
		String sendSuccessTag = "y";

		String bodyType = ChatMessageUtil.MSG_BODY_TYPE_TEXT;// text/xhtml;
		String msgBody = nickname_
				+ mAppContext
						.getString(R.string.cellonemessage_rev_huddle_attention_msg);
		String msgText = msgBody;

		HuddleChatMessageInfo msg = new HuddleChatMessageInfo();

		msg.setMyJid(myJid);
		msg.setFromJid(fromJid);
		msg.setFromName(fromName);
		msg.setCreatorJid(creatorJid);
		msg.setRoomJid(roomJid);
		msg.setMessageId(messageId);
		msg.setMessageType(messageType);
		msg.setThreadId(threadId);// ThreadID
		msg.setGeoloc(geoloc);// 设置地理位置
		msg.setTimeStamp(timeStamp);
		msg.setDbTime(dbTime);
		msg.setBodyType(bodyType);
		msg.setMsgBody(msgBody);
		msg.setMsgText(msgText);
		msg.setInfoType(infoType);
		msg.setIsDelete(isDelete);
		msg.setSendOrRcvTag(Constants.MsgSendOrRcvTag.REV);
		msg.setSendSuccessTag(sendSuccessTag);

		return msg;
	}

	/**
	 * 解析地理位置消息
	 * 
	 * @param id_
	 * @param room_
	 * @param nickname_
	 * @param geoloc_
	 * @return
	 */
	public HuddleChatMessageInfo resolveHuddleGeolocMessage(String id_,
			String occupantJid, JID room_, String nickname_, Geoloc geoloc_) {

		// String fromJid = msg_.from().full();// FullJid
		// --类似20140818155258@muc.fe.shenj.com/dgd001
		String myJid = this.getDbMyJid();
		String fromJid = occupantJid;
		String fromName = nickname_;// 发送方姓名
		String toJid = myJid;
		String creatorJid = "";// Fulljid
		String roomJid = room_.bare();// 聊天房间号Bare JID
		String messageId = id_;// xmpp协议消息id
		String messageType = MessageComType.MessageType_GROUPCHAT;//
		// xmpp协议消息type
		String threadId = ChatMessageUtil.getThreadId();// xmpp协议消息退回(不确定是否对)
		String geoloc = ChatMessageUtil.getGeoloc();// 地理位置
		String timeStamp = ChatMessageUtil.getStampTime();// 协议时间
		String dbTime = timeStamp;// 入库时间
		int infoType = Constants.MsgInfoType.INSTANT;
		String isDelete = "n";
		String sendSuccessTag = "y";

		String bodyType = ChatMessageUtil.MSG_BODY_TYPE_GEOLOC;// text/xhtml;
		String msgBody = ChatMessageUtil.createGeolocMsg(geoloc_.lon(),
				geoloc_.lat());
		String msgText = ChatMessageUtil.MSG_TEXT_GEOLOC;

		HuddleChatMessageInfo msg = new HuddleChatMessageInfo();

		msg.setMyJid(myJid);
		msg.setFromJid(fromJid);
		msg.setFromName(fromName);
		msg.setCreatorJid(creatorJid);
		msg.setRoomJid(roomJid);
		msg.setMessageId(messageId);
		msg.setMessageType(messageType);
		msg.setThreadId(threadId);// ThreadID
		msg.setGeoloc(geoloc);// 设置地理位置
		msg.setTimeStamp(timeStamp);
		msg.setDbTime(dbTime);
		msg.setBodyType(bodyType);
		msg.setMsgBody(msgBody);
		msg.setMsgText(msgText);
		msg.setInfoType(infoType);
		msg.setIsDelete(isDelete);
		msg.setSendOrRcvTag(Constants.MsgSendOrRcvTag.REV);
		msg.setSendSuccessTag(sendSuccessTag);

		return msg;

	}

	public HuddleChatMessageInfo resolveHuddleInvitationMessage(
			String invitorJid, JID room_) {

		String myJid = this.getDbMyJid();
		String fromJid = invitorJid;
		String fromName = XmppData.getSingleton().getFriend(invitorJid)
				.getShowName();
		;// 发送方姓名
		String toJid = myJid;
		String creatorJid = "";// Fulljid
		String roomJid = room_.bare();// 聊天房间号Bare JID
		String messageId = ChatMessageUtil.getMsgId();// 因为该条消息是模拟出来的，所以自己生成
		String messageType = MessageComType.MessageType_GROUPCHAT;//
		// xmpp协议消息type
		String threadId = ChatMessageUtil.getThreadId();// xmpp协议消息退回(不确定是否对)
		String geoloc = ChatMessageUtil.getGeoloc();// 地理位置
		String timeStamp = ChatMessageUtil.getStampTime();// 协议时间
		String dbTime = timeStamp;// 入库时间
		int infoType = Constants.MsgInfoType.HINT;// 震动类型
		String isDelete = "n";
		String sendSuccessTag = "y";

		String bodyType = ChatMessageUtil.MSG_BODY_TYPE_TEXT;// text/xhtml;
		String msgBody = fromName
				+ mAppContext
						.getString(R.string.huddle_chat_create_huddle_invite_hint);
		String msgText = msgBody;

		HuddleChatMessageInfo msg = new HuddleChatMessageInfo();

		msg.setMyJid(myJid);
		msg.setFromJid(fromJid);
		msg.setFromName(fromName);
		msg.setCreatorJid(creatorJid);
		msg.setRoomJid(roomJid);
		msg.setMessageId(messageId);
		msg.setMessageType(messageType);
		msg.setThreadId(threadId);// ThreadID
		msg.setGeoloc(geoloc);// 设置地理位置
		msg.setTimeStamp(timeStamp);
		msg.setDbTime(dbTime);
		msg.setBodyType(bodyType);
		msg.setMsgBody(msgBody);
		msg.setMsgText(msgText);
		msg.setInfoType(infoType);
		msg.setIsDelete(isDelete);
		msg.setSendOrRcvTag(Constants.MsgSendOrRcvTag.REV);
		msg.setSendSuccessTag(sendSuccessTag);

		return msg;
	}
}
