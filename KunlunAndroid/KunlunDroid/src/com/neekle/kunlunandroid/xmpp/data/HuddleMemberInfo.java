package com.neekle.kunlunandroid.xmpp.data;

/**
 * @author kevin 群成员信息
 * 
 * */
public class HuddleMemberInfo {

	// 群JID
	public String HuddleJID;

	// JID，主键
	public String JID;

	// 群内昵称
	public String Nickname;

	// 群内状态，如在线、离线、离开等
	public TypeEnum.PresenceTypes Status;

	public HuddleMemberInfo(String huddleJID, String memberJID) {
		HuddleJID = huddleJID;
		JID = memberJID;
		Nickname = null;
		Status = TypeEnum.PresenceTypes.Unknown;
	}

}
