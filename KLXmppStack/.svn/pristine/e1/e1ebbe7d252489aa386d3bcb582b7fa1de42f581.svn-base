#ifndef KL_SERVICE_H__
#define KL_SERVICE_H__

#include <string>
#include <list>
#include "kl_disco_identity.h"

namespace kl
{
	/**
	 * @brief XMPP服务器提供的服务，通过disco获取到。
	 */
	struct Service
	{
		// 服务JID
		std::string jid;
		
		// 服务名称
		std::string name;

		// 服务Identity
		std::list<disco::Identity*> identities;
		
		// 服务支持的特性
		std::list<std::string> features;
	};
}

#endif // KL_SERVICE_H__