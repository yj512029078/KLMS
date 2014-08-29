#ifndef KL_ENTITY_H__
#define KL_ENTITY_H__

#include <string>
#include <list>
#include <map>
#include "kl_disco_identity.h"




namespace kl
{
	/**
	 * @brief XMPP实体。
	 */
	struct Entity
	{
		// 实体JID
		std::string jid;
		
		// 实体名称
		std::string name;

		// 实体能力标识码
		std::string ver;

		// 实体Identity列表
		std::list<kl::disco::Identity*> identities;
		
		// 实体支持的特性
		std::list<std::string> features;

		// 子实体列表
		std::map<std::string, kl::Entity*> children;
	};
}

	kl::Entity* findEntity( kl::Entity* entity_, const std::string jid_ )
	{
		if ( entity_->jid == jid_ )
		{
			return entity_;
		}
		else
		{
			std::map<std::string, kl::Entity*>::const_iterator it = entity_->children.begin();
			kl::Entity* entity = 0;
			for ( ; it != entity_->children.end(); it++ )
			{
				entity = findEntity( (*it).second, jid_ );
				if ( entity )
				{
					break;
				}
			}

			return entity;
		}
	}

#endif // KL_ENTITY_H__