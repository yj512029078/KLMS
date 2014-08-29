#ifndef KL_XMPPENTITY_H__
#define KL_XMPPENTITY_H__

#include "../../gloox/src/jid.h"
#include "../../gloox/src/disco.h"
#include "../../gloox/src/capabilities.h"

namespace kl
{
	class XmppEntity
	{
	public:
		XmppEntity( const gloox::JID& jid_ ) 
			: m_jid( jid_ ),
			  m_capabilities( 0 ),
			  m_discoInfo( 0 )
		{
		}

		virtual ~XmppEntity() {}

		gloox::Capabilities* capabilities() const { return m_capabilities; }

		gloox::Disco::Info* discoInfo() const { return m_discoInfo; }

		void setCapabilities( gloox::Capabilities* capabilities_ ) { m_capabilities = capabilities_; }

		void setDiscoInfo( gloox::Disco::Info* discoInfo_ ) { m_discoInfo = discoInfo_; }

	private:
		gloox::JID           m_jid;
		gloox::Capabilities* m_capabilities;
		gloox::Disco::Info*  m_discoInfo;

	};
}

#endif // KL_XMPPENTITY_H__ 