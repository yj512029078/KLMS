#ifndef KL_RECEIPT_H__
#define KL_RECEIPT_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/receipt.h"

namespace kl
{
	class KLXMPP_API Receipt : public gloox::Receipt
	{
	public:
		Receipt( const gloox::Tag* tag ) : gloox::Receipt( tag )
		{
			m_id = tag->findAttribute( "id" );
		}

		Receipt( gloox::Receipt::ReceiptType rcpt, const std::string& id = "" )
			: gloox::Receipt( rcpt ), m_id( id ) {}

		gloox::Tag* tag() const
		{
			gloox::Tag* tag = this->gloox::Receipt::tag();
			if ( !m_id.empty() ) 
			{
				tag->addAttribute( "id", m_id );
			}
			return tag;
		}

		virtual gloox::StanzaExtension* newInstance( const gloox::Tag* tag ) const
		{
			return new Receipt( tag );
		}

		std::string id() const { return m_id; }
	private:
      std::string m_id;
	};
}

#endif // KL_RECEIPT_H__