#ifndef KL_MUCROOMINFO_H__
#define KL_MUCROOMINFO_H__

#include <string>

namespace kl
{
	/**
	 * @brief 群聊房间信息。
	 *
	 * @author JI Yixuan
	 */
	struct MUCRoomInfo
	{
		MUCRoomInfo()
		{
			isPublic = true;
			isPersistent = false;
			isModerated = false;
			isMembersOnly = false;
			isPasswordProtected = false;
			isNonAnonymous = true;
			occupants = 0;
		}

		// 房间名称
		std::string roomname;

		// 房间简介
		std::string roomdesc;

		// 房间主题
		std::string roomsubject;

		// 当前房客数量
		int occupants;

		// 房间创建时间
		std::string creationdate;

		// 是否是公共房间 public vs hidden
		bool isPublic;

		// 是否是持久房间 persistent vs temporary
		bool isPersistent;

		// 是否是主持人房间 moderated vs unmoderated
		bool isModerated;

		// 是否是仅成员房间 membersonly vs open
		bool isMembersOnly;

		// 是否是密码保护房间 password-protected vs unsecured
		bool isPasswordProtected;

		// 是否是非匿名房间 non-anonymous vs semi-anonymous
		bool isNonAnonymous;
	};
}

#endif // KL_MUCROOMINFO_H__