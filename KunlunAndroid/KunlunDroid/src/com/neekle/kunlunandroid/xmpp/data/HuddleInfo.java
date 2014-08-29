package com.neekle.kunlunandroid.xmpp.data;

import java.util.ArrayList;

import com.neekle.kunlunandroid.xmpp.data.TypeEnum.HuddleState;

public class HuddleInfo {

	// 群JID，是裸JID，主键，比如huddle@conference.openfire
	private String JID = "";

	// 群的当前状态
	private HuddleState State;

	// 群主题
	private String Subject;

	// 群备注名称
	private String RemarkName;
	
	//是否显示群昵称
	private boolean isShowNickName;

	// 群成员列表
	private ArrayList<HuddleMemberInfo> memberList;

	public HuddleInfo(String huddleJID) {
		JID = huddleJID;
		State = HuddleState.None;
		// Self = new HuddleMemberInfo(huddleJID, createrJID);
		Subject = null;
		RemarkName = null;
		memberList = new ArrayList<HuddleMemberInfo>();

	}

	public HuddleInfo() {
		State = HuddleState.None;
		// Self = new HuddleMemberInfo(huddleJID, createrJID);
		Subject = null;
		RemarkName = null;
		memberList = new ArrayList<HuddleMemberInfo>();

	}

	public String getJID() {
		return JID;
	}

	public void setJID(String jID) {
		JID = jID;
	}

	public HuddleState getState() {
		return State;
	}

	public void setState(HuddleState state) {
		State = state;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getRemarkName() {
		return RemarkName;
	}

	public void setRemarkName(String remarkName) {
		RemarkName = remarkName;
	}

	public ArrayList<HuddleMemberInfo> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<HuddleMemberInfo> memberList) {
		this.memberList = memberList;
	}

	public boolean isShowNickName() {
		return isShowNickName;
	}

	public void setShowNickName(boolean isShowNickName) {
		this.isShowNickName = isShowNickName;
	}
	

}
