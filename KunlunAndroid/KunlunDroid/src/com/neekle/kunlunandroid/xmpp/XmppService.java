package com.neekle.kunlunandroid.xmpp;

import java.util.ArrayList;
import java.util.List;

import org.xmpp.myWRAP.Geoloc;
import org.xmpp.myWRAP.IMailSMTPInfo;
import org.xmpp.myWRAP.JID;
import org.xmpp.myWRAP.MUCRoomConfig;
import org.xmpp.myWRAP.MUCRoomInvitationType;
import org.xmpp.myWRAP.Microblog;
import org.xmpp.myWRAP.Presence;
import org.xmpp.myWRAP.StringList;
import org.xmpp.myWRAP.XmppStack;
import org.xmpp.myWRAP.klcppwrapJNI;

import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.text.StaticLayout;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.CellOneContactData;
import com.neekle.kunlunandroid.data.HuddleChatMessageInfo;
import com.neekle.kunlunandroid.data.MicroblogInfo;
import com.neekle.kunlunandroid.map.GeolocationData;
import com.neekle.kunlunandroid.util.ChatMessageUtil;
import com.neekle.kunlunandroid.xmpp.common.XmppConstants;
import com.neekle.kunlunandroid.xmpp.data.HuddleInfo;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;
import com.neekle.kunlunandroid.xmpp.data.XmppSingleChatMessage;

public class XmppService {

	private volatile static XmppService mSingleton;
	private XmppStack mXmppStack;

	private XmppService() {

	}

	public static XmppService getSingleton() {
		if (mSingleton == null) {
			synchronized (XmppService.class) {
				if (mSingleton == null) {
					mSingleton = new XmppService();
				}
			}
		}

		return mSingleton;
	}

	public void login(String jidString, String pwd, String hostIp, int port) {
		JID jid = new JID(jidString);

		// Construct XmppStack and register callback to it
		// mXmppStack = new XmppStack(jid, pwd, hostIp, port);
		mXmppStack = new XmppStack(jid, pwd, hostIp, port);
		MyXmppCallback mxc = MyXmppCallback.getSingleton();
		mxc.setXmppStack(mXmppStack);
		mXmppStack.registerXmppCallback(mxc);

		login(mXmppStack);
	}

	public boolean getIsLogined() {
		// JID jid = new JID(jidString);
		//
		// XmppStack xmppStack = new XmppStack(jid, pwd, hostIp, port);
		// boolean isLogined = xmppStack.isLogined();

		boolean isLogined = false;
		if (mXmppStack != null) {
			isLogined = mXmppStack.isLogined();
		}

		return isLogined;
	}

