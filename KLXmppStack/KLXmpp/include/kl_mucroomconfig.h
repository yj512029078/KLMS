#ifndef KL_MUCROOMCONFIG_H__
#define KL_MUCROOMCONFIG_H__

#include "../../gloox/src/gloox.h"
#include "../../gloox/src/jid.h"
#include <string>
#include <list>

namespace kl
{
	/**
	 * @brief MUC房间配置参数。
	 *
	 * @author JI Yixuan
	 */
	struct MUCRoomConfig
	{
		// 默认构造函数，设置默认值
		MUCRoomConfig()
		{
			enableLogging = false;
			enableChangeSubject = true;
			isPublic = true;
			isPersistent = true;
			isModerated  = false;
			isMembersOnly = true;
			isPasswordProtected = false;
		}

		/**
		 * 房间名称。
		 */
		std::string roomname;

		/**
		 * 房间简介。
		 */
		std::string roomdesc;
		
		// 是否记录日志，默认：否。
		bool enableLogging;
		
		/**
		 * 是否允许住客修改房间主题，默认：否。
		 */
		bool enableChangeSubject;

		// 是否是公共房间，默认：是。
		bool isPublic;

		// 是否是持久房间，默认：否。
		bool isPersistent;

		// 是否是主持人房间，默认：否。
		bool isModerated;

		// 是否是仅成员房间，默认：否。
		bool isMembersOnly;

		// 是否是密码保护房间，默认：否。
		bool isPasswordProtected;

		// 房间密码
		std::string roompassword;
	};
}

#endif // KL_MUCROOMCONFIG_H__