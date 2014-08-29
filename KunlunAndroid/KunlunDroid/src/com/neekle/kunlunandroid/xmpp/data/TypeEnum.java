package com.neekle.kunlunandroid.xmpp.data;

public class TypeEnum {

	/**
	 * 群状态
	 * 
	 * */
	public enum HuddleState {
		None, Creating, // 正在创建
		Configing, // 正在配置
		Available // 可用
	}

	/**
	 * 在线状态
	 * 
	 * */
	public enum PresenceTypes {

		// / 在线
		Available,
		// / 离线
		Unavailable,
		// / 离开
		Away,
		// / 忙碌
		DND,
		// / 未知
		Unknown
	}
}