	private void login(final XmppStack xmppStack) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				mXmppStack.login();
			}
		});

		thread.start();
	}

	public void logout() {
		if (mXmppStack != null) {
			mXmppStack.logout();
			mXmppStack = null;
		}
	}

	public void deleteFriend(String jid) {
		JID jidObject = new JID(jid);
		mXmppStack.deleteRosterItem(jidObject);
	}

	public void addFriend(String jid, String nickName, String msg,
			ArrayList<String> groups) {
		JID jidObject = new JID(jid);
		// mXmppStack.addContact(jidObject, nickName);
		// mXmppStack.addRosterItem(jidObject, nickName, groups_)
		// mXmppStack.addRosterItem(jid_, name_, groups_)
		// mXmppStack.sub
		StringList stringList = new StringList();
		if ((groups != null) && (groups.size() != 0)) {
			int size = groups.size();
			for (int i = 0; i < size; i++) {
				String group = groups.get(i);
				stringList.add(group);
			}
		}

		mXmppStack.addRosterItem(jidObject, nickName, stringList, true, msg);
	}

	public void approveSubscription(String jid) {
		JID jidObject = new JID(jid);
		mXmppStack.approveSubscription(jidObject);
	}

	public void denySubscription(String jid) {
		JID jidObject = new JID(jid);
		mXmppStack.denySubscription(jidObject, true);
	}

	public void sendChatMessage(String toJid, String msg, String msgId,
			String xhtml, String subject, String thread) {
		JID to_ = new JID(toJid);
		mXmppStack.sendChatMessage(msgId, to_, msg, xhtml, subject, thread);
	}

	public void sendChatMessage(XmppSingleChatMessage data) {

		String id_ = data.getMessageId();
		JID to_ = new JID(data.getToBareJid());
		String body_ = data.getMsgText();
		String xhtml_ = data.getMsgBody();
		String subject_ = "";
		String thread_ = data.getThreadId();
		boolean amp_ = false;
		boolean receipts_ = false;

		if (Constants.MsgInfoType.IMPORTANT == data.getInfoType()) {
			// 重要消息
			receipts_ = true;

		}

		if (data.getBodyType().equals(ChatMessageUtil.MSG_BODY_TYPE_TEXT)) {
			// 纯文本

			mXmppStack.sendChatMessage(id_, to_, body_, Constants.EMPTY_STRING,
					subject_, thread_, amp_, receipts_);

		} else {
			// 富文本
			mXmppStack.sendChatMessage(id_, to_, body_, xhtml_, subject_,
					thread_, amp_, receipts_);

		}

	}

	/************************************************
	 * @author kevin
	 ******************************************************/
	/**
	 * 创建群 -创建房间 说明：创建房间 +配置持久化房间
	 * 
	 * @param room_
	 *            :格式 a@b/c a:房间名；b:muc服务名 ；c:在房间内显示的个人昵称
	 * 
	 * */
	public void createHuddle(HuddleInfo huddleInfo) {
		// 生成房间jid
		JID room_ = new JID(huddleInfo.getJID());

		// 房间配置
		MUCRoomConfig config_ = new MUCRoomConfig();
		// 允许修改主题
		config_.setEnableChangeSubject(true);
		// 不允许记录群聊日志
		config_.setEnableLogging(false);
		// 设为紧成员房间
		config_.setIsMembersOnly(true);
		// 设为非主持人房间
		config_.setIsModerated(false);
		// 设为非密码保护房间
		config_.setIsPasswordProtected(false);
		// 设为持久房间
		config_.setIsPersistent(true);

		// 设为公共房间
		config_.setIsPublic(true);

		mXmppStack.createMUCRoom(room_, config_);
	}

	// 邀请群成员
	public void inviteMembersIntoHuddle(XmppJid huddleRoomJID,
			ArrayList<String> huddleMembersList) {

		// 发送离线邀请（采用间接邀请方式）

		JID room_ = new JID(huddleRoomJID.getBare());

		StringList owners_ = new StringList();

		for (String member : huddleMembersList) {

			String jid = member;

			owners_.add(jid);

			JID invitee_ = new JID(jid);

			mXmppStack.inviteIntoMUCRoom(room_, invitee_,
					MUCRoomInvitationType.Direct);
			// 昆仑客户端最终确定使用直接邀请方式
			// mXmppStack.inviteIntoMUCRoom(room_, invitee_,
			// MUCRoomInvitationType.Mediated);
		}

		// 将提交的成员列表设置为owner
		mXmppStack.modifyMUCRoomOwnerList(room_, owners_);

	}

	// 群 - 删除群成员
	public void deleteMemberWithNicknameFromHuddle(String roomJid,
			String occupantNickname_, String userJid) {

		JID room_ = new JID(roomJid);
		JID user_ = new JID(userJid);
		// 1、收回群成员授权
		mXmppStack.revokeMUCRoomMembership(room_, user_);
		// 2、剔出群聊房间
		mXmppStack.kickOutMUCRoom(room_, occupantNickname_);

	}

	// 群- 接受群邀请进入群
	public void enterHuddle(XmppJid room) {

		JID room_ = new JID(room.getFull());
		mXmppStack.enterMUCRoom(room_);

	}

	// 群-查询房间信息
	public void queryHuddleInfo(XmppJid room) {
		JID room_ = new JID(room.getFull());
		mXmppStack.queryMUCRoomInfo(room_);
	}

	// 群- 请求获取群成员列表
	public void queryHuddleOwnerList(XmppJid room) {
		JID room_ = new JID(room.getFull());
		mXmppStack.requestMUCRoomOwnerList(room_);
	}

	// 群- 查询房间配置信息
	public void queryMUCRoomConfig(XmppJid room) {

		JID room_ = new JID(room.getFull());

		mXmppStack.queryMUCRoomConfig(room_);

	}

	// 创建群-发送群消息
	public void sendMessageToHuddle(HuddleChatMessageInfo msg) {

		String id_ = msg.getMessageId();
		String roomJid = msg.getRoomJid();
		String msg_ = msg.getMsgText();
		String xhtml_ = msg.getMsgBody();

		JID room_ = new JID(roomJid);

		if (msg.getBodyType().equals(ChatMessageUtil.MSG_BODY_TYPE_TEXT)) {
			// 纯文本
			mXmppStack.sendMUCRoomMessage(id_, room_, msg_,
					Constants.EMPTY_STRING);

		} else {

			mXmppStack.sendMUCRoomMessage(id_, room_, msg_, xhtml_);

		}

	}

	// 群 - 修改群聊主题
	public void changeSubjectOfHuddle(XmppJid room, String subject_) {

		JID room_ = new JID(room.getFull());
		mXmppStack.changeMUCRoomSubject(room_, subject_);
	}

	// 群 - 离开群：自己离开房间
	public void leaveHuddle(String roomJid) {

		JID room_ = new JID(roomJid);
		mXmppStack.exitMUCRoom(room_);

	}

	// 群 - 修改我的群昵称
	public void changeSelfNickname(XmppJid room, String nickname_) {

		JID room_ = new JID(room.getFull());

		mXmppStack.changeSelfNicknameInMUCRoom(room_, nickname_);

	}

	// 将群保存到通讯录中
	public void saveHuddleToAddressBook(String roomJid) {

		StringList groups_ = new StringList();
		JID jid_ = new JID(roomJid);
		boolean subscribe_ = false;
		mXmppStack.addRosterItem(jid_, "", groups_, subscribe_);

	}

	// 将群从通讯录中删除
	public void removeHuddleFromAddressBook(String roomJid) {

		JID jid_ = new JID(roomJid);
		mXmppStack.deleteRosterItem(jid_);
	}

	// 删群
	public void deleteHuddle(String roomJid) {

		JID room_ = new JID(roomJid);
		mXmppStack.destroyMUCRoom(room_);

	}

	// 退群
	public void exitHuddle(String roomJid, String occupantNickname_,
			String userJid) {
		// 1、收回自己的成员授权
		// 2、离开群
		JID room_ = new JID(roomJid);
		JID user_ = new JID(userJid);
		mXmppStack.revokeMUCRoomMembership(room_, user_);
		mXmppStack.exitMUCRoom(room_);

	}

	/**
	 * 发送位置消息
	 * 
	 * @param id_
	 *            消息id
	 * @param to_
	 *            接受者
	 * @param geoloc
	 *            地理位置
	 * @return
	 */
	public boolean sendLocationMessage(String id_, String jid,
			GeolocationData geolocData) {

		JID to_ = new JID(jid);

		Geoloc geoloc = new Geoloc();
		geoloc.setLat(geolocData.getLatitude());
		geoloc.setLon(geolocData.getLongitude());

		return mXmppStack.sendLocationMessage(id_, to_, geoloc);

	}

	/**
	 * 发送群位置消息
	 * 
	 * @param id_
	 * @param room
	 * @param geolocData
	 */
	public void sendMUCRoomLocationMessage(String id_, String room,
			GeolocationData geolocData) {

		JID room_ = new JID(room);

		Geoloc geoloc = new Geoloc();
		geoloc.setLat(geolocData.getLatitude());
		geoloc.setLon(geolocData.getLongitude());

		mXmppStack.sendMUCRoomLocationMessage(id_, room_, geoloc);

	}

	/**
	 * 发送回执
	 * 
	 * @param id_
	 * @param to_
	 * @param receiptId_
	 * @return
	 */
	public void sendReceiptMessage(String id_, JID to_, String receiptId_) {

		mXmppStack.sendReceiptMessage(id_, to_, receiptId_);
	}

	/**
	 * 发送震屏消息(双人会话)
	 * 
	 * @param to_
	 * @return
	 */
	public void sendAttentionMessage(String id_, String to_) {

		JID jid_ = new JID(to_);

		mXmppStack.sendAttentionMessage(id_, jid_);
	}

	/**
	 * 发送震屏消息(群聊)
	 * 
	 * @param room_
	 */
	public void sendMUCRoomAttentionMessage(String id, String room) {

		JID room_ = new JID(room);
		mXmppStack.sendMUCRoomAttentionMessage(id, room_);
	}

	/********************************** 微博 ******************/

	/**
	 * 发微博
	 * 
	 * @param info
	 */
	public void publishMicroblog(MicroblogInfo info) {

		Microblog microblog_ = new Microblog(info.getId());

		// 设置微博作者
		microblog_.setAuthor(info.getAuthor());
		// 设置微博内容类型：文本/富文本
		if (info.getContentType().equals(MicroblogInfo.CONTENT_TYPE_XHTML)) {

			microblog_.setType(microblog_.type().Xhtml);
		} else {

			microblog_.setType(microblog_.type().Text);
		}
		// 设置微博内容
		microblog_.setContent(info.getContent());
		// 设置所用设备
		microblog_.setDevice(info.getDevice());
		// 设置发布所在城市
		microblog_.setGeoloc(info.getCity());
		// 设置发布时间
		microblog_.setPublished(info.getPublishTime());

		mXmppStack.publish(microblog_);
	}

	/**
	 * 评论微博
	 * 
	 * @param info
	 */
	public void CommentMicroblog(MicroblogInfo info) {
		Microblog microblog_ = new Microblog(info.getId());

		// 设置微博作者
		microblog_.setAuthor(info.getAuthor());
		// 设置微博内容类型：文本/富文本
		if (info.getContentType().equals(MicroblogInfo.CONTENT_TYPE_XHTML)) {

			microblog_.setType(microblog_.type().Xhtml);
		} else {

			microblog_.setType(microblog_.type().Text);
		}
		// 设置微博pid
		microblog_.setCommentLink(info.getpId());
		// 设置微博内容
		microblog_.setContent(info.getContent());
		// 设置所用设备
		microblog_.setDevice(info.getDevice());
		// 设置发布所在城市
		microblog_.setGeoloc(info.getCity());
		// 设置发布时间
		microblog_.setPublished(info.getPublishTime());

		mXmppStack.publish(microblog_);

	}

	/**
	 * 删除微博
	 * 
	 * @param id_
	 */
	public void deleteMicroblog(MicroblogInfo info) {

		mXmppStack.deleteMicroblog(info.getId());

	}
}
