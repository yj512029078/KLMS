#ifndef KL_DIRECTMUCINVITATION_H__
#define KL_DIRECTMUCINVITATION_H__

#include "../../gloox/src/stanzaextension.h"
#include "../../gloox/src/tag.h"

#include <string>

namespace kl
{
	class DirectMucInvitation : public gloox::StanzaExtension
	{
	public:
		
		DirectMucInvitation( const gloox::Tag* tag_ = 0 )
			: gloox::StanzaExtension( kl::StanzaExtensionType::ExtDirectMUCInvitation ), m_tag( 0 )
		{
			if ( tag_ )
			{
				m_tag = tag_->clone();
			}
		}
		
		virtual ~DirectMucInvitation()
		{
			delete m_tag;
		}

		virtual const std::string& filterString() const
		{
			static const std::string filter = "/message/x[@xmlns='" + kl::XMLNS_X_CONFERENCE + "']";
			return filter;
		}

		virtual gloox::StanzaExtension* newInstance( const gloox::Tag* tag_ ) const
		{
			return new DirectMucInvitation( tag_ );
		}

		virtual gloox::Tag* tag() const
		{
			return m_tag->clone();
		}

		virtual gloox::StanzaExtension* clone() const
		{
			DirectMucInvitation* x = new DirectMucInvitation();
			x->m_tag = m_tag ? m_tag->clone() : 0;
			return 0;
		}

	private:
		gloox::Tag* m_tag;
	};
}

#endif // KL_DIRECTMUCINVITATION_H__