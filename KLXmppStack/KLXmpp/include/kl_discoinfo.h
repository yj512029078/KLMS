#ifndef KL_DISCOINFO_H__
#define KL_DISCOINFO_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../KLXmpp/include/kl_identity.h"
#include "../../gloox/src/disco.h"
#include "../../gloox/src/util.h"

namespace kl
{
	/* 类前置声明 */

	typedef std::list<Identity*> IdentityList;

	class KLXMPP_API DiscoInfo
	{
	public:
		DiscoInfo()
		{
		}
#ifndef SWIG // SWIG过滤以下接口
		DiscoInfo( const gloox::Disco::Info& info_ ) : m_features( gloox::StringList( info_.features() ) )
		{
			gloox::Disco::IdentityList identities = info_.identities();
			gloox::Disco::IdentityList::const_iterator it = identities.begin();
			for ( ; it != identities.end(); it++ )
			{
				const std::string type = (*it)->type();
				m_identities.push_back( new Identity( (*it)->category(), (*it)->name(), (*it)->type() ) );
			}
		}
		virtual ~DiscoInfo()
		{
			gloox::util::clearList( m_identities );
		}
#endif // SWIG
	public: /* API */
		gloox::StringList* features() { return &m_features; }
		std::list<Identity*>* identities() { return &m_identities; }
	private:
		IdentityList      m_identities;
		gloox::StringList m_features;
	};
}

#endif // KL_DISCOINFO_H__